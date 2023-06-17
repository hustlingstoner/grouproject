package com.example.auction;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class AddVehicleDialog extends Dialog<Vehicle> {
    private ComboBox<String> vehicleTypeComboBox;
    private TextField vehicleModelField;
    private TextField vehicleMakeField;
    private TextField vehicleYearField;
    private TextField vehiclePriceField;
    private boolean isAdminLoggedIn;
    private AuctionHouse auctionHouse;

    public AddVehicleDialog(AuctionHouse auctionHouse, boolean isAdminLoggedIn) {
        this.auctionHouse = auctionHouse;
        this.isAdminLoggedIn = isAdminLoggedIn;

        setTitle("Add Vehicle");
        setHeaderText("Enter the details of the new vehicle");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        vehicleTypeComboBox = new ComboBox<>();
        vehicleTypeComboBox.getItems().addAll("Car", "Motorcycle", "Truck");
        vehicleTypeComboBox.setValue("Car");

        vehicleModelField = new TextField();
        vehicleModelField.setPromptText("Vehicle Model");

        vehicleMakeField = new TextField();
        vehicleMakeField.setPromptText("Vehicle Make");

        vehicleYearField = new TextField();
        vehicleYearField.setPromptText("Vehicle Year");

        vehiclePriceField = new TextField();
        vehiclePriceField.setPromptText("Vehicle Price");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));

        gridPane.add(new Label("Vehicle Type:"), 0, 0);
        gridPane.add(vehicleTypeComboBox, 1, 0);
        gridPane.add(new Label("Vehicle Model:"), 0, 1);
        gridPane.add(vehicleModelField, 1, 1);
        gridPane.add(new Label("Vehicle Make:"), 0, 2);
        gridPane.add(vehicleMakeField, 1, 2);
        gridPane.add(new Label("Vehicle Year:"), 0, 3);
        gridPane.add(vehicleYearField, 1, 3);
        gridPane.add(new Label("Vehicle Price:"), 0, 4);
        gridPane.add(vehiclePriceField, 1, 4);

        getDialogPane().setContent(gridPane);

        setResultConverter(buttonType -> {
            if (buttonType.equals(addButtonType) && isAdminLoggedIn) {
                String type = vehicleTypeComboBox.getValue();
                String model = vehicleModelField.getText();
                String make = vehicleMakeField.getText();
                int year = Integer.parseInt(vehicleYearField.getText());
                double price = Double.parseDouble(vehiclePriceField.getText());

                if (!model.isEmpty() && !make.isEmpty()) {
                    switch (type) {
                        case "Car":
                            Car car = new Car("Car", model, make, "CarName", year, price);
                            return car;
                        case "Motorcycle":
                            Motorcycle motorcycle = new Motorcycle("Motorcycle", model, make, "MotorcycleName", year, price);
                            return motorcycle;
                        case "Truck":
                            Truck truck = new Truck("Truck", model, make, "TruckName", year, price);
                            return truck;
                    }
                }
            }
            return null;
        });
    }

    public Vehicle getVehicle() {
        Optional<Vehicle> result = showAndWait();
        if (result.isPresent()) {
            return result.get();
        }
        return null;
    }
}
