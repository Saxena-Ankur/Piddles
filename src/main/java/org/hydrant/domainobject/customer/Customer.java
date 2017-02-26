package org.hydrant.domainobject.customer;

import java.time.LocalDateTime;

/**
 * Customer class to support Q4.
 * @author asaxena
 */
public class Customer {

    private static final int MAX_ORDERS_IN_A_DAY = 10;
    private static final int MAX_ORDERS_IN_AN_HOUR = 1;

    long id;
    String fName;
    String lName;
    LocalDateTime todaysFirstOrderDateTime;
    int ordersInADayCount;
    Long recentOrderDateTime;

    public Customer(long id, String fName, String lName){
        this.id = id;
        this.fName = fName;
        this.lName = lName;
    }

    /**
     * This method is used to validate if the customer is allowed to buy hydrant with current promotion.
     * @throws RuntimeException if order placed more than once in an hour or more that 10 in a day.
     */
    public void usePromotion(){
        LocalDateTime newOrderDateTime = LocalDateTime.now();

        if(todaysFirstOrderDateTime == null || recentOrderDateTime == null){
            todaysFirstOrderDateTime = newOrderDateTime;
            recentOrderDateTime = System.currentTimeMillis();
            ordersInADayCount = 1;
            return;
        }

        if(ordersInADayCount == 10 && todaysFirstOrderDateTime.getDayOfMonth() == newOrderDateTime.getDayOfMonth()){
                throw new RuntimeException("Only " + MAX_ORDERS_IN_A_DAY + " in a day");
        }

        if(System.currentTimeMillis() - recentOrderDateTime <= 3600){
            throw new RuntimeException("Only " + MAX_ORDERS_IN_AN_HOUR + " in an hour");
        }

        if(newOrderDateTime.getDayOfMonth() < newOrderDateTime.getDayOfMonth()){
            ordersInADayCount = 1;
            todaysFirstOrderDateTime = newOrderDateTime;
        } else {
            ordersInADayCount++;
        }

        if(System.currentTimeMillis() - recentOrderDateTime > 3600){
            recentOrderDateTime = System.currentTimeMillis();
        }
    }
}

