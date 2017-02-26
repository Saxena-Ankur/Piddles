package org.hydrant.domainobject.piddle;

import org.hydrant.queue.DelayQueue;
import org.hydrant.queue.ExpiringObject;
import org.hydrant.service.ThreadExecutorService;

import java.util.Iterator;
import java.util.Map;

 /**
  * This class is used for Q2,Q3 where piddle can perform multiple tasks in an hour.
  * This class uses {@link DelayQueue} to store the tasks that piddle has to execute.
  * @author asaxena
  */
public class MultipleTaskPiddle extends SingleTaskPiddle {

    private DelayQueue<ExpiringObject> delayQueue;
    private int currentTaskUnderExecutionCount;

    public MultipleTaskPiddle(){
        delayQueue = new DelayQueue<>();
    }

    @Override
    public boolean isAvailable() {
        return delayQueue.size() < DelayQueue.MAX_SIZE || canEvict();
    }

    @Override
    public int getCurrentTaskUnderExecutionCount() {
        return currentTaskUnderExecutionCount;
    }

    @Override
    public synchronized boolean startTest() {

        // On each request to start the thread check if first object in the queue is expired.
        // if object is expired, evict the object from the queue.
        if(canEvict()){
            evict();
        }

        // Creates a expiring object that will expires after 1 hour of creation.
        ExpiringObject expiringObject = new ExpiringObject();

        // add the expiringObject to a delayQueue.
        // If delayQueue is full that test is not accepted else task is added to a queue
        boolean isAcceptedTest = delayQueue.offer(expiringObject);

        if(!isAcceptedTest){
            return false;
        }

        Runnable runnable = null;

        // if a thread is currently executing task from the queue then do not spawn new thread and use the same thread to execute all threads.
        // Using same thread ensures that piddle just executes one test at a time.
        if(!delayQueue.isUnderProcess()){
            runnable = new Runnable() {
                public void run() {
                    for(Iterator<Map.Entry<ExpiringObject,Boolean>> it = delayQueue.getUnprocessedItems().entrySet().iterator(); it.hasNext(); ) {
                        delayQueue.setUnderProcess(true);
                        currentTaskUnderExecutionCount++;
                        long currentTime = System.currentTimeMillis();
                        setTimeToCompleteInMills(currentTime + WORK_CAPACITY_MILLS);
                        while(System.currentTimeMillis() < getTimeToCompleteInMills());
                        --currentTaskUnderExecutionCount;
                        it.remove();
                    }

                    delayQueue.setUnderProcess(false);
                }
            };
        }

        if(runnable != null){
            ThreadExecutorService.executeThread(runnable);
        }

        return true;
    }

    /**
     * Method is used to check if any object is expired to evict from the delay queue.
     * @return boolean
     */
    public boolean canEvict() {
        if(delayQueue.size() > 0){
            return delayQueue.peek().canEvict();
        }
        return false;
    }

    private void evict(){
        if(delayQueue.size() > 0){
            delayQueue.poll();
        }
    }
}





