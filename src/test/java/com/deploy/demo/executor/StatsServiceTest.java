package com.deploy.demo.executor;

import com.deploy.demo.aws.AwsClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class StatsServiceTest {

    @Autowired
    private StatsService statsService;

    @Autowired
    private AwsClientBuilder awsClientBuilder;

    private String instanceId;


    @Test
    @Order(1)
    @DisplayName("AWS Instance metrics")
    void testAWSInstanceMetrics() {

        Assertions.assertThat(statsService.listMetrics()).isNotEmpty();

    }



}