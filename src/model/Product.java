package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 * RUNTIME ERROR: Previously, getAllAssociatedParts was incorrectly created as static, which was causing multiple
 * Products to share their Associated Parts
 *
 * FUTURE ENHANCEMENT: Adding pictures of Products, if applicable, might be nice for the User GUI.
 */

public class Product {
    private int id, stock, min, max;
    private String name;
    private double price;

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    public Product(int id, int stock, int min, int max, String name, double price) {
        this.id = id;
        this.stock = stock;
        this.min = min;
        this.max = max;
        this.name = name;
        this.price = price;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

    public void addAssociatedParts(Part part) {
            associatedParts.add(part);
            System.out.println("Part added to Product");
    }

    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        associatedParts.remove(selectedAssociatedPart);
        return true;
    }
}
