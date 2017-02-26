package org.hydrant.service;


import org.hydrant.domainobject.customer.Customer;
import org.hydrant.domainobject.piddle.IPiddle;


/**
 * Created by asaxena on 2/19/17.
 */
public class HydrantService implements IHydrantService<Boolean, Integer> {

    private IPiddle piddle;

    public HydrantService(IPiddle piddle){
        this.piddle = piddle;
    }

    @Override
    public Boolean canSellHydrant(){
        return piddle.isAvailable();
    }

    @Override
    public Boolean sellHydrant(){
        if(!piddle.isAvailable()){
            return false;
        }

        return piddle.startTest();
    }

    @Override
    public Integer getCurrentTaskUnderExecutionCount() {
        return piddle.getCurrentTaskUnderExecutionCount();
    }

    @Override
    public Boolean sellHydrantPromotional(Customer customer) {
        throw new UnsupportedOperationException("Promotions are not supported");
    }
}
