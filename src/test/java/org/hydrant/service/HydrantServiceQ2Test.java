package org.hydrant.service;

import org.hydrant.domainobject.piddle.MultipleTaskPiddle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hydrant.queue.ExpiringObject.EVICT_TIME_MILLS;

/**
 * Created by asaxena on 2/22/17.
 */
public class HydrantServiceQ2Test {

    private IHydrantService<Boolean, Integer> iHydrantService;

    @Before
    public void setUp(){
        MultipleTaskPiddle multipleTaskPiddle = new MultipleTaskPiddle();
        iHydrantService = new HydrantService(multipleTaskPiddle);
    }

    @Test
    public void testCanSellHydrant1Req() throws InterruptedException{
        Assert.assertTrue("First call to canSellHydrant should be true as " +
                "piddle hasn't started working yet",iHydrantService.canSellHydrant());
    }

    @Test
    public void testCanSellHydrant2Req() throws InterruptedException{
        iHydrantService.canSellHydrant();
        sellHydrantHelper();
        Assert.assertTrue("Second request after sellHydrant to canSellHydrant should be true for multi task piddle", iHydrantService.canSellHydrant());
    }

    @Test
    public void testSellHydrant2Req() throws InterruptedException{
        sellHydrantHelper();
        sellHydrantHelper();
        Assert.assertEquals("Should be 1 task each executing by 1 piddle", new Integer(1), iHydrantService.getCurrentTaskUnderExecutionCount());
    }

    @Test
    public void testCanSellHydrantAfter5ReqIn1Hour() throws InterruptedException{
        for(int i = 0; i< 5; i++){
            sellHydrantHelper();
        }

        Assert.assertFalse("Multi task piddle can only work on 5 tasks in an hour", iHydrantService.canSellHydrant());
    }

    @Test
    public void testSellHydrantAfter5ReqIn1Hour() throws InterruptedException{
        for(int i = 0; i< 5; i++){
            sellHydrantHelper();
        }
        Assert.assertFalse("Multi task piddle can only work on 5 tasks in an hour", iHydrantService.sellHydrant());
    }

    @Ignore("This test will take 1 hour to finish.")
    @Test
    public void testCanSellHydrantAfter1Hour() throws InterruptedException{
        for(int i = 0; i< 5; i++){
            sellHydrantHelper();
        }
        Thread.sleep(EVICT_TIME_MILLS + 2000);
        Assert.assertTrue("Multi task piddle can work on new tasks after hour", iHydrantService.canSellHydrant());
    }

    @Ignore("This test will take 1 hour to finish.")
    @Test
    public void testSellHydrantAfter1Hour() throws InterruptedException{
        for(int i = 0; i< 5; i++){
            sellHydrantHelper();
        }
        Thread.sleep(EVICT_TIME_MILLS + 2000);
        Assert.assertTrue("Multi task piddle can work on new tasks after hour", iHydrantService.sellHydrant());
    }

    private void sellHydrantHelper() throws InterruptedException {
        Assert.assertTrue(iHydrantService.sellHydrant());
        Thread.sleep(1l);
    }
}

