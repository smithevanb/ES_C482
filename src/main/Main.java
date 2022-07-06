package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

/**
 * RUNTIME ERROR: Main threw a Null Pointer Exception until the path to Main Form was fixed
 *
 * FUTURE ENHANCEMENT: Adding a database to store added Parts and Products, so that all data provided is not volatile
 * and only kept while the application is running only, would greatly improve this application.
 */

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        stage.setTitle("Main Form");
        stage.setScene(new Scene(root, 900, 400));
        stage.show();
    }


    public static void main(String[] args){

        Product product1 = new Product(1,5,0,10,"chair",6.99);
        Product product2 = new Product(2,10,0,30,"desk",10.99);
        Product product3 = new Product(3,7,0,15,"stool",4.99);
        Part part1 = new InHouse(1,"seat",5.00,10,0,20,001);
        Part part2 = new Outsourced(2,"beam",2.00,40,4,100,"Ace Hardware");

        Inventory.addProduct(product1);
        Inventory.addProduct(product2);
        Inventory.addProduct(product3);
        Inventory.addPart(part1);
        Inventory.addPart(part2);

        launch(args);
    }
}
