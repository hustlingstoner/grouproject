package com.example.auction;
public class Bid {
    private AuthenticationService.User user;
    private double amount;
    private Vehicle vehicle;

    public Bid(AuthenticationService.User user, double amount, Vehicle vehicle) {
        this.user = user;
        this.amount = amount;
        this.vehicle = vehicle;
    }

    public AuthenticationService.User getUser() {
        return user;
    }

    public double getAmount() {
        return amount;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}


