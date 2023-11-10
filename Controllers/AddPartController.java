package Controllers;

import Models.InHouse;
import Models.Inventory;
import Models.Outsourced;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * The AddPartController adds and changes the part inventory
 */
public class AddPartController implements Initializable {

    public Button cancelAddPart;

    public TextField textPartID;

    public TextField textPartName;

    public TextField textPartCost;

    public TextField textPartMax;

    public TextField textSwitchable;

    public TextField textPartMin;

    public RadioButton radioInHouse;

    public RadioButton radioOutsourced;

    public Label labelSwitchable;

    public Button addPartSaveButton;

    public TextField textInventory;

    /**
     * Initializes the AddPartController and displays a prompt when initialized.
     * Also disables the save button until all fields are populated.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Add Parts Screen Initialized");
        addPartSaveButton.disableProperty().bind(
                textInventory.textProperty().isEmpty().
                        or(textPartCost.textProperty().isEmpty().or
                                (textPartMax.textProperty().isEmpty().or
                                        (textPartMin.textProperty().isEmpty().or
                                                (textPartName.textProperty().isEmpty().or(
                                                        textSwitchable.textProperty().isEmpty()))))));
    }

    /**
     * On button press, return to the main screen.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void returnMain(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Add Part");
        alert.setContentText("Would you like to cancel changes and return to Main Screen?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("/inventorysystem/main/InventoryManagement.fxml"));
            Stage InventoryManager = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1288, 1080);
            InventoryManager.setTitle("Inventory Manager");
            InventoryManager.setScene(scene);
            InventoryManager.show();
        }
    }

    /**
     * On button press, sets the switchable text to Machine ID and disables the Outsourced Button
     *
     * @param actionEvent
     */
    public void radioInHouse(ActionEvent actionEvent) {
        labelSwitchable.setText("Machine ID");
        radioOutsourced.setSelected(false);
    }

    /**
     * On button press, sets the switchable text to Company Name and disables the InHouse Button
     *
     * @param actionEvent
     */
    public void radioOutsourced(ActionEvent actionEvent) {
        labelSwitchable.setText("Company Name");
        radioInHouse.setSelected(false);

    }

    /**
     * On button press,saves the part that has been inputted by the user to be added to inventory.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void addPartSaveButton(ActionEvent actionEvent) throws IOException {

        try {
            Integer.parseInt(textPartMax.getText());
            Integer.parseInt(textPartMin.getText());
            Double.parseDouble(textPartCost.getText());
            Integer.parseInt(textInventory.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Invalid");
            alert.setContentText("Part Inventory, Cost, Minimum, and Maximum must be numerical entries.");
            alert.showAndWait();
            return;
        }

            int partID = Inventory.incrementPartID();
            String name = textPartName.getText();
            int stock = Integer.parseInt(textInventory.getText());
            double price = Double.parseDouble(textPartCost.getText());
            int min = Integer.parseInt(textPartMin.getText());
            int max = Integer.parseInt(textPartMax.getText());

            if (max < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Maximum must be greater than minimum.");
                alert.showAndWait();
            } else if (stock < min || max < stock) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Inventory must be within min and max.");
                alert.showAndWait();
            } else if (min < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Minimum must be higher than 0");
                alert.showAndWait();
            } else {
                try {
                    if (radioInHouse.isSelected()) {
                        int machineID = Integer.parseInt(textSwitchable.getText());
                        InHouse New = new InHouse(partID, name, price, stock, min, max, machineID);
                        Inventory.addPart(New);

                        Parent root = FXMLLoader.load(getClass().getResource("/inventorysystem/main/InventoryManagement.fxml"));
                        Stage InventoryManager = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root, 1288, 1080);
                        InventoryManager.setTitle("Inventory Manager");
                        InventoryManager.setScene(scene);
                        InventoryManager.show();
                    } else if (radioOutsourced.isSelected()) {
                        String companyName = textSwitchable.getText();
                        Outsourced New = new Outsourced(partID, name, price, stock, min, max, companyName);
                        Inventory.addPart(New);

                        Parent root = FXMLLoader.load(getClass().getResource("/inventorysystem/main/InventoryManagement.fxml"));
                        Stage InventoryManager = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root, 1288, 1080);
                        InventoryManager.setTitle("Inventory Manager");
                        InventoryManager.setScene(scene);
                        InventoryManager.show();
                    }
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Input Error");
                    alert.setContentText("Machine ID entry must only be numeric");
                    alert.showAndWait();
                }
            }
        }
    }
