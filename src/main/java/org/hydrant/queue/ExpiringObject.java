package org.hydrant.queue;

/**
 * Created by asaxena on 2/19/17.
 */
public class ExpiringObject {
    public static final long EVICT_TIME_MILLS = 60 * 60 * 1000;

    private long evictionTime;

    public ExpiringObject(){
        evictionTime = System.currentTimeMillis() + EVICT_TIME_MILLS;
    }

    public boolean canEvict(){
        return evictionTime < System.currentTimeMillis();
    }
}
