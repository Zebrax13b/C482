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
 * The ModifyPartController allows modification of a part already in the observable list.
 */
public class ModifyPartController implements Initializable {

    public Button cancelModifyPart;
    public TextField textID;
    public TextField textName;
    public TextField textPrice;
    public TextField textMax;
    public TextField textSwitchable;
    public TextField textMin;
    public Button modifyPartSaveButton;
    public TextField textInventory;
    public ButtonBar radioGroup;
    public Label labelSwitchable;
    public RadioButton radioInHouse;
    public RadioButton radioOutsourced;

    /**
     * Initializes the ModifyPartController and displays a prompt when initialized.
     * Runs setData()
     * Disables the save button if any text fields are empty.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Modify Part Window Initialized");
        setData();
        modifyPartSaveButton.disableProperty().bind(
                textInventory.textProperty().isEmpty().
                        or(textPrice.textProperty().isEmpty().or
                                (textMax.textProperty().isEmpty().or
                                        (textMin.textProperty().isEmpty().or
                                                (textName.textProperty().isEmpty().or(
                                                        textSwitchable.textProperty().isEmpty()))))));
        }

    /**
     * Loads the data in the text fields for the selected part to be modified
     */
    public void setData() {
        if (InventoryController.selectedPart instanceof InHouse) {
            InHouse tempPart = (InHouse) InventoryController.selectedPart;
            labelSwitchable.setText(("Machine ID"));
            textSwitchable.setText(Integer.toString(tempPart.getMachineId()));
            textName.setText(InventoryController.selectedPart.getName());
            textID.setText(Integer.toString(InventoryController.selectedPart.getId()));
            textMin.setText(Integer.toString(InventoryController.selectedPart.getMin()));
            textMax.setText(Integer.toString(InventoryController.selectedPart.getMax()));
            textPrice.setText(Double.toString(InventoryController.selectedPart.getPrice()));
            textInventory.setText(Integer.toString(InventoryController.selectedPart.getStock()));
            radioInHouse.setSelected(true);
        }
        if (InventoryController.selectedPart instanceof Outsourced) {
            Outsourced tempPart = (Outsourced) InventoryController.selectedPart;
            labelSwitchable.setText(("Company Name"));
            textSwitchable.setText(tempPart.getCompanyName());
            textName.setText(InventoryController.selectedPart.getName());
            textID.setText(Integer.toString(InventoryController.selectedPart.getId()));
            textMin.setText(Integer.toString(InventoryController.selectedPart.getMin()));
            textMax.setText(Integer.toString(InventoryController.selectedPart.getMax()));
            textPrice.setText(Double.toString(InventoryController.selectedPart.getPrice()));
            textInventory.setText(Integer.toString(InventoryController.selectedPart.getStock()));
            radioOutsourced.setSelected(true);
        }
    }

    /**
     * on button press, returns to the main screen
     * @param actionEvent
     * @throws IOException
     */
    public void returnMain(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Modify Part");
        alert.setContentText("Would you like to cancel changes and return to Main Screen?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("/inventorysystem/main/InventoryManagement.fxml"));
            Stage InventoryManager = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1288, 1080);
            InventoryManager.setTitle("Modify Parts");
            InventoryManager.setScene(scene);
            InventoryManager.show();
        }
    }

    /**
     * on button press, allows the selected part to be saved with the new changes.
     * @param actionEvent
     * @throws IOException
     */
    public void modifyPartSaveButton(ActionEvent actionEvent) throws IOException {

        try {
            Integer.parseInt(textMax.getText());
            Integer.parseInt(textMin.getText());
            Double.parseDouble(textPrice.getText());
            Integer.parseInt(textInventory.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Invalid");
            alert.setContentText("Part Inventory, Cost, Minimum, and Maximum must be numerical entries.");
            alert.showAndWait();
            return;
        }

            int index = Inventory.AllParts.indexOf(InventoryController.selectedPart);
            int min = Integer.parseInt(textMin.getText());
            int max = Integer.parseInt(textMax.getText());
            int stock = Integer.parseInt(textInventory.getText());

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

                        InHouse tempPart = new InHouse(
                                Integer.parseInt(textID.getText()),
                                textName.getText(),
                                Double.parseDouble(textPrice.getText()),
                                Integer.parseInt(textInventory.getText()),
                                Integer.parseInt(textMin.getText()),
                                Integer.parseInt(textMax.getText()),
                                Integer.parseInt(textSwitchable.getText()));

                        Inventory.AllParts.set(index, tempPart);

                        Parent root = FXMLLoader.load(getClass().getResource("/inventorysystem/main/InventoryManagement.fxml"));
                        Stage InventoryManager = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root, 1288, 1080);
                        InventoryManager.setTitle("Inventory Manager");
                        InventoryManager.setScene(scene);
                        InventoryManager.show();
                    }
                    if (radioOutsourced.isSelected()) {

                        Outsourced tempPart = new Outsourced(
                                Integer.parseInt(textID.getText()),
                                textName.getText(),
                                Double.parseDouble(textPrice.getText()),
                                Integer.parseInt(textInventory.getText()),
                                Integer.parseInt(textMin.getText()),
                                Integer.parseInt(textMax.getText()),
                                textSwitchable.getText());

                        Inventory.AllParts.set(index, tempPart);

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
                    alert.setContentText("Machine ID entry must only be numeric.");
                    alert.showAndWait();
                }
            }
    }

    /**
     * On button press, sets the switchable text to Machine ID and disables the Outsourced button.
     * @param actionEvent
     */
    public void radioInHouse(ActionEvent actionEvent) {
        labelSwitchable.setText("Machine ID");
        radioOutsourced.setSelected(false);
    }

    /**
     * on button press, sets the switchable text to Company Name and disables the InHouse button.
     * @param actionEvent
     */
    public void radioOutsourced(ActionEvent actionEvent) {
        labelSwitchable.setText("Company Name");
        radioInHouse.setSelected(false);
    }

}
