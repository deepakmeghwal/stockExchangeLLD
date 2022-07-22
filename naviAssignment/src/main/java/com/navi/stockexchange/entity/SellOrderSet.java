package com.navi.stockexchange.entity;

import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

final class SellOrderComparator implements Comparator<Order> {
    @Override
    public int compare(Order a, Order b) {
        // invalid orders cases
        if (a.getId().equals(b.getId())) {
            return 0;
        }

        int compareTime = a.getTime().compareTo(b.getTime());

        if(compareTime != 0){
            //if time are not same
            return compareTime;
        }

        //if time are same, then compare the price
        return a.getPrice().compareTo(b.getPrice());
    }
}

/**
 * SellOrderSet is a set of {@link Order} of orderType SELL, sorted by time and price.
 */
public class SellOrderSet {
    private SortedSet<Order> orderSet;

    public SellOrderSet() {
        Comparator<Order> comparator = new SellOrderComparator();
        this.orderSet = new TreeSet<>(comparator);
    }

    public Set<Order> getOrderSet() {
        return orderSet;
    }
}


