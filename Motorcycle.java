package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Motorcycle extends Vehicle {

    private String name;
    private int year;
    private double price;

    public Motorcycle(String type, String model, String make, String name, int year, double price) {
        super(type, model, make);
        this.name = name;
        this.year = year;
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public List<Bid> getBids() {
        return bids;
    }
}
