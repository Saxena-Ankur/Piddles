package org.hydrant.domainobject.piddle;

/**
 * This class is used for Q1 where piddle can only perform one task.
 * @author asaxena
 */
public class SingleTaskPiddle implements IPiddle {

    private boolean isAvailable;
    private long timeToCompleteInMills;
    private int currentTaskUnderExecutionCount;

    public SingleTaskPiddle(){
        isAvailable = true;
    }
    @Override
    public boolean isAvailable() {
        return isAvailable;
    }

    protected void setAvailable(boolean available) {
        isAvailable = available;
    }

    protected long getTimeToCompleteInMills() {
        return timeToCompleteInMills;
    }

    protected void setTimeToCompleteInMills(long timeToCompleteInMills) {
        this.timeToCompleteInMills = timeToCompleteInMills;
    }

    @Override
    public int getCurrentTaskUnderExecutionCount() {
        return currentTaskUnderExecutionCount;
    }

    @Override
    public synchronized boolean startTest() {
        if(!isAvailable()){
            return false;
        }

        Thread testThread = new Thread() {
            public void run() {
                currentTaskUnderExecutionCount++;
                setAvailable(false);
                setTimeToCompleteInMills(System.currentTimeMillis() + WORK_CAPACITY_MILLS);
                while(System.currentTimeMillis() < getTimeToCompleteInMills());
                setAvailable(true);
                --currentTaskUnderExecutionCount;
            }
        };

        testThread.start();
        return true;
    }
}
