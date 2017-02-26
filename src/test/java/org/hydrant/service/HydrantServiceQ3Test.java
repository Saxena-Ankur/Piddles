package org.hydrant.service;

import org.hydrant.domainobject.piddle.IPiddle;
import org.hydrant.domainobject.piddle.MultipleTaskPiddle;
import org.hydrant.queue.*;
import org.hydrant.queue.DelayQueue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.*;


/**
 * Created by asaxena on 2/20/17.
 */
public class HydrantServiceQ3Test {

    private IHydrantService<Boolean, Integer> iHydrantService;

    @Before
    public void setUp(){
        List<IPiddle> piddles = new LinkedList<>();
        for(int i = 0; i< 100; i++){
            MultipleTaskPiddle multipleTaskPiddle = new MultipleTaskPiddle();
            piddles.add(multipleTaskPiddle);
        }
        iHydrantService = new HydrantServiceQ3(piddles);
    }

    @Test
    public void testCanSellHydrant1Req(){
        Assert.assertTrue("First call to canSellHydrant should be true as " +
                "piddle hasn't started working yet", iHydrantService.canSellHydrant());
    }

    @Test
    public void testSellHydrantTest1Req(){
        Assert.assertTrue("1 task should be getting executed by 1 piddle", iHydrantService.sellHydrant());
    }

    @Test
    public void testCanSellHydrant2Req(){
        iHydrantService.canSellHydrant();
        iHydrantService.sellHydrant();
        iHydrantService.canSellHydrant();
        iHydrantService.sellHydrant();
        Assert.assertEquals("1 task each should be getting executed by 2 piddle2", new Integer(2), iHydrantService.getCurrentTaskUnderExecutionCount());
    }

    @Test
    public void testCanSellHydrantAllPiddlesWorking(){
        allPiddlesWorkingHelper();
        Assert.assertFalse("canSellHydrant should always return false when all the piddles are working, ", iHydrantService.canSellHydrant());

    }

    @Test
    public void testSellHydrantTestMillionRequest100Threads(){
        Callable<Boolean> callable = () -> {
            return iHydrantService.sellHydrant();
        };

        List<Future<Boolean>> futures = null;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        try{
            futures = executorService.invokeAll(Collections.nCopies(100, callable));
        } catch(InterruptedException e){
            e.printStackTrace();
        }

        Assert.assertNotNull(futures);
        futures.stream().forEach(f-> {
            try {
                 Assert.assertTrue("sellHydrant should return true for million requests", f.get());
            }catch(InterruptedException | ExecutionException e){
                e.printStackTrace();
            }
        });
        executorService.shutdown();
    }

    @Test
    public void testSellHydrantTestAllPiddlesWorking(){
        allPiddlesWorkingHelper();
        boolean isHydrantRequestSubmitted = iHydrantService.<Boolean>sellHydrant();
        Assert.assertFalse(isHydrantRequestSubmitted);
    }

    private void allPiddlesWorkingHelper() {
        List<IPiddle> piddles = new LinkedList<>();
        for(int i = 0; i< 100; i++){
            MultipleTaskPiddle multipleTaskPiddle = new MultipleTaskPiddle();
            try{
                Field f = multipleTaskPiddle.getClass().getDeclaredField("delayQueue");
                f.setAccessible(true);
                DelayQueue<ExpiringObject> delayQueue = (DelayQueue)f.get(multipleTaskPiddle);
                for(int j = 0; j< 5; j++){
                    delayQueue.add(new ExpiringObject());
                }
            }catch(NoSuchFieldException | IllegalAccessException e){
                e.printStackTrace();
            }

            piddles.add(multipleTaskPiddle);
        }
        iHydrantService = new HydrantServiceQ3(piddles);
    }
}
