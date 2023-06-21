package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Vehicle {
    protected String type;
    protected String model;
    protected String make;
    protected double highestBid;
    protected List<Bid> bids;

    public Vehicle(String type, String model, String make) {
        this.type = type;
        this.model = model;
        this.make = make;
        this.highestBid = 0;
        this.bids = new ArrayList<>();
    }

    public String getType() {
        return type;
    }

    public String getModel() {
        return model;
    }

    public String getMake() {
        return make;
    }

    public double getHighestBid() {
        return highestBid;
    }

    public void setHighestBid(double bid) {
        this.highestBid = bid;
    }

    // Method to add a bid to this vehicle
    public void addBid(Bid bid) {
        bids.add(bid);
        if (bid.getAmount() > highestBid) {
            highestBid = bid.getAmount();
        }
    }


    public List<Bid> getBids() {
        return bids;
    }

    public abstract String getName();

    public abstract int getYear();

    public abstract double getPrice();

    @Override
    public String toString() {
        return type + ": " + make + " " + model;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Vehicle other = (Vehicle) obj;
        return type.equals(other.type) && model.equals(other.model) && make.equals(other.make);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, model, make);
    }
}