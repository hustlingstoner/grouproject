package com.example.auction;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuctionHouse {
    private ObservableList<Vehicle> vehicles;
    private List<Auction> auctions;
    private AuthenticationService authService;

    public AuctionHouse(AuthenticationService authService) {
        vehicles = FXCollections.observableArrayList();
        auctions = new ArrayList<>();
        this.authService = authService;
    }

    // Method to create a new auction
    public Auction createAuction(String auctionName, Date endTime) {
        if (isAdmin()) {
            Auction auction = new Auction(auctionName, endTime);
            auctions.add(auction);
            return auction;
        } else {
            System.out.println("Only admins can create auctions.");
            return null;
        }
    }

    // Method to add a vehicle to the AuctionHouse
    public void addVehicle(Vehicle vehicle) {
        if (isAdmin()) {
            vehicles.add(vehicle);
        } else {
            System.out.println("Only admins can add vehicles.");
        }
    }

    // Method to add a vehicle to an auction
    public void addVehicleToAuction(Vehicle vehicle, Auction auction) {
        if (isAdmin()) {
            auction.addVehicle(vehicle);
        } else {
            System.out.println("Only admins can add vehicles to auctions.");
        }
    }

    // Method to place a bid on a vehicle in an auction
    public void placeBidOnVehicleInAuction(AuthenticationService.User user, double bidAmount, Vehicle vehicle, Auction auction) {
        if (user != null) {
            auction.placeBid(user, bidAmount, vehicle);
        } else {
            System.out.println("You must be logged in to place a bid.");
        }
    }

    public ObservableList<Vehicle> getVehicles() {
        return vehicles;
    }

    public List<Auction> getAuctions() {
        return auctions;
    }

    private boolean isAdmin() {
        AuthenticationService.User loggedInUser = authService.getLoggedInUser();
        return loggedInUser != null && loggedInUser.getUserType() == AuthenticationService.UserType.ADMIN;
    }
}
