package com.deploy.demo.enums;


import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum MetricGraphTypes {

	CPU_UTILIZATION(0,"CPUUtilization"), DISK_READ_OPS(1,"DiskReadOps"), DISK_WRITE_OPS(2,"DiskWriteOps"),
	DISK_READ_BYTES(3,"DiskReadBytes"),DISK_WRITE_BYTES(4,"DiskWriteBytes"),NETWORK_IN(5,"NetworkIn"),
	NETWORK_OUT(6,"NetworkOut"),NETWORK_PACKETS_IN(7,"NetworkPacketsIn"),NETWORK_PACKETS_OUT(8,"NetworkPacketsOut"),
	STATUS_CHECK_FAILED(9,"StatusCheckFailed"),STATUS_CHECK_FAILED_INSTANCE(10,"StatusCheckFailed_Instance"),STATUS_CHECK_FAILED_SYSTEM(11,"StatusCheckFailed_System");

	private static final Map<Integer, MetricGraphTypes> lookup = new HashMap<Integer, MetricGraphTypes>();

	static {
		for (MetricGraphTypes s : EnumSet.allOf(MetricGraphTypes.class))
			lookup.put(s.getGraphType(), s);
	}

	private int graphType;
	private String awsValue;
	

	private MetricGraphTypes(int graphType, String awsValue) {
		this.graphType = graphType;
		this.awsValue = awsValue;
		
	}

	public int getGraphType() {
		return graphType;
	}
	
	public String getAwsValue() {
		return awsValue;
	}	
	

	public static MetricGraphTypes get(int graphType) {
		return lookup.get(graphType);
	}
}
