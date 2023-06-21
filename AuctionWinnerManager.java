package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class AuctionWinnerManager {
    private List<AuthenticationService.User> auctionWinners;

    public AuctionWinnerManager() {
        auctionWinners = new ArrayList<>();
    }

    public void addAuctionWinner(AuthenticationService.User user) {
        auctionWinners.add(user);
    }

    public List<AuthenticationService.User> getAuctionWinners() {
        return auctionWinners;
    }
}
