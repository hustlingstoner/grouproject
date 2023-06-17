package com.example.auction;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Auction {
    private List<Vehicle> vehicles;
    private double currentHighestBid;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<Bid> bids;
    private boolean isOpen;
    private Bid winningBid;

    public interface AuctionEndListener {
        void onAuctionEnd(Bid winningBid);
    }

    private AuctionEndListener auctionEndListener;

    public Auction(List<Vehicle> vehicles, LocalDateTime startTime, LocalDateTime endTime) {
        this.vehicles = vehicles;
        this.startTime = startTime;
        this.endTime = endTime;
        this.bids = new ArrayList<>();
        this.isOpen = false;
        scheduleAuctionStart();
        scheduleAuctionEnd();
    }

    public void placeBid(Bid bid) {
        if (isOpen && bid.getAmount() > currentHighestBid) {
            bids.add(bid);
            currentHighestBid = bid.getAmount();
        }
    }

    private void scheduleAuctionStart() {
        Timer timer = new Timer();
        Date startDate = Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant());
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                isOpen = true;
            }
        }, startDate);
    }

    private void scheduleAuctionEnd() {
        Timer timer = new Timer();
        Date endDate = Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant());
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                isOpen = false;
                determineWinner();
                if (auctionEndListener != null) {
                    auctionEndListener.onAuctionEnd(winningBid);
                }
            }
        }, endDate);
    }

    private void determineWinner() {
        double maxAmount = 0;
        for (Bid bid : bids) {
            if (bid.getAmount() > maxAmount) {
                maxAmount = bid.getAmount();
                winningBid = bid;
            }
        }
    }

    public void setAuctionEndListener(AuctionEndListener auctionEndListener) {
        this.auctionEndListener = auctionEndListener;
    }


    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public double getCurrentHighestBid() {
        return currentHighestBid;
    }

    public void setCurrentHighestBid(double currentHighestBid) {
        this.currentHighestBid = currentHighestBid;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public Bid getWinningBid() {
        return winningBid;
    }

    public void setWinningBid(Bid winningBid) {
        this.winningBid = winningBid;
    }

    public AuctionEndListener getAuctionEndListener() {
        return auctionEndListener;
    }
}

