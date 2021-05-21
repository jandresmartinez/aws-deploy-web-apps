package com.deploy.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class CloudWatchMetric {

	public CloudWatchMetric(String metricName, String statistic, List<Double> value) {
		super();
		this.metricName = metricName;
		this.statistic = statistic;
		this.value = value;
	}
	private String metricName;
	private String statistic;
	private List<Double> value;
	

	

}
