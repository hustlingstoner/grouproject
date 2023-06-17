package com.example.auction;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AuctionHouse {
    private ObservableList<Vehicle> vehicles;
    private int carCount;
    private int motorcycleCount;
    private int truckCount;
    private AuthenticationService authService; // Added AuthenticationService field

    public AuctionHouse(AuthenticationService authService) { // Modified constructor to accept AuthenticationService
        vehicles = FXCollections.observableArrayList();
        carCount = 0;
        motorcycleCount = 0;
        truckCount = 0;
        this.authService = authService; // Initialize AuthenticationService field
    }

    public void addVehicle(Vehicle vehicle) {
        // Check if the logged-in user is an admin
        if (authService.getLoggedInUser() != null && authService.getLoggedInUser().getUserType() == AuthenticationService.UserType.ADMIN) {
            vehicles.add(vehicle);
            incrementVehicleCount(vehicle);
        } else {
            System.out.println("Only admins can add vehicles.");
        }
    }

    private void incrementVehicleCount(Vehicle vehicle) {
        if (vehicle instanceof Car) {
            carCount++;
        } else if (vehicle instanceof Motorcycle) {
            motorcycleCount++;
        } else if (vehicle instanceof Truck) {
            truckCount++;
        }
    }

    public ObservableList<Vehicle> getVehicles() {
        return vehicles;
    }

    public int getCarCount() {
        return carCount;
    }

    public int getMotorcycleCount() {
        return motorcycleCount;
    }

    public int getTruckCount() {
        return truckCount;
    }

    public void placeBid(Vehicle vehicle, double bidAmount, AuthenticationService.User user) { // Added user parameter
        if (vehicles.contains(vehicle)) {
            double currentBid = vehicle.getHighestBid();
            if (bidAmount > currentBid) {
                vehicle.setHighestBid(bidAmount);
                Bid bid = new Bid(user, bidAmount, vehicle);
                vehicle.addBid(bid);
            } else {
                // Error Msg Help ME!
            }
        } else {
            // Error MSG HELP ME!
        }
    }

    public String getVehicleDetails(Vehicle vehicle) {
        StringBuilder details = new StringBuilder();
        details.append("Vehicle: ").append(vehicle.getName()).append("\n");
        details.append("Type: ").append(vehicle.getType()).append("\n");
        details.append("Current Highest Bid: $").append(vehicle.getHighestBid()).append("\n");


        details.append("Bids:\n");
        for (Bid bid : vehicle.getBids()) {
            details.append("User: ").append(bid.getUser().getUsername())
                    .append(", Amount: $").append(bid.getAmount()).append("\n");
        }

        return details.toString();
    }
}