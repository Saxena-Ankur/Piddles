package org.hydrant.service;

import org.hydrant.domainobject.customer.Customer;
import org.hydrant.domainobject.piddle.IPiddle;
import org.hydrant.domainobject.piddle.MultipleTaskPiddle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by asaxena on 2/25/17.
 */
public class HydrantServiceQ4Test {

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
    public void testSellHydrantPromotional(){
        Customer customer = new Customer(1, "FName", "LName");
        Assert.assertTrue("Customer should be able to use the promotion", iHydrantService.sellHydrantPromotional(customer));
    }

    @Test(expected = RuntimeException.class)
    public void testSell2HydrantPromotionalToSameCustomerIn1Hour(){
        Customer customer = new Customer(1, "FName", "LName");
        iHydrantService.sellHydrantPromotional(customer);
        iHydrantService.sellHydrantPromotional(customer);
    }

    @Test(expected = RuntimeException.class)
    public void testSell11HydrantPromotionalToSameCustomerIn1Day() throws InterruptedException{
        Customer customer = new Customer(1, "FName", "LName");

        for(int i =0; i< 10; i++){
            sellHydrantHelper(customer);
        }

        try{
            sellHydrantHelper(customer);
        }catch (RuntimeException e){
            throw new RuntimeException("Only 10 hydrant can be sold in 1 day");
        }
    }

    private void sellHydrantHelper(Customer customer) throws InterruptedException {
        this.incrementTimeBy1Hour(customer);
        iHydrantService.sellHydrantPromotional(customer);
        Thread.sleep(1l);
    }

    private void incrementTimeBy1Hour(Customer c){
        try{
            Field f = c.getClass().getDeclaredField("recentOrderDateTime");
            f.setAccessible(true);
            f.set(c, -1l);
        } catch(NoSuchFieldException | IllegalAccessException nsfe){
                nsfe.printStackTrace();
        }

    }

}
