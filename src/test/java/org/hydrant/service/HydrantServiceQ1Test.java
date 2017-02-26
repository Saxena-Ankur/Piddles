package org.hydrant.service;

import org.hydrant.domainobject.piddle.SingleTaskPiddle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hydrant.domainobject.piddle.IPiddle.WORK_CAPACITY_MILLS;

/**
 * Created by asaxena on 2/22/17.
 */
public class HydrantServiceQ1Test {

    private IHydrantService<Boolean,Integer> iHydrantService;

    @Before
    public void setUp(){
        SingleTaskPiddle singleTaskPiddle = new SingleTaskPiddle();
        iHydrantService = new HydrantService(singleTaskPiddle);
    }

    @Test
    public void testCanSellHydrant(){
        Assert.assertTrue("First call to canSellHydrant should be true as " +
                "piddle hasn't started working yet", iHydrantService.canSellHydrant());
    }

    @Test
    public void testSellHydrant(){
        Assert.assertTrue("First call to sellHydrant should be true as " +
                "piddle hasn't started working yet", iHydrantService.sellHydrant());
    }

    @Test
    public void testCanSellHydrantWhenPiddleIsWorking() throws InterruptedException{
        iHydrantService.canSellHydrant();
        iHydrantService.sellHydrant();

        // Making current thread sleep so that piddle start test thread can be spawned to work.
        Thread.sleep(1l);
        Assert.assertFalse("Second call to canSellHydrant should be false as " +
                "piddle is alread working", iHydrantService.canSellHydrant());
    }

    @Test
    public void testSellHydrantWhenPiddleIsWorking() throws InterruptedException{
        iHydrantService.canSellHydrant();
        iHydrantService.sellHydrant();
        Thread.sleep(1l);
        Assert.assertFalse("Second call to sellHydrant should be false as " +
        "piddle is already working", iHydrantService.sellHydrant());
    }

    @Ignore("Ignoring this test as it will take more than 5 mins to execute.")
    @Test
    public void testCanSellHydrantWhenPiddleAlreadyCompleted1Task() throws InterruptedException{
        iHydrantService.canSellHydrant();
        iHydrantService.sellHydrant();
        Thread.sleep(WORK_CAPACITY_MILLS + 1000l);
        Assert.assertTrue("First call to canSellHydrant should be true as " +
                "piddle has already completed the previous task", iHydrantService.canSellHydrant());
    }

    @Ignore("Ignoring this test as it will take more than 5 mins to execute.")
    @Test
    public void testSellHydrantWhenPiddleAlreadyCompleted1Task() throws InterruptedException{
        iHydrantService.canSellHydrant();
        iHydrantService.sellHydrant();
        Thread.sleep(WORK_CAPACITY_MILLS + 1000l);
        Assert.assertTrue("First call to sellHydrant should be true as " +
        "piddle has already completed the previous task",iHydrantService.sellHydrant());
    }
}
