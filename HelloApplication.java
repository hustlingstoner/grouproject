package com.example.auction;
import java.util.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    private AuctionHouse auctionHouse;
    private ListView<Vehicle> vehicleListView;
    private AuthenticationService authService;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        authService = new AuthenticationService();
        auctionHouse = new AuctionHouse(authService); // Pass authService as an argument
        vehicleListView = new ListView<>();

        primaryStage.setTitle("Auction System");

        VBox authBox = createAuthPage();
        primaryStage.setScene(new Scene(authBox, 400, 400));
        primaryStage.show();
    }

    private VBox createAuthPage() {
        Button userButton = new Button("User Login");
        userButton.setOnAction(e -> showUserDetailsPage());

        Button adminButton = new Button("Admin Login");
        adminButton.setOnAction(e -> showAdminLoginPage());

        VBox authBox = new VBox(10);
        authBox.getChildren().addAll(userButton, adminButton);
        authBox.setSpacing(20);
        authBox.setPadding(new Insets(10));

        return authBox;
    }

    private void showUserDetailsPage() {
        Stage userDetailsStage = new Stage();
        userDetailsStage.setTitle("User Details");

        Label nameLabel = new Label("Username:");
        TextField nameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button saveButton = new Button("Log in");

        saveButton.setOnAction(e -> {
            String username = nameField.getText();
            String password = passwordField.getText();
            boolean validUser = authService.authenticateUser(username, password);

            if (validUser) {
                userDetailsStage.close();
                showVehiclesForAuction();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid user credentials.");
                alert.showAndWait();
            }
        });

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(nameLabel, nameField, passwordLabel, passwordField, saveButton);

        userDetailsStage.setScene(new Scene(root));
        userDetailsStage.show();
    }

    private void showVehiclesForAuction() {
        Stage vehiclesStage = new Stage();
        vehiclesStage.setTitle("Vehicles for Auction");

        ListView<Vehicle> listView = new ListView<>();
        listView.setItems(auctionHouse.getVehicles());

        Button viewDetailsButton = new Button("View Details");
        viewDetailsButton.setOnAction(e -> {
            Vehicle selectedVehicle = listView.getSelectionModel().getSelectedItem();
            if (selectedVehicle != null) {
                showVehicleDetails(selectedVehicle);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Vehicle Selected");
                alert.setHeaderText(null);
                alert.setContentText("Please select a vehicle to view details.");
                alert.showAndWait();
            }
        });

        Button placeBidButton = new Button("Place Bid");
        placeBidButton.setOnAction(e -> {
            Vehicle selectedVehicle = listView.getSelectionModel().getSelectedItem();
            if (selectedVehicle != null) {
                showBidDialog(selectedVehicle);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Vehicle Selected");
                alert.setHeaderText(null);
                alert.setContentText("Please select a vehicle to place a bid.");
                alert.showAndWait();
            }
        });

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(listView, viewDetailsButton, placeBidButton);

        vehiclesStage.setScene(new Scene(root));
        vehiclesStage.show();
    }

    private void showVehicleDetails(Vehicle vehicle) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Vehicle Details");
        alert.setHeaderText(vehicle.getName());
        String vehicleDetails = auctionHouse.getVehicleDetails(vehicle);
        alert.setContentText(vehicleDetails);
        alert.showAndWait();
    }

    private void showBidDialog(Vehicle vehicle) {
        AuthenticationService.User currentUser = authService.getLoggedInUser();
        BidDialog dialog = new BidDialog(vehicle, auctionHouse, currentUser);
        dialog.setTitle("Place Bid");
        dialog.setHeaderText("Place your bid for " + vehicle.getName());

        dialog.showAndWait().ifPresent(bidAmount -> {
            auctionHouse.placeBid(vehicle, bidAmount, currentUser);
            String vehicleDetails = auctionHouse.getVehicleDetails(vehicle);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Bid Placed");
            alert.setHeaderText(null);
            alert.setContentText("Your bid of $" + bidAmount + " has been placed.\n\n" + vehicleDetails);
            alert.showAndWait();
        });
    }



    private void showAdminPage() {
        Stage adminStage = new Stage();
        adminStage.setTitle("Admin Panel");

        Button addVehicleButton = new Button("Add Vehicle");
        addVehicleButton.setOnAction(e -> {
            showAddVehicleDialog();
        });

        Button createAuctionButton = new Button("Create Auction"); // New button for creating auctions
        createAuctionButton.setOnAction(e -> {
            showCreateAuctionDialog();
        });

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(addVehicleButton, createAuctionButton); // Add the new button to the layout

        adminStage.setScene(new Scene(root));
        adminStage.show();
    }

    // Method to show a dialog for creating an auction
    private void showCreateAuctionDialog() {
        // You can create a custom dialog similar to AddVehicleDialog
        // For simplicity, I'm just using a hardcoded date for the auction end time
        Date endTime = new Date(System.currentTimeMillis() + 3600000); // 1 hour from now
        auctionHouse.createAuction(endTime);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Auction Created");
        alert.setHeaderText(null);
        alert.setContentText("The auction has been created and will end at " + endTime);
        alert.showAndWait();
    }


    private void showAddVehicleDialog() {
        AddVehicleDialog dialog = new AddVehicleDialog(auctionHouse, true);
        Vehicle newVehicle = dialog.getVehicle();

        if (newVehicle != null) {
            // Check if the vehicle already exists in the auction house
            if (auctionHouse.getVehicles().contains(newVehicle)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Duplicate Vehicle");
                alert.setHeaderText(null);
                alert.setContentText("This vehicle is already added to the auction.");
                alert.showAndWait();
            } else {

                auctionHouse.addVehicleToAuction(newVehicle);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Vehicle Added");
                alert.setHeaderText(null);
                alert.setContentText("The vehicle has been added to the auction.");
                alert.showAndWait();

                // Refresh the vehicle list view
                vehicleListView.setItems(auctionHouse.getVehicles());
            }
        }
    }


    private void showAdminLoginPage() {
        Stage adminLoginStage = new Stage();
        adminLoginStage.setTitle("Admin Login");

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            boolean validAdmin = authService.authenticateAdmin(username, password);

            if (validAdmin) {
                adminLoginStage.close();
                showAdminPage();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid admin credentials.");
                alert.showAndWait();
            }
        });

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, loginButton);

        adminLoginStage.setScene(new Scene(root));
        adminLoginStage.show();
    }
}
