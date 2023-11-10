package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * The Inventory Class establishes the Observable lists for both Parts and Products
 */
public class Inventory {


    public static ObservableList<Part> AllParts = FXCollections.observableArrayList();
    public static ObservableList<Product> AllProducts = FXCollections.observableArrayList();


    /**
     * Adds a new part to the observable list
     * @param newPart
     */
    public static void addPart(Part newPart) {

        AllParts.add(newPart);
    }

    /**
     * Adds a new product to the observable list
     * @param newProduct
     */
    public static void addProduct(Product newProduct) {

        AllProducts.add(newProduct);
    }

    /**
     * Searches for a partID with a loop and returns the searched part if found
     * @param partId
     * @return
     */
    public static Part lookupPart(int partId) {
        Part searchedPart = null;
        for (Part part : Inventory.getAllParts()) {
            if (part.getId() == partId) {
                searchedPart = part;
            }
        }
        return searchedPart;
    }

    /**
     * Searches for a productID with a loop and returns the product if found
     * @param productId
     * @return
     */
    public static Product lookupProduct(int productId) {
        Product searchedProduct = null;
        for(Product product: Inventory.getAllProducts()) {
            if (product.getId() == productId)
                searchedProduct = product;
        }
        return searchedProduct;
    }

    /**
     * creates a temporary observable list of parts filtered and returns any found.
     * @param partName
     * @return
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> searchedPart = FXCollections.observableArrayList();

        for (Part part: AllParts) {
            if (part.getName().toLowerCase().contains(partName.toLowerCase()) || String.valueOf(part.getId()).contains(partName)) {
                searchedPart.add(part);
            }
    }
    return searchedPart;
    }

    /**
     * creates a temporary observable list of products filtered and returns any found
     * @param productName
     * @return
     */
    public static ObservableList<Product> lookupProduct (String productName) {
        ObservableList<Product> searchedProduct = FXCollections.observableArrayList();

        for (Product product : AllProducts) {
            if (product.getName().toLowerCase().contains(productName.toLowerCase()) || String.valueOf(product.getId()).contains(productName)) {
                searchedProduct.add(product);
            }
        }
        return searchedProduct;
    }

    /**
     * Updates a part with an observable list
     * @param index
     * @param selectedPart
     */
    public static void updatePart(int index, Part selectedPart) {

        AllParts.set(index, selectedPart);
    }

    /**
     * Updates the product with an observable list
     * @param index
     * @param selectedProduct
     */
    public static void updateProduct(int index, Product selectedProduct) {

        AllProducts.set(index, selectedProduct);
    }

    /**
     * Removes a part within the observable list
     * @param selectedPart
     * @return
     */
    public static boolean deletePart (Part selectedPart) {
        if (AllParts.contains(selectedPart)) {
            AllParts.remove(selectedPart);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Removes a product within the observable list
     * @param selectedProduct
     * @return
     */
    public static boolean deleteProduct (Product selectedProduct) {
        if(AllProducts.contains(selectedProduct)) {
            AllProducts.remove(selectedProduct);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Returns All Parts within the Observable list
     * @return
     */
    public static ObservableList<Part> getAllParts() {

        return AllParts;
    }

    /**
     * Returns All Products within the Observable list
     * @return
     */
    public static ObservableList<Product> getAllProducts() {

        return AllProducts;
    }

    /**
     * Loops through all parts in the current inventory and returns a new part ID incremented higher
     * @return new part ID
     */
    public static int incrementPartID() {
        int newID = 1;
        for (int i = 0; i < Inventory.getAllParts().size(); i++) {
            newID++;
        }
        return newID;
    }

    /**
     * Loops through all products in the current inventory and returns a new product ID incremented higher
     * @return new product ID
     */
    public static int incrementProductID() {
        int newID = 1;
        for (int i = 0; i < Inventory.getAllProducts().size(); i++) {
            newID++;
        }
        return newID;
    }
}
