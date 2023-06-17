package com.example.auction;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.stream.Collectors;

public class BidDialog extends Dialog<Double> {

    private final TextField bidAmountField;
    private final Label lastBidLabel;
    private final ListView<String> bidListView;

    public BidDialog(Vehicle vehicle, AuctionHouse auctionHouse, AuthenticationService.User user) {
        setTitle("Place Bid");
        setHeaderText("Place your bid for " + vehicle.toString()); // Modified line

        ButtonType bidButtonType = new ButtonType("Bid", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(bidButtonType, ButtonType.CANCEL);

        bidAmountField = new TextField();
        bidAmountField.setPromptText("Bid Amount");

        lastBidLabel = new Label("Last Bid: $" + vehicle.getHighestBid());

        ObservableList<String> bidItems = FXCollections.observableArrayList(
                vehicle.getBids().stream()
                        .map(bid -> bid.getUser().getUsername() + " bid $" + bid.getAmount())
                        .collect(Collectors.toList())
        );
        bidListView = new ListView<>();
        bidListView.setItems(bidItems);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));
        gridPane.add(new Label("Bid Amount:"), 0, 0);
        gridPane.add(bidAmountField, 1, 0);
        gridPane.add(lastBidLabel, 0, 1);

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(gridPane, bidListView);

        getDialogPane().setContent(vBox);

        setResultConverter(dialogButton -> {
            if (dialogButton == bidButtonType) {
                try {
                    double bidAmount = Double.parseDouble(bidAmountField.getText());
                    auctionHouse.placeBid(vehicle, bidAmount, user);
                    return bidAmount;
                } catch (NumberFormatException e) {

                }
            }
            return null;
        });
    }

    public double getBidAmount() {
        String input = bidAmountField.getText();
        if (input.isEmpty())
            return 0;
        else
            return Double.parseDouble(input);
    }

    public void updateLastBid(double amount) {
        lastBidLabel.setText("Last Bid: $" + amount);
    }
}
