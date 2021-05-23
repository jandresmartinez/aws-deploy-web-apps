package com.deploy.demo.executor;

import com.amazonaws.services.ec2.model.Instance;

import com.amazonaws.services.ec2.model.StopInstancesResult;
import com.deploy.demo.aws.AwsClientBuilder;
import com.deploy.demo.domain.WebApplication;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class InstanceServiceTest {

    @Autowired
    private InstanceService instanceService;

    @Autowired
    private AwsClientBuilder awsClientBuilder;

    private String instanceId;


    @Test
    @Order(1)
    @DisplayName("Triggers an AWS EC2 instance")
    void testLaunchNewAWSInstance() throws ExecutionException, InterruptedException {
        log.info("Executing ");
        String requestId= instanceService.runInstance(createDummyWebApp(),false);

        log.info("Transfer status = " + instanceService.getLaunchStatus(requestId));

        Assertions.assertThat(instanceService.getLaunchStatus(requestId)).isEqualTo(LaunchInstanceStatus.PROCESSING);

        instanceId= instanceService.waitForRequestId(requestId).getReservation().getInstances().get(0).getInstanceId();
        Assertions.assertThat(instanceId).isNotNull();

    }


    @Test
    @Order(2)
    @DisplayName("Display existing AWS EC2 instance")
    void testDisplayExistingAWSInstance() throws ExecutionException, InterruptedException {
        log.info("Displaying instance =>"+instanceId);
        Instance instance = instanceService.describeOneInstance(instanceId);
        Assertions.assertThat(instance).isNotNull();
        if (instance!=null){
            while (!instanceService.describeOneInstance(instanceId).getState().getName().equalsIgnoreCase("running")){
                try {
                    Thread.sleep(5000);
                    log.info("Waiting instance to be in running state!!");
                } catch (InterruptedException e) {
                    log.error("Error while waiting instance to launch");
                    e.printStackTrace();
                }


            }
        }

    }



    @Test
    @Order(3)
    @DisplayName("Stop an existing AWS EC2 instance")
    void testStopExistingAWSInstance() throws ExecutionException, InterruptedException {
        log.info("Stopping instance =>"+instanceId);
        StopInstancesResult stopResult = instanceService.stopEc2Instance(instanceId);
        Assertions.assertThat(stopResult.getStoppingInstances().get(0).getCurrentState().getName()).isNotEqualTo("running");
        Assertions.assertThat(instanceService.describeOneInstance(instanceId).getState().getName()).isEqualToIgnoringCase("stopping");
    }

    @Test
    @Order(4)
    @DisplayName("List ELB")
    void tesListELBAWS() throws ExecutionException, InterruptedException {
        log.info("Listing load balancers");
    }


    private WebApplication createDummyWebApp(){
        WebApplication dummyWebApp= new WebApplication();
        dummyWebApp.setName("test");

        dummyWebApp.setUserData(join(createDummyUserData(), "\n"));
        return dummyWebApp;

    }
    private List<String> createDummyUserData() {
        List<String> lines = new ArrayList<>();
        lines.add("#! /bin/bash");
        lines.add("curl -sSL https://raw.githubusercontent.com/bitnami/bitnami-docker-mediawiki/master/docker-compose.yml > docker-compose.yml");
        lines.add("docker-compose up -d");
        return  lines;
    }

    static String join(Collection<String> s, String delimiter) {
        StringBuilder builder = new StringBuilder();
        Iterator<String> iter = s.iterator();
        while (iter.hasNext()) {
            builder.append(iter.next());
            if (!iter.hasNext()) {
                break;
            }
            builder.append(delimiter);
        }
        return builder.toString();
    }

}