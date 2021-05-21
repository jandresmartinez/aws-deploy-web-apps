package com.deploy.demo.controller;


import com.deploy.demo.domain.WebApplication;
import com.deploy.demo.dto.CloudWatchMetric;
import com.deploy.demo.enums.MetricPeriodTypes;
import com.deploy.demo.enums.MetricStatisticsTypes;
import com.deploy.demo.exceptions.ObjectNotFoundException;
import com.deploy.demo.executor.StatsService;
import com.deploy.demo.service.WebApplicationService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/web-apps-stats")
public class WebApplicationStatsController {


    private WebApplicationService webApplicationService;
    private StatsService statsService;

    final long fifteenMin = 1000 * MetricPeriodTypes.FIFTEEN_MINUTES.getMetricRangeType();
    int period = MetricPeriodTypes.FIVE_MINUTES.getMetricRangeType();
    Collection< String > statistics = new ArrayList< >();


    @Autowired
    public WebApplicationStatsController(WebApplicationService webApplicationService, StatsService statsService) {
        this.webApplicationService = webApplicationService;
        this.statsService = statsService;
    }

    @ApiOperation(value = "Get web app stats", nickname ="getStats", tags = { "Web App Stats" })
    @GetMapping(path = "/cpu-stats/{id}")
    public @ResponseBody
    List<CloudWatchMetric> getWebAppCPUStats(@PathVariable Long id ) {
        statistics.add(MetricStatisticsTypes.AVERAGE.getAwsValue());
        WebApplication webApplication = webApplicationService.findOne(id);
        if (webApplication==null)
            throw new ObjectNotFoundException("Web Application with id:"+id+ " not found.");
        return statsService.getMetricResult(fifteenMin,period,statistics,webApplication.getInstanceId());
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<String> onObjectNotFoundException(ObjectNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("%s - %s",
                HttpStatus.NOT_FOUND, ex.getMessage()));
    }

}
