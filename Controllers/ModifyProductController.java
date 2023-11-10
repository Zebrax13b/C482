package Controllers;

import Models.*;
import Models.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The ModifyProductController allows modification to a product already in the observable list.
 */
public class ModifyProductController implements  Initializable {
    public Button cancelModifyProduct;
    public TextField textName;
    public TextField textInv;
    public TextField textPrice;
    public TextField textMax;
    public TableColumn columnPartIDTop;
    public TableColumn columnPartNameTop;
    public TableColumn columnInvTop;
    public TableColumn columnPriceTop;
    public TableView tableTop;
    public TableView tableBott;
    public TableColumn columnPartIDBott;
    public TableColumn columnPartNameBott;
    public TableColumn columnInvBott;
    public TableColumn columnPriceBott;
    public Button addAssociatedPart;
    public Button removeAssociatedPart;
    public Button saveButton;
    public TextField textMin;
    public TextField textID;
    public TextField partsSearch;
    Product selectedProduct;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();


    /**
     * On button press, returns to the main screen.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void returnMain(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/inventorysystem/main/InventoryManagement.fxml"));
        Stage InventoryManager = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1288, 1080);
        InventoryManager.setTitle("Modify Product");
        InventoryManager.setScene(scene);
        InventoryManager.show();
    }

    /**
     * Initializes the ModifyProductController and sets the data.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Modify Products Initialized");
        setData();
        saveButton.disableProperty().bind(
                textInv.textProperty().isEmpty().
                        or(textPrice.textProperty().isEmpty().or
                                (textMax.textProperty().isEmpty().or
                                        (textMin.textProperty().isEmpty().or
                                                (textName.textProperty().isEmpty())))));
    }

    /**
     * Loads the data in the text fields for the selected product to be modified.
     */
    public void setData() {
        Product tempProduct = InventoryController.selectedProduct;
        textName.setText(InventoryController.selectedProduct.getName());
        textID.setText(Integer.toString(InventoryController.selectedProduct.getId()));
        textMin.setText(Integer.toString(InventoryController.selectedProduct.getMin()));
        textMax.setText(Integer.toString(InventoryController.selectedProduct.getMax()));
        textPrice.setText(Double.toString(InventoryController.selectedProduct.getPrice()));
        textInv.setText(Integer.toString(InventoryController.selectedProduct.getStock()));

        selectedProduct = InventoryController.selectedProduct;

        columnPartIDTop.setCellValueFactory(new PropertyValueFactory<>("Id"));
        columnPartNameTop.setCellValueFactory(new PropertyValueFactory<>("Name"));
        columnInvTop.setCellValueFactory(new PropertyValueFactory<>("Stock"));
        columnPriceTop.setCellValueFactory(new PropertyValueFactory<>("Price"));
        tableTop.setItems(Inventory.AllParts);

        columnPartIDBott.setCellValueFactory(new PropertyValueFactory<>("Id"));
        columnPartNameBott.setCellValueFactory(new PropertyValueFactory<>("Name"));
        columnInvBott.setCellValueFactory(new PropertyValueFactory<>("Stock"));
        columnPriceBott.setCellValueFactory(new PropertyValueFactory<>("Price"));
        tableBott.setItems(selectedProduct.getAllAssociatedParts());

    }

    /**
     * On button press, adds an associated part to the selected product.
     *
     * @param actionEvent
     */
    public void clickAddAssociatedPart(ActionEvent actionEvent) {
        Part selectedPart = (Part) tableTop.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            selectedProduct.addAssociatedPart(selectedPart);
            tableBott.setItems(selectedProduct.getAllAssociatedParts());
            tableBott.refresh();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Part Has Been Selected");
            alert.setContentText("Please Select A Part To Add");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }

    /**
     * on button press, removes an associated part from the selected product.
     *
     * @param actionEvent
     */
    public void clickRemoveAssociatedPart(ActionEvent actionEvent) {
        Part selectedPart = (Part) tableBott.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove Associated Part");
            alert.setHeaderText("Are You Sure You Would Like To Remove Associated Part?");
            alert.setContentText("Press OK To Confirm");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                selectedProduct.deleteAssociatedPart(selectedPart);
                tableBott.setItems(selectedProduct.getAllAssociatedParts());

            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Part Has Been Selected");
            alert.setContentText("Please Select A Part To Remove");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }


    /**
     * on button press, allows the selected product to be saved with the new changes.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void clickSaveButton(ActionEvent actionEvent) throws IOException {

        try {
            Integer.parseInt(textMax.getText());
            Integer.parseInt(textMin.getText());
            Double.parseDouble(textPrice.getText());
            Integer.parseInt(textInv.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Invalid");
            alert.setContentText("Product Inventory, Cost, Minimum, and Maximum must be numerical entries.");
            alert.showAndWait();
            return;
        }
            int index = Inventory.AllProducts.indexOf(InventoryController.selectedProduct);
            int stock = Integer.parseInt(textInv.getText());
            int min = Integer.parseInt(textMin.getText());
            int max = Integer.parseInt(textMax.getText());

            try {
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
                Product tempProduct = InventoryController.selectedProduct;
                tempProduct.setId(Integer.parseInt(textID.getText()));
                tempProduct.setName(textName.getText());
                tempProduct.setStock(Integer.parseInt(textInv.getText()));
                tempProduct.setPrice(Double.parseDouble(textPrice.getText()));
                tempProduct.setMin(Integer.parseInt(textMin.getText()));
                tempProduct.setMax(Integer.parseInt(textMax.getText()));

                Inventory.AllProducts.set(index, tempProduct);

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
            alert.setContentText("Incorrect value");
            alert.showAndWait();
        }
    }

    /** RUNTIME ERROR
     *  Exception in thread "JavaFX Application Thread" java.lang.RuntimeException
     *  Caused by: java.lang.NullPointerException
     *  A syntax error in the FXML labeling of the searchbox was found. A simple rename and the error was corrected.
     *  There was also an error with tableTop showing as empty after the Alert Warning was shown. This was corrected via
     *  changing the method where tapleTop set items.
     *
     * Searches via Part ID or Name from the observable list of All Parts and updates the parts table
     *
     * @param actionEvent
     */
    public void partsSearchAction(ActionEvent actionEvent) {
        String searchText = partsSearch.getText();
        ObservableList<Part> foundPart = Inventory.lookupPart(searchText);
        tableTop.setItems(foundPart);
        if (searchText.isEmpty() || foundPart.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Search Invalid");
            alert.setContentText("No Part ID's or Names Found");
            alert.showAndWait();
            partsSearch.setText("Search by Part ID or Name");
            tableTop.setItems(Inventory.AllParts);
        }
    }
}
