package com.deploy.demo.executor;

import com.amazonaws.services.ec2.model.*;

import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.elasticloadbalancing.model.*;

import com.deploy.demo.aws.AwsClientBuilder;
import com.deploy.demo.domain.WebApplication;
import com.deploy.demo.enums.WebAppStatus;
import com.deploy.demo.service.WebApplicationService;
import com.deploy.demo.utils.Consts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.*;

@Component
@Slf4j
public class InstanceService {


    @Autowired
    private AwsClientBuilder awsClientBuilder;

    @Autowired
    private WebApplicationService webApplicationService;

    @Value("${amazon.credentials-access-key-id}")
    private String credentialsAccessKeyId;

    @Value("${amazon.ami-id}")
    private String instanceAmiId;

    @Value("${amazon.security-group-id}")
    private String securityGroupId;

    @Value("${amazon.key-name}")
    private String keyName;

    ExecutorService executorService;
    Map<String, Future<?>> requests;

    public InstanceService(){
        executorService = Executors.newSingleThreadExecutor();

         requests=new HashMap<>();
    }

    public String runInstance(WebApplication webApplication,boolean useELB){

       Future<?> future = executorService.submit(createLaunchTask(webApplication,useELB));
       String uuid=UUID.randomUUID().toString();
       requests.put(uuid,future);

       return uuid;

    }


    private Callable<?> createLaunchTask(WebApplication webApplication,boolean useELB){
        Callable<?> task= ()->{
            log.info("Init triggering task");

            String str = Base64.getEncoder().encodeToString(webApplication.getUserData().getBytes());
            RunInstancesRequest runInstancesRequest =
                    new RunInstancesRequest();

            runInstancesRequest.withImageId(instanceAmiId)
                    .withInstanceType(InstanceType.T2Micro)
                    .withMinCount(1)
                    .withMaxCount(1)
                    .withKeyName(keyName)
                    .withSecurityGroupIds(securityGroupId)
                    .withUserData(str);


            RunInstancesResult result = awsClientBuilder.getAmazonEC2Client().runInstances(
                    runInstancesRequest);

            Instance instance=result.getReservation().getInstances().get(0);


            if(useELB)
                createLoadBalancer(createInstanceElbCollection(instance));


            int retries=0;
            while(!describeOneInstance(instance.getInstanceId()).getState().getName().equalsIgnoreCase("running")&&retries<20){

                    Thread.sleep(5000);
                    log.info("Waiting instance to be in running state!! and increasing retries ");
                    retries++;

            }
            instance=describeOneInstance(instance.getInstanceId());
            WebApplication existingWebApp=getWebAppByName(webApplication.getName());
            if(existingWebApp!=null){
                log.info("Setting instance information to web application");
                webApplicationService.updateAndReturn(appendInstanceInfo(existingWebApp,instance));
            }


            log.info("End  triggering task");
            return result;
        };

        return task;
    }

    private WebApplication getWebAppByName(String name){
        return webApplicationService.findByName(name);
    }

    private WebApplication appendInstanceInfo(WebApplication webAppObject, Instance instance ){

        webAppObject.setInstanceId(instance.getInstanceId());
        webAppObject.setUrl(Consts.HTTP+instance.getPublicDnsName());
        webAppObject.setState(WebAppStatus.READY.getStatus());
        return webAppObject;
    }

    public Instance describeOneInstance(String instanceId){
        Instance instance = null;
        boolean done = false;

        DescribeInstancesRequest request = new DescribeInstancesRequest();
        request.withInstanceIds(instanceId);
        while(!done) {
            DescribeInstancesResult response = awsClientBuilder.getAmazonEC2Client().describeInstances(request);

            for(Reservation reservation : response.getReservations()) {
                for(Instance instanceOb : reservation.getInstances()) {
                    instance=instanceOb;
                   log.info(String.format(
                            "Found instance with id %s, " +
                                    "AMI %s, " +
                                    "type %s, " +
                                    "state %s " +
                                    "and monitoring state %s",
                           instanceOb.getInstanceId(),
                           instanceOb.getImageId(),
                           instanceOb.getInstanceType(),
                           instanceOb.getState().getName(),
                           instanceOb.getMonitoring().getState()));
                }
            }

            request.setNextToken(response.getNextToken());

            if(response.getNextToken() == null) {
                done = true;
            }
        }
        return instance;
    }

    public StopInstancesResult stopEc2Instance(String instanceId){
        StopInstancesRequest request = new StopInstancesRequest()
                .withInstanceIds(instanceId);

        return awsClientBuilder.getAmazonEC2Client().stopInstances(request);
    }

    public TerminateInstancesResult terminateEc2Instance(String instanceId){
        TerminateInstancesRequest request = new TerminateInstancesRequest()
                .withInstanceIds(instanceId);


        return awsClientBuilder.getAmazonEC2Client().terminateInstances(request);
    }



    public LaunchInstanceStatus getLaunchStatus(String requestId) {
        if(requests.get(requestId)!=null) {
            return this.performLaunchStatus(requestId);
        } else {
            return LaunchInstanceStatus.NOT_FOUND;
        }
    }

    private LaunchInstanceStatus performLaunchStatus(String requestId){
        if(requests.get(requestId).isCancelled())
            return LaunchInstanceStatus.CANCELLED;
        else if(requests.get(requestId).isDone()) {
            try {
                if( requests.get(requestId).get()!=null)
                    return LaunchInstanceStatus.DONE;
                else
                    return LaunchInstanceStatus.ERROR;
            } catch (Exception e) {
                log.error("Error getting task");
                e.printStackTrace();
            }
            return LaunchInstanceStatus.ERROR;
        } else
            return LaunchInstanceStatus.PROCESSING;
    }


    public RunInstancesResult waitForRequestId(String requestId) throws ExecutionException, InterruptedException {

        while (getLaunchStatus(requestId).equals(LaunchInstanceStatus.PROCESSING)) {
            Thread.sleep(500);

        }
        return (RunInstancesResult) requests.get(requestId).get();
    }



    private Collection<com.amazonaws.services.elasticloadbalancing.model.Instance> createInstanceElbCollection(Instance instance){
        Collection<com.amazonaws.services.elasticloadbalancing.model.Instance> collection = new ArrayList<>();
        com.amazonaws.services.elasticloadbalancing.model.Instance elbInstance= new com.amazonaws.services.elasticloadbalancing.model.Instance();
        elbInstance.setInstanceId(instance.getInstanceId());
        collection.add(elbInstance);
        return  collection;
    }

    public void createLoadBalancer(Collection<com.amazonaws.services.elasticloadbalancing.model.Instance> instances){

        log.info("Creating load balancer");
        CreateLoadBalancerRequest lbRequest = new CreateLoadBalancerRequest();
        lbRequest.setLoadBalancerName("loader");
        List<Listener> listeners = new ArrayList<>(1);
        listeners.add(new Listener("HTTP", 80, 80));
        lbRequest.withAvailabilityZones(Consts.availabiltyZones);
        lbRequest.setListeners(listeners);

        CreateLoadBalancerResult lbResult=awsClientBuilder.getAmazonElbClient().createLoadBalancer(lbRequest);
        log.info("Created load balancer loader=>" +lbResult.getDNSName());

        if(instances!=null && !instances.isEmpty()){
            RegisterInstancesWithLoadBalancerRequest register =new RegisterInstancesWithLoadBalancerRequest();
            register.setLoadBalancerName("loader");
            register.setInstances(instances);
            awsClientBuilder.getAmazonElbClient().registerInstancesWithLoadBalancer(register);
        }

    }

    public void listLoadBalancer(){
        DescribeLoadBalancersResult lb = awsClientBuilder.getAmazonElbClient().describeLoadBalancers();
        lb.getLoadBalancerDescriptions();

    }






    @PreDestroy
    public void shutdownNow() {
        executorService.shutdownNow();
    }

}
