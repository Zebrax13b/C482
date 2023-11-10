package inventorysystem.main;

import Models.InHouse;
import Models.Inventory;
import Models.Outsourced;
import Models.Product;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


/**
 * The InventoryApplication Class creates the application for inventory management and adds sample parts and products
 *
 * @author
 * Nathaniel Coulter
 * Student ID: 003745326
 *
 *
 */
public class InventoryApplication extends Application {

    /**
     * The start method initializes the fxml file for the main screen InventoryManagement.fxml
     * @param mainScreen
     * @throws IOException
     */
    @Override
    public void start(Stage mainScreen) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InventoryApplication.class.getResource("/inventorysystem/main/InventoryManagement.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1288, 1040);
        mainScreen.setTitle("Inventory Management");
        mainScreen.setScene(scene);
        mainScreen.show();
    }
// JavaDoc Folder Located @ /C482/src/main/JavaDocs
    /**
     * The main method adds the test data and launches the application.
     * @param args
     */
    public static void main(String[] args) {
        InHouse pcv = new InHouse(1, "PCV Valve", 25.00, 2, 1, 5, 99708-73523);
        Outsourced hose = new Outsourced(2, "Hose", 30.00, 3, 1, 6, "Subaru");
        InHouse starter = new InHouse(3, "Starter", 99.00, 1, 1, 3, 15203-17632);
        Outsourced radiator = new Outsourced(4, "Radiator", 359.00, 1, 1, 2, "1-800 Radiator");

        Inventory.addPart(pcv);
        Inventory.addPart(hose);
        Inventory.addPart(starter);
        Inventory.addPart(radiator);

        Product Ascent = new Product(1, "Ascent", 41595, 1, 1, 8);
        Product Impreza = new Product(2, "Impreza", 24995, 2, 1, 6);

        Inventory.addProduct(Ascent);
        Inventory.addProduct(Impreza);

        Ascent.addAssociatedPart(pcv);
        Ascent.addAssociatedPart(hose);
        Impreza.addAssociatedPart(starter);
        Impreza.addAssociatedPart(radiator);


        launch();
    }


}