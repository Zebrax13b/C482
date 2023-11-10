package Controllers;


import Models.Inventory;
import Models.Part;
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
 * The AddProductController adds and changes the Product Inventory.
 */
public class AddProductController implements Initializable {

    public Button buttonAddAssociated;

    public Button buttonRemoveAssociated;

    public TextField textID;

    public TextField textName;

    public TextField textInv;

    public TextField textPrice;

    public TextField textMax;

    public TextField textMin;

    public TextField partsSearch;

    Product selectedProduct;

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    public Button saveButton;

    public Button cancelAddProduct;

    public TableColumn columnPartIDTop;

    public TableColumn columnPartNameTop;
    public TableColumn columnInvTop;
    public TableColumn columnPriceTop;
    public TableColumn columnPartIDBott;
    public TableColumn columnInvBott;
    public TableColumn columnPriceBott;
    public TableView<Part> tableTop;
    public TableView<Part> tableBott;
    public TableColumn columnPartNameBott;

    /**
     * On button press, return to the main screen.
     * @param actionEvent
     * @throws IOException
     */
    public void returnMain(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Add Product");
        alert.setContentText("Would you like to cancel changes and return to Main Screen?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("/inventorysystem/main/InventoryManagement.fxml"));
            Stage InventoryManager = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1288, 1080);
            InventoryManager.setTitle("Add Product");
            InventoryManager.setScene(scene);
            InventoryManager.show();
        }
    }

    /**
     * Initializes the AddProductController and sets the table columns for the associated parts.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Add Product Controller Initiallized");

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

        saveButton.disableProperty().bind(
                textInv.textProperty().isEmpty().
                        or(textPrice.textProperty().isEmpty().or
                                (textMax.textProperty().isEmpty().or
                                        (textMin.textProperty().isEmpty().or
                                                (textName.textProperty().isEmpty())))));
    }

    /**
     * On button press, adds an associated part from the table for the product to be added.
     * @param actionEvent
     */
    public void clickAddAssociated(ActionEvent actionEvent) {
        Part selectedPart = tableTop.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            associatedParts.add(selectedPart);
            tableBott.setItems(associatedParts);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Part Has Been Selected");
            alert.setContentText("Please Select A Part To Add");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }

    /** RUNTIME ERROR
     * Caused by: java.lang.NullPointerException: Cannot invoke "Models.Product.deleteAssociatedPart(Models.Part)" because "this.selectedProduct" is null
     * This runtime error occurred because I attempted to remove parts from the selected Product, but when adding a new
     * Product, there is no selection model. The correction was to disassociate it from a selection model
     *
     * On button press, removes an associated part from the table for the product to be added.
     * @param actionEvent
     */
    public void clickRemoveAssociated(ActionEvent actionEvent) {
        Part selectedPart = tableBott.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove Associated Part");
            alert.setHeaderText("Are You Sure You Would Like To Remove Associated Part?");
            alert.setContentText("Press OK To Confirm");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                associatedParts.remove(selectedPart);
                tableBott.setItems(associatedParts);
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
     * On button press, saves the new product to be added to inventory.
     * @param actionEvent
     * @throws IOException
     */
    public void clickSaveButton (ActionEvent actionEvent) throws IOException {

        try {
            Integer.parseInt(textMax.getText());
            Integer.parseInt(textMin.getText());
            Double.parseDouble(textPrice.getText());
            Integer.parseInt(textInv.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Invalid");
            alert.setContentText("Product Inventory, Cost, Minimum, and Maximum must be numerical entries.");
            alert.showAndWait();
            return;
        }
            int productID = Inventory.incrementProductID();
            String name = textName.getText();
            Double price = Double.parseDouble(textPrice.getText());
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
                Product New = new Product(productID, name, price, stock, min, max);

                for (Part part : associatedParts) {
                    New.addAssociatedPart(part);
                }
                Inventory.addProduct(New);
                Parent root = FXMLLoader.load(getClass().getResource("/inventorysystem/main/InventoryManagement.fxml"));
                Stage InventoryManager = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 1288, 1080);
                InventoryManager.setTitle("Inventory Manager");
                InventoryManager.setScene(scene);
                InventoryManager.show();

            }
        }
            catch(NumberFormatException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setContentText("Incorrect value");
                alert.showAndWait();
            }
        }

    /**
     * Searches via Part ID or Name from the observable list of All Parts and updates the top table
     * @param actionEvent
     */
    public void partsSearchOnAction(ActionEvent actionEvent) {
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

