package com.deploy.demo.executor;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum LaunchInstanceStatus {

    PROCESSING(0),
    DONE(1),
    CANCELLED(2),
    ERROR(3),
    NOT_FOUND(-1);

    private static final Map<Integer,LaunchInstanceStatus> lookup = new HashMap<>();

    static {
        for(LaunchInstanceStatus s : EnumSet.allOf(LaunchInstanceStatus.class))
            lookup.put(s.getValue(), s);
    }

    private final int value;

    LaunchInstanceStatus(int value) {
        this.value = value;
    }

    public int getValue() { return value; }

    public static LaunchInstanceStatus get(int value) {
        return lookup.getOrDefault(value, LaunchInstanceStatus.NOT_FOUND);
    }
}