package com.deploy.demo.enums;


import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum MetricStatisticsTypes {

	MINIMUM(0,"Minimum"), MAXIMUM(1,"Maximum"), SUM(2,"Sum"),
	AVERAGE(3,"Average"),SAMPLE_COUNT(4,"SampleCount");

	private static final Map<Integer, MetricStatisticsTypes> lookup = new HashMap<Integer, MetricStatisticsTypes>();

	static {
		for (MetricStatisticsTypes s : EnumSet.allOf(MetricStatisticsTypes.class))
			lookup.put(s.getStatisticsType(), s);
	}

	private int statisticType;
	private String awsValue;
	

	private MetricStatisticsTypes(int statisticType, String awsValue) {
		this.statisticType = statisticType;
		this.awsValue = awsValue;
		
	}

	public int getStatisticsType() {
		return statisticType;
	}
	
	public String getAwsValue() {
		return awsValue;
	}	
	

	public static MetricStatisticsTypes get(int statisticType) {
		return lookup.get(statisticType);
	}
}
