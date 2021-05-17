package com.deploy.demo.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum WebAppStatus {
	
	IN_PROGRESS(0),
	READY(1),
    FAILED(2);

    private static final Map<Integer,WebAppStatus> lookup 
         = new HashMap<Integer, WebAppStatus>();

    static {
         for(WebAppStatus s : EnumSet.allOf(WebAppStatus.class))
              lookup.put(s.getStatus(), s);
    }

    private int status;

    private WebAppStatus(int status) {
         this.status = status;
    }

    public int getStatus() { return status; }

    public static WebAppStatus get(int status) { 
         return lookup.getOrDefault(status, WebAppStatus.READY);
    }
}
