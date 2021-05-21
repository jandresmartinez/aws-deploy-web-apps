package com.deploy.demo.enums;


import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum MetricPeriodTypes {

	FIVE_MINUTES(300),FIFTEEN_MINUTES(900),ONE_HOUR(3600),SIX_HOURS(21600),ONE_DAY(86400);

	private static final Map<Integer, MetricPeriodTypes> lookup = new HashMap<Integer, MetricPeriodTypes>();

	static {
		for (MetricPeriodTypes s : EnumSet.allOf(MetricPeriodTypes.class))
			lookup.put(s.getMetricRangeType(), s);
	}

	private int metricPeriodRangeType;

	private MetricPeriodTypes(int metricPeriodRangeType) {
		this.metricPeriodRangeType = metricPeriodRangeType;
	}

	public int getMetricRangeType() {
		return metricPeriodRangeType;
	}

	public static MetricPeriodTypes get(int metricPeriodRangeType) {
		return lookup.get(metricPeriodRangeType);
	}
}
