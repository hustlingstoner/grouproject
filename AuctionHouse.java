package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuctionHouse {
    private List<Auction> auctions;
    private ObservableList<Vehicle> vehicles;
    private AuthenticationService authService;

    public AuctionHouse() {
        this.auctions = new ArrayList<>();
    }

    public List<Auction> getAuctions() {
        return auctions;
    }

    public void createAuction(String auctionName, Date endTime) {
        Auction auction = new Auction(auctionName, endTime);
        auctions.add(auction);
    }

    public void addVehicleToAuction(Vehicle vehicle, Auction auction) {
        auction.addVehicle(vehicle);
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

    

    private boolean isAdmin() {
        AuthenticationService.User loggedInUser = authService.getLoggedInUser();
        return loggedInUser != null && loggedInUser.getUserType() == AuthenticationService.UserType.ADMIN;
    }

}