package com.example.demo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Auction {
    private String auctionName;
    private Date endTime;
    private List<Vehicle> vehicles;

    public Auction(String auctionName, Date endTime) {
        this.auctionName = auctionName;
        this.endTime = endTime;
        this.vehicles = new ArrayList<>();
    }

    public String getAuctionName() {
        return auctionName;
    }

    public Date getEndTime() {
        return endTime;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    @Override
    public String toString() {
        return auctionName;
    }

    public void placeBid(AuthenticationService.User user, double bidAmount, Vehicle vehicle) {
        // Check if the bid amount is higher than the current highest bid
        if (bidAmount > vehicle.getHighestBid()) {
            // Create a new Bid object
            Bid bid = new Bid(user, bidAmount, vehicle);

            // Add the bid to the vehicle
            vehicle.addBid(bid);

            // Optionally, you can add additional logic here, such as notifying other bidders
        } else {
            System.out.println("Your bid must be higher than the current highest bid.");
        }
    }
}
