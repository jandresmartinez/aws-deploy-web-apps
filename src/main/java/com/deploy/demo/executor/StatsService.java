package com.deploy.demo.executor;

import com.amazonaws.services.cloudwatch.model.*;
import com.deploy.demo.aws.AwsClientBuilder;
import com.deploy.demo.dto.CloudWatchMetric;
import com.deploy.demo.enums.MetricGraphTypes;
import com.deploy.demo.enums.MetricStatisticsTypes;
import com.deploy.demo.service.WebApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class StatsService {


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

    private final static String NAMESPACE = "AWS/EC2";	//https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/aws-namespaces.html

    /**
     * Gets the metric result.
     *
     * @param startTime the start time
     * @param period the period
     * @param statistics the statistics
     * @return the metric result @see CloudWatchMetric
     */
    public List<CloudWatchMetric> getMetricResult(long startTime, int period ,Collection<String> statistics,String instanceId) {

        List<CloudWatchMetric> listTotalMetrics= new ArrayList<>();
        ListMetricsRequest request = new ListMetricsRequest()
                .withMetricName(MetricGraphTypes.CPU_UTILIZATION.getAwsValue())
                .withNamespace(NAMESPACE);
        boolean done = false;

        while(!done) {

            ListMetricsResult response = awsClientBuilder.getAmazonCloudWatchBuilder().listMetrics(request);

            for(Metric metric : response.getMetrics()) {
                if (metric.getDimensions().get(0).getValue().equalsIgnoreCase(instanceId)){
                    log.info("Retrieved metric" + metric.toString());
                    GetMetricStatisticsRequest getMetricStatisticsRequest = getMetricStatisticsRequest(metric.getNamespace(),
                            metric.getMetricName(), metric.getDimensions(),startTime,period,statistics);
                    GetMetricStatisticsResult result = awsClientBuilder.getAmazonCloudWatchBuilder().getMetricStatistics(getMetricStatisticsRequest);
                    listTotalMetrics.add(buildMetric(result));
                }
            }
            request.setNextToken(response.getNextToken());
            if(response.getNextToken() == null) {
                done = true;
            }
        }

        return listTotalMetrics;

    }

    /**
     * Gets the metric result.
     *
     * @return the metric result @see CloudWatchMetric
     */
    public List<Metric> listMetrics() {

        List<Metric> listTotalMetrics= new ArrayList<>();
        ListMetricsRequest request = new ListMetricsRequest()
                .withMetricName(MetricGraphTypes.CPU_UTILIZATION.getAwsValue())
                .withNamespace(NAMESPACE);
        boolean done = false;

        while(!done) {

            ListMetricsResult response = awsClientBuilder.getAmazonCloudWatchBuilder().listMetrics(request);

            for(Metric metric : response.getMetrics()) {
                listTotalMetrics.add(metric);
            }

            request.setNextToken(response.getNextToken());
            if(response.getNextToken() == null) {
                done = true;
            }
        }

        return listTotalMetrics;

    }

    /**
     * buildMetric the metric.
     *
     * @param result @see com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult
     * @return the cloudWatchMetric object to return it
     */
    private CloudWatchMetric buildMetric(GetMetricStatisticsResult result ) {

        CloudWatchMetric cloudWatchMetric = new CloudWatchMetric(MetricGraphTypes.CPU_UTILIZATION.getAwsValue(), MetricStatisticsTypes.AVERAGE.getAwsValue(), constructResponse(result));

        return cloudWatchMetric;
    }

    private List<Double> constructResponse(GetMetricStatisticsResult result){
        List<Double> responseList= new ArrayList<>();
        for(Datapoint dataPoint: result.getDatapoints()){
            responseList.add(dataPoint.getAverage());
        }
        return responseList;
    }



    /**
     * Gets the metric statistics request.
     *
     * @param NAMESPACE the namespace
     * @param METRIC_NAME the metric name
     * @param dimensions the dimensions
     * @param startTime the start time
     * @param period the period
     * @param statistics the statistics
     * @return the metric statistics request
     */
    public static GetMetricStatisticsRequest getMetricStatisticsRequest(String NAMESPACE, String METRIC_NAME, List<Dimension> dimensions,long startTime, int period, Collection<String> statistics) {

        GetMetricStatisticsRequest metricStatisticsRequest =new GetMetricStatisticsRequest();
        metricStatisticsRequest
                .withStartTime(new Date(new Date().getTime() - startTime))
                .withNamespace(NAMESPACE)
                .withPeriod(period)
                .withDimensions(dimensions)
                .withMetricName(METRIC_NAME)
                .withStatistics(statistics) 	// https://docs.aws.amazon.com/AmazonCloudWatch/laest/monitoring/cloudwatch_concepts.html#Statistic
                .withEndTime(new Date());


        return metricStatisticsRequest;
    }
}
