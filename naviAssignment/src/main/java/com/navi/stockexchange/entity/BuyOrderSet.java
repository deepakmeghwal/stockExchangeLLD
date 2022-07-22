package com.navi.stockexchange.entity;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Comparator for buy Order
 * @Order
 */
final class BuyOrderComparator implements Comparator<Order> {
    @Override
    public int compare(Order a, Order b) {
        // invalid orders case
        if (a.getId().equals(b.getId())) {
            return 0;
        }

        int compareTime = a.getTime().compareTo(b.getTime());
        if (compareTime == 0) {
            // if time are same, then compare the Ids
            return a.getId().compareTo(b.getId());
        }
        return compareTime;
    }
}

/**
 * BuyOrderSet is contains the order are having type as BUY
 * {@Order}
 */
public class BuyOrderSet {
    private Set<Order> orderSet;

    public BuyOrderSet() {
        Comparator<Order> comparator = new BuyOrderComparator();
        this.orderSet = new TreeSet<>(comparator);
    }

    public Set<Order> getOrderSet() {
        return orderSet;
    }
}

