package Controllers;


import Models.Inventory;
import Models.Part;
import Models.Product;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** FUTURE ENHANCEMENT
 *
 * Updating the search function to update the table live per key press instead of needing to press enter when the search is ready.
 * Another enhancement would be making the search fields correctly populate the default message and delete the default message appropriately.
 *
 *
 * The InventoryController provides usage and logic for the main UI and allows interaction with the inventory.
 */
public class InventoryController implements Initializable {


    public Button mainExit;
    public Button productsDelete;
    public Button productsModify;
    public Button productsAdd;
    public Button partsDelete;
    public Button partsModify;
    public Button partsAdd;
    public TextField partsSearch;
    public TextField productsSearch;
    public TableView<Part> partsTable;
    public TableView<Product> productsTable;
    public TableColumn columnPartID;
    public TableColumn columnPartName;
    public TableColumn columnPartInv;
    public TableColumn columnPartPrice;
    public TableColumn columnProductID;
    public TableColumn columnProductName;
    public TableColumn columnProductInv;
    public TableColumn columnProductPrice;

    public static Part selectedPart;
    public static Product selectedProduct;


    /**
     * Initializes the InventoryController and sets both table values with the current inventory.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println("Initialized Main Window");

        columnPartID.setCellValueFactory(new PropertyValueFactory<>("Id"));
        columnPartName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        columnPartInv.setCellValueFactory(new PropertyValueFactory<>("Stock"));
        columnPartPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        partsTable.setItems(Inventory.AllParts);

        columnProductID.setCellValueFactory(new PropertyValueFactory<>("Id"));
        columnProductName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        columnProductInv.setCellValueFactory(new PropertyValueFactory<>("Stock"));
        columnProductPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        productsTable.setItems(Inventory.AllProducts);


    }

    /**
     * On  button press, loads the AddPart.fxml and controller screen.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void buttonAddParts(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/inventorysystem/main/AddPart.fxml"));
        Stage addPart = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1288, 1080);
        addPart.setTitle("Add Parts");
        addPart.setScene(scene);
        addPart.show();
    }

    /**
     * on button press, loads the ModifyPart.fxml and controller screen.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void buttonModifyParts(ActionEvent actionEvent) throws IOException {
        selectedPart = partsTable.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            Parent root = FXMLLoader.load(getClass().getResource("/inventorysystem/main/ModifyPart.fxml"));
            Stage modifyPart = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1288, 1080);
            modifyPart.setTitle("Modify Parts");
            modifyPart.setScene(scene);
            modifyPart.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("No Part Selected");
            alert.showAndWait();
        }

    }

    /**
     * on button press, removes the selected part from the observable list.
     *
     * @param actionEvent
     */
    public void buttonDeleteParts(ActionEvent actionEvent) {
        if (partsTable.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("No Part Selected");
            alert.showAndWait();
        } else if (confirmDelete("Delete Confirmation", "Would you like to remove the selected part?")) {
            int selectPart = partsTable.getSelectionModel().getSelectedIndex();
            partsTable.getItems().remove(selectPart);
        }
    }


    /**
     * on button press, launches the AddProducts.fxml and controller screen.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void buttonAddProducts(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/inventorysystem/main/AddProduct.fxml"));
        Stage addProduct = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1288, 1080);
        addProduct.setTitle("Add Parts");
        addProduct.setScene(scene);
        addProduct.show();
    }

    /**
     * on button press, launches the ModifyProducts.fxml and controller screen.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void buttonModifyProducts(ActionEvent actionEvent) throws IOException {
        selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            Parent root = FXMLLoader.load(getClass().getResource("/inventorysystem/main/ModifyProduct.fxml"));
            Stage modifyProduct = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1288, 1080);
            modifyProduct.setTitle("Modify Products");
            modifyProduct.setScene(scene);
            modifyProduct.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("No Product Selected");
            alert.showAndWait();
        }
    }

    /**
     * RUNTIME ERROR Caused by: java.lang.NullPointerException
     * Correction was changing the multiple if statements to else if statements. The latter statements were still trying to execute
     * after the first error message went away
     * on button press, removes the selected product from the observable list.
     *
     * @param actionEvent
     */
    public void buttonDeleteProducts(ActionEvent actionEvent) {
        Product selectProduct = productsTable.getSelectionModel().getSelectedItem();
        if (productsTable.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("No Product Selected");
            alert.showAndWait();
        } else if (selectProduct.getAllAssociatedParts().size() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Selected Product has associated parts attached. Remove before deleting.");
            alert.showAndWait();

        } else if (confirmDelete("Delete Confirmation", "Would you like to remove the selected product?")) {
            productsTable.getItems().remove(selectProduct);
        }
    }

    /**
     * on button press, returns to the main screen
     *
     * @param actionEvent
     */
    @FXML
    public void buttonMainExit(ActionEvent actionEvent) {
        Stage stage = (Stage) mainExit.getScene().getWindow();
        stage.close();
    }

    /**
     * provides a delete confirmation prompt when removing a product or part from the observable list.
     *
     * @param Title
     * @param Content
     * @return
     */
    static boolean confirmDelete(String Title, String Content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Title);
        alert.setHeaderText("Please Confirm.");
        alert.setContentText(Content);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Searches via Part ID or Name from the observable list of All Parts and updates the parts table
     *
     * @param actionEvent
     */
    public void partsSearchAction(ActionEvent actionEvent) {
        String searchText = partsSearch.getText();
        ObservableList<Part> foundPart = Inventory.lookupPart(searchText);
        partsTable.setItems(foundPart);
        if (searchText.isEmpty() || foundPart.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Search Invalid");
            alert.setContentText("No Part ID's or Names Found");
            alert.showAndWait();
            partsSearch.setText("Search by Part ID or Name");
            partsTable.setItems(Inventory.AllParts);
        }
    }

    /**
     * Searches via Product ID or Name from the observable list of All Products and updates the Products Table
     *
     * @param actionEvent
     */
    public void productSearchAction(ActionEvent actionEvent) {
        String searchText = productsSearch.getText();
        ObservableList<Product> foundProduct = Inventory.lookupProduct(searchText);
        productsTable.setItems(foundProduct);
        if (searchText.isEmpty() || foundProduct.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Search Invalid");
            alert.setContentText("No Product ID's or Names Found");
            alert.showAndWait();
            productsSearch.setText("Search by Product ID or Name");
            productsTable.setItems(Inventory.AllProducts);
        }
    }

    /**
     * Clears the search field on mouse click if it's populated with the default message
     *
     * @param mouseEvent
     */
    public void partSearchMouseClick(MouseEvent mouseEvent) {
        String searchText = partsSearch.getText();
        if (searchText.equals("Search by Part ID or Name")) {
            partsSearch.setText("");
        }
    }

    /**
     * Clears the search field on mouse click if it's populated with the default message
     *
     * @param mouseEvent
     */
    public void productSearchMouseClick(MouseEvent mouseEvent) {
        String searchText = productsSearch.getText();
        if (searchText.equals("Search by Product ID or Name")) {
            productsSearch.setText("");
        }
    }
}