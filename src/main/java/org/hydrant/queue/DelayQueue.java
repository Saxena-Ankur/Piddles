package org.hydrant.queue;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by asaxena on 2/19/17.
 */
public class DelayQueue<T extends ExpiringObject> extends LinkedList<T>{

    public static final int MAX_SIZE = 5;

    private ConcurrentHashMap<ExpiringObject, Boolean> unprocessedItems;

    private boolean underProcess;

    public DelayQueue(){
        unprocessedItems = new ConcurrentHashMap<>(5);
    }

    @Override
    public boolean offer(T t) {
        if(t == null || size() == MAX_SIZE){
            return false;
        }

        unprocessedItems.put(t,true);
        return super.offer(t);
    }

    public ConcurrentHashMap<ExpiringObject, Boolean> getUnprocessedItems() {
        return unprocessedItems;
    }

    public boolean isUnderProcess() {
        return underProcess;
    }

    public void setUnderProcess(boolean underProcess) {
        this.underProcess = underProcess;
    }
}
