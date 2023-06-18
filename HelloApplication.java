package com.example.auction;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Date;
import java.util.Optional;

public class HelloApplication extends Application {

    private AuctionHouse auctionHouse;
    private AuthenticationService authService;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        authService = new AuthenticationService();
        auctionHouse = new AuctionHouse(authService);
        VBox authBox = createAuthPage();
        primaryStage.setTitle("Auction System");
        primaryStage.setScene(new Scene(authBox, 400, 400));
        primaryStage.show();
    }

    private void showUserLoginPage() {
        Stage userLoginStage = new Stage();
        userLoginStage.setTitle("User Login");

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            boolean validUser = authService.authenticateUser(username, password); // Call authenticateUser method

            if (validUser) {
                userLoginStage.close();
                showUserDetailsPage();
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
        root.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, loginButton);

        userLoginStage.setScene(new Scene(root));
        userLoginStage.show();
    }

    private void showUserDetailsPage() {
        Stage userDetailsStage = new Stage();
        userDetailsStage.setTitle("User Details");

        // Create UI elements
        Label welcomeLabel = new Label("Welcome, User!");
        Button viewAuctionsButton = new Button("View Auctions");
        Button placeBidButton = new Button("Place a Bid");

        // Add action listeners to buttons
        viewAuctionsButton.setOnAction(e -> {
            // Logic to view auctions
            ListView<Auction> auctionListView = new ListView<>();
            auctionListView.getItems().addAll(auctionHouse.getAuctions());

            Stage auctionListStage = new Stage();
            auctionListStage.setTitle("Auctions");
            VBox vbox = new VBox(auctionListView);
            Scene scene = new Scene(vbox, 300, 200);
            auctionListStage.setScene(scene);
            auctionListStage.show();
        });

        placeBidButton.setOnAction(e -> {
            // Logic to place a bid
            ChoiceDialog<Auction> auctionChoiceDialog = new ChoiceDialog<>();
            auctionChoiceDialog.setTitle("Select Auction");
            auctionChoiceDialog.setHeaderText("Select an auction to place a bid on:");
            auctionChoiceDialog.getItems().addAll(auctionHouse.getAuctions());

            Optional<Auction> selectedAuction = auctionChoiceDialog.showAndWait();

            if (selectedAuction.isPresent()) {
                ChoiceDialog<Vehicle> vehicleChoiceDialog = new ChoiceDialog<>();
                vehicleChoiceDialog.setTitle("Select Vehicle");
                vehicleChoiceDialog.setHeaderText("Select a vehicle to place a bid on:");
                // Assuming Auction class has a getVehicles() method
                vehicleChoiceDialog.getItems().addAll(selectedAuction.get().getVehicles());

                Optional<Vehicle> selectedVehicle = vehicleChoiceDialog.showAndWait();

                if (selectedVehicle.isPresent()) {
                    TextInputDialog bidAmountDialog = new TextInputDialog();
                    bidAmountDialog.setTitle("Place Bid");
                    bidAmountDialog.setHeaderText("Enter Bid Amount");
                    bidAmountDialog.setContentText("Please enter your bid amount:");

                    Optional<String> bidAmountResult = bidAmountDialog.showAndWait();

                    if (bidAmountResult.isPresent()) {
                        try {
                            double bidAmount = Double.parseDouble(bidAmountResult.get());
                            AuthenticationService.User user = authService.getLoggedInUser();
                            // Call the correct method with the correct parameters
                            auctionHouse.placeBidOnVehicleInAuction(user, bidAmount, selectedVehicle.get(), selectedAuction.get());
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Bid Placed");
                            alert.setHeaderText(null);
                            alert.setContentText("Your bid has been placed.");
                            alert.showAndWait();
                        } catch (NumberFormatException ex) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Please enter a valid number.");
                            alert.showAndWait();
                        }
                    }
                }
            }
        });


        // Create layout and add elements
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(welcomeLabel, viewAuctionsButton, placeBidButton);

        // Set the scene
        userDetailsStage.setScene(new Scene(root));
        userDetailsStage.show();
    }


    private VBox createAuthPage() {
        Button userButton = new Button("User Login");
        userButton.setOnAction(e -> showUserLoginPage()); // Change this line

        Button adminButton = new Button("Admin Login");
        adminButton.setOnAction(e -> showAdminLoginPage());

        VBox authBox = new VBox(10);
        authBox.getChildren().addAll(userButton, adminButton);
        authBox.setSpacing(20);
        authBox.setPadding(new Insets(10));

        return authBox;
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

    private void showAdminPage() {
        Stage adminStage = new Stage();
        adminStage.setTitle("Admin Panel");

        Button createAuctionButton = new Button("Create Auction");
        createAuctionButton.setOnAction(e -> showCreateAuctionDialog());

        Button addVehicleButton = new Button("Add Vehicle to Auction");
        addVehicleButton.setOnAction(e -> showAddVehicleToAuctionDialog());

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(createAuctionButton, addVehicleButton);

        adminStage.setScene(new Scene(root));
        adminStage.show();
    }

    private void showCreateAuctionDialog() {
        TextInputDialog auctionNameDialog = new TextInputDialog();
        auctionNameDialog.setTitle("Create Auction");
        auctionNameDialog.setHeaderText("Set Auction Name");
        auctionNameDialog.setContentText("Please enter auction name:");

        Optional<String> auctionNameResult = auctionNameDialog.showAndWait();

        if (auctionNameResult.isPresent()) {
            String auctionName = auctionNameResult.get();

            TextInputDialog endTimeDialog = new TextInputDialog("1");
            endTimeDialog.setTitle("Create Auction");
            endTimeDialog.setHeaderText("Set Auction End Time");
            endTimeDialog.setContentText("Please enter auction end time in hours:");

            Optional<String> endTimeResult = endTimeDialog.showAndWait();
            if (endTimeResult.isPresent()) {
                try {
                    long endTimeHours = Long.parseLong(endTimeResult.get());
                    long endTimeMillis = endTimeHours * 3600000; // Convert hours to milliseconds
                    Date endTime = new Date(System.currentTimeMillis() + endTimeMillis);
                    auctionHouse.createAuction(auctionName, endTime); // Pass auctionName and endTime
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Auction Created");
                    alert.setHeaderText(null);
                    alert.setContentText("The auction '" + auctionName + "' has been created and will end at " + endTime);
                    alert.showAndWait();
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Please enter a valid number.");
                    alert.showAndWait();
                }
            }
        }
    }



    private void showAddVehicleToAuctionDialog() {
        ChoiceDialog<Auction> auctionChoiceDialog = new ChoiceDialog<>();
        auctionChoiceDialog.setTitle("Select Auction");
        auctionChoiceDialog.setHeaderText("Select an auction to add a vehicle to:");
        auctionChoiceDialog.getItems().addAll(auctionHouse.getAuctions());

        Optional<Auction> selectedAuction = auctionChoiceDialog.showAndWait();

        if (selectedAuction.isPresent()) {
            // Create a custom dialog
            Dialog<Vehicle> dialog = new Dialog<>();
            dialog.setTitle("Add Vehicle");

            // Set the button types
            ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

            // Create input fields
            ComboBox<String> vehicleTypeField = new ComboBox<>();
            vehicleTypeField.getItems().addAll("Car", "Truck");
            vehicleTypeField.setPromptText("Select Vehicle Type");

            TextField vehicleModelField = new TextField();
            vehicleModelField.setPromptText("Vehicle Model");

            TextField vehicleMakeField = new TextField();
            vehicleMakeField.setPromptText("Vehicle Make");

            TextField vehicleNameField = new TextField();
            vehicleNameField.setPromptText("Vehicle Name");

            TextField vehicleYearField = new TextField();
            vehicleYearField.setPromptText("Vehicle Year");

            TextField vehiclePriceField = new TextField();
            vehiclePriceField.setPromptText("Vehicle Price");

            // Create layout for the dialog
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));
            grid.add(new Label("Vehicle Type:"), 0, 0);
            grid.add(vehicleTypeField, 1, 0);
            grid.add(new Label("Vehicle Model:"), 0, 1);
            grid.add(vehicleModelField, 1, 1);
            grid.add(new Label("Vehicle Make:"), 0, 2);
            grid.add(vehicleMakeField, 1, 2);
            grid.add(new Label("Vehicle Name:"), 0, 3);
            grid.add(vehicleNameField, 1, 3);
            grid.add(new Label("Vehicle Year:"), 0, 4);
            grid.add(vehicleYearField, 1, 4);
            grid.add(new Label("Vehicle Price:"), 0, 5);
            grid.add(vehiclePriceField, 1, 5);

            dialog.getDialogPane().setContent(grid);

            // Convert the result to a Vehicle object
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == addButtonType) {
                    String type = vehicleTypeField.getValue();
                    String model = vehicleModelField.getText();
                    String make = vehicleMakeField.getText();
                    String name = vehicleNameField.getText();
                    int year = Integer.parseInt(vehicleYearField.getText());
                    double price = Double.parseDouble(vehiclePriceField.getText());

                    // Create an instance of the appropriate subclass based on the selected type
                    if ("Car".equals(type)) {
                        return new Car(type, model, make, name, year, price);
                    } else if ("Truck".equals(type)) {
                        return new Truck(type, model, make, name, year, price);
                    }
                }
                return null;
            });

            Optional<Vehicle> result = dialog.showAndWait();
            result.ifPresent(vehicle -> {
                auctionHouse.addVehicleToAuction(vehicle, selectedAuction.get());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Vehicle Added");
                alert.setHeaderText(null);
                alert.setContentText("The vehicle has been added to the auction.");
                alert.showAndWait();
            });
        }
    }

}



