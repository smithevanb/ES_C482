package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * RUNTIME ERROR: This class had a Invocation Exception Error, when referenced in other classes, while trying to invoke
 * its lookupPart and lookupProduct methods, as they are static, and were attempting to reference a selected item from
 * other pages' Table Views, which could not be referenced in a static context.
 *
 * FUTURE ENHANCEMENT: A more secure/complex method of obtaining productIdCounter and partIdCounter would improve this
 * class.
 */

public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    private static ObservableList<Part> filteredParts = FXCollections.observableArrayList();

    private static ObservableList<Product> filteredProducts = FXCollections.observableArrayList();

    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    public static ObservableList<Part> getFilteredParts() { return filteredParts;}

    public static ObservableList<Product> getFilteredProducts() { return filteredProducts;}

    public static int productIdCounter = 4;

    public static int partsIdCounter = 5;

    public static void addPart(Part part) {
        allParts.add(part);
    }

    public static void addProduct(Product product) {
        allProducts.add(product);
    }

    public static boolean deletePart(Part selectedPart) {
        allParts.remove(selectedPart);
        return true;
    }

    public static boolean deleteProduct(Product selectedProduct) {
        allProducts.remove(selectedProduct);
        return true;
    }

    public static void updatePart(int index, Part selectedPart) {

        allParts.set(index, selectedPart);
    }

    public static void updateProduct(int index, Product newProduct) {

        allProducts.set(index, newProduct);

    }

    public static Part lookupPart(int partId) {
        int index = -1;

        for(Part part : Inventory.getAllParts()) {
            index++;

            if(part.getId() == partId) {
                return allParts.get(index); // returns matching part using its index
            }
        }
        return null; //if no part was found
    }

    public static Product lookupProduct(int productId) {
        int index = -1;

        for(Product product: Inventory.getAllProducts()) {
            index++;

            if(product.getId() == productId) {
                return getAllProducts().get(index); //returns the matching product using its index

            }
        }
        return null; //if no product was found
    }


    public static ObservableList<Part> lookupPart(String partName) {
        int index = -1; //not sure if needed
        ObservableList<Part> matchingParts = null;

        for(Part part : Inventory.getAllParts()) {
            index++;

            if(part.getName() == partName) {
                matchingParts.add(part);
            }
        }
        return matchingParts;
    }

    public static ObservableList<Product> lookupProduct(String productName) {
        int index = -1; //not sure if needed
        ObservableList<Product> matchingProducts = null;

        for(Product product : Inventory.getAllProducts()) {
            index++;

            if(product.getName() == productName)
                matchingProducts.add(product);
        }
        return matchingProducts;
    }
}
