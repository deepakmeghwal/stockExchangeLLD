package com.navi.stockexchange.entity;


import java.util.UUID;

public class Stock {
    private final UUID id;
    private final String stockName;

    public Stock(String stockName) {
        this.stockName = stockName;
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return stockName;
    }

    @Override
    public int hashCode() {
        return this.stockName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Stock other = (Stock) obj;
        return this.stockName.equals(other.getName());
    }
}