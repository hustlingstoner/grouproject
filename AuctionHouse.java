package com.example.auction;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AuctionHouse {
    private ObservableList<Vehicle> vehicles;
    private int carCount;
    private int motorcycleCount;
    private int truckCount;

    public AuctionHouse() {
        vehicles = FXCollections.observableArrayList();
        carCount = 0;
        motorcycleCount = 0;
        truckCount = 0;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        incrementVehicleCount(vehicle);
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
                Bid bid = new Bid(user, bidAmount, vehicle); // Create a new Bid object
                vehicle.addBid(bid); // Add the bid to the vehicle
            } else {
                // Handle invalid bid amount (e.g., show an error message)
            }
        } else {
            // Handle vehicle not found in the auction house (e.g., show an error message)
        }
    }

    public String getVehicleDetails(Vehicle vehicle) {
        StringBuilder details = new StringBuilder();
        details.append("Vehicle: ").append(vehicle.getName()).append("\n");
        details.append("Type: ").append(vehicle.getType()).append("\n");
        details.append("Current Highest Bid: $").append(vehicle.getHighestBid()).append("\n");

        // Display the list of users who have placed bids along with the bid amounts
        details.append("Bids:\n");
        for (Bid bid : vehicle.getBids()) {
            details.append("User: ").append(bid.getUser().getUsername())
                    .append(", Amount: $").append(bid.getAmount()).append("\n");
        }

        return details.toString();
    }
}