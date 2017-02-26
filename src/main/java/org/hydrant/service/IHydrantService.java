package org.hydrant.service;

import org.hydrant.domainobject.customer.Customer;

/**
 * Created by asaxena on 2/19/17.
 */
public interface IHydrantService<T,E> {

    /**
     * Method to support Q1,Q2,Q3. Use this method to check if
     * piddle is available to test the hydrant
     * @return
     */
    T canSellHydrant();

    /**
     * Method to support Q1,Q2,Q3. Use this method to sell Hydrant
     * to a customer
     * @return T type boolean. True if piddle accepted the request
     * to test hydrant. False if piddle rejected the request.
     **/
    T sellHydrant();

    /**
     * Method to support Q4. Use this method to sell Hydrant
     * to a customer atmost in 1 hour and 10 in 1 day.
     * It returns true or false if customer is allowed to buy
     * with the current promotion.
     * @param v - customer
     * @return T type boolean.
     **/
    <V extends Customer> T sellHydrantPromotional(V v);
    E getCurrentTaskUnderExecutionCount();
}
