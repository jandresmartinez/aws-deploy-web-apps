package com.deploy.demo.aws;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.retry.PredefinedRetryPolicies;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancing;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancingClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class AwsClientBuilder {




    @Value("${amazon.credentials-access-key-id}")
    private String credentialsAccessKeyId;
    @Value("${amazon.credentials-secret-access-key}")
    private String credentialsSecretAccessKey ;

    // Configuration properties
    @Value("${amazon.configuration-region}")
    private String configurationRegion ;

    private static final String ERROR_MESSAGE="Cannot load the credentials from the credential profiles file. " +
            "Please make sure that your credentials file is at the correct " +
            "location, and is in valid format.";



    public AmazonS3 getAmazonS3Client() {
        BasicAWSCredentials credentials = null;
        try {
            credentials = new BasicAWSCredentials(credentialsAccessKeyId, credentialsSecretAccessKey);
        } catch (Exception e) {
            throw new AmazonClientException(
                    ERROR_MESSAGE, e
            );
        }

        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setRetryPolicy(PredefinedRetryPolicies.getDefaultRetryPolicyWithCustomMaxRetries(5));



        return AmazonS3ClientBuilder
                .standard()
                .withClientConfiguration(clientConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(configurationRegion)
                .build();
    }

    public AmazonEC2 getAmazonEC2Client() throws AmazonClientException {
        BasicAWSCredentials credentials = null;
        try {
            credentials = new BasicAWSCredentials(credentialsAccessKeyId, credentialsSecretAccessKey);
        } catch (Exception e) {
            throw new AmazonClientException(
                    ERROR_MESSAGE, e
            );
        }


        return AmazonEC2ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(configurationRegion)
                .build();
    }

    public AmazonElasticLoadBalancing getAmazonElbClient() throws AmazonClientException {
        BasicAWSCredentials credentials = null;
        try {
            credentials = new BasicAWSCredentials(credentialsAccessKeyId, credentialsSecretAccessKey);
        } catch (Exception e) {
            throw new AmazonClientException(
                    ERROR_MESSAGE, e
            );
        }


        return AmazonElasticLoadBalancingClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(configurationRegion)
                .build();
    }

    public AmazonCloudWatch getAmazonCloudWatchBuilder() throws AmazonClientException {
        BasicAWSCredentials credentials = null;
        try {
            credentials = new BasicAWSCredentials(credentialsAccessKeyId, credentialsSecretAccessKey);
        } catch (Exception e) {
            throw new AmazonClientException(
                    ERROR_MESSAGE, e
            );
        }


        return AmazonCloudWatchClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(configurationRegion)
                .build();
    }



}