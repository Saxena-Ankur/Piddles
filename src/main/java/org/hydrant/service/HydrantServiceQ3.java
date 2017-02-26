package org.hydrant.service;

import org.hydrant.domainobject.customer.Customer;
import org.hydrant.domainobject.piddle.IPiddle;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by asaxena on 2/19/17.
 */
public class HydrantServiceQ3 implements IHydrantService<Boolean, Integer>{

    private LinkedBlockingDeque<IPiddle> piddles;

    public HydrantServiceQ3(List<IPiddle> piddles){
        this.piddles = new LinkedBlockingDeque<>(piddles);
    }

    @Override
    public Boolean canSellHydrant() {
        return piddles.peek().isAvailable();
    }

    @Override
    public Boolean sellHydrant() {

        /*
            1. Check if the topmost piddle is full.
            2. If not, then remove the piddle from the Piddles queue.
            3. Add new task in the piddle work queue.
            4. Add the piddle to the ent of the piddles queue.
        */

        if(!piddles.peek().isAvailable()){
            return false;
        }

        IPiddle piddle = piddles.poll();
        boolean testStarted = piddle.startTest();

        if(!testStarted){
            return false;
        }

        piddles.addLast(piddle);

        return true;
    }


    @Override
    public Boolean sellHydrantPromotional(Customer customer) {
        customer.usePromotion();
        return sellHydrant();
    }

    @Override
    public Integer getCurrentTaskUnderExecutionCount() {
        return piddles.stream().map(p-> p.getCurrentTaskUnderExecutionCount()).filter(c -> c > 0).mapToInt(Integer::new).sum();
    }
}
