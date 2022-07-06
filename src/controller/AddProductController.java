package controller;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class, Add Product, comes from the Main Form, and allows the User to create a
 * new Product object, and add Parts to the Product. The User can also Remove Associated
 * Parts after they have been added to a Product, if they need to.The User can then Save,
 * which will add the Product to the Observable List allProducts, and return to the Main Form.
 *
 * RUNTIME ERROR: This class had issues with the Search bar throwing a Number Format Exception when
 * searching for numbers vs names.
 *
 * FUTURE ENHANCEMENT: This page could include a running tab of sorts, adding up the total cost of all Associated Parts
 * that have been added to the Product, allowing for a better idea of what the Price of the Product should be.
 */
public class AddProductController implements Initializable {

    Stage stage;
    Parent scene;

    public static Product newProduct = null;
    public ObservableList<Part> bottomListOfParts = FXCollections.observableArrayList();

    /** The "Cancel" button, sends the User back to Main Form **/
    @FXML
    void onActionDisplayMainForm(ActionEvent event) throws IOException {

        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This should add a Part to be Associated with the current Product that is being created,
     * by adding it to associatedParts ObservableList, and also moving the Part from the Parts
     * TableView into the Associated Parts TableView. It should also keep users from adding
     * the same Part more than once to a Product.
     */
    @FXML
    void onActionAdd(ActionEvent event) {
        //Take a currently selected Part from Parts table, and add it to Associated Parts table, and into the Observable
        //List bottomListOfParts

        try{
            Part part = PartTable.getSelectionModel().getSelectedItem();

            if(part == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Please select a Part to Associate with this Product");
                alert.showAndWait();
            }
            else{
                bottomListOfParts.add(part);
            }
        }
        catch(NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please select a Part to Associate with this Product!");
            alert.showAndWait();
        }
    }

    @FXML
    void onActionRemoveAssociatedPart(ActionEvent event) {
        if(AssociatedPartTable.getSelectionModel().getSelectedItem() != null) {
            bottomListOfParts.remove(AssociatedPartTable.getSelectionModel().getSelectedItem());
        }
        if(AssociatedPartTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please select a Part to Delete from this Product");
            alert.showAndWait();
        }
    }

    /**
     * Adds a Product to the Inventory's Observable List of allProducts
     */
    @FXML
    void onActionSaveProd(ActionEvent event) throws IOException {

        try{
            Inventory.productIdCounter+=2;
            int id = Inventory.productIdCounter;
            String name = AddProdName.getText();
            int stock = Integer.parseInt(AddProdInv.getText());
            double price = Double.parseDouble(AddProdPrice.getText());
            int min = Integer.parseInt(AddProdMin.getText());
            int max = Integer.parseInt(AddProdMax.getText());

            if(max < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Max must be greater than or equal to Min");
                alert.showAndWait();
            }
            if(min < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Min must be larger than 0");
                alert.showAndWait();
            }
            if(stock > max || stock < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Inventory level must be less than or equal to Max, and greater than or equal to Min");
                alert.showAndWait();
            }
            if(stock <= max && stock >= min) {

                Product product = new Product(id, stock, min, max, name, price);
                product.getAllAssociatedParts().addAll(bottomListOfParts);
                Inventory.addProduct(product);

                //Returns to Main Form after Saving the new product
                stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
        catch(NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please enter valid values into Text Fields");
            alert.showAndWait();
        }
    }

    @FXML
    void onActionSearchParts(ActionEvent event) {
        filterParts(AddProdSearch.getText());
    }

    public ObservableList<Part> filterParts(String name) {

        if (!(Inventory.getFilteredParts().isEmpty())) //if Filtered Parts is Not Empty
            Inventory.getFilteredParts().clear(); //reset the filter
        for (Part part : Inventory.getAllParts()) {
            try {
                //checks for String
                if (part.getName().contains(name)) {
                    Inventory.getFilteredParts().add(part);
                    PartTable.setItems(Inventory.getFilteredParts());
                }
                if (AddProdSearch.getText().isEmpty()) {
                    PartTable.setItems(Inventory.getAllParts());
                }
                else if (part.getId() == Integer.parseInt(AddProdSearch.getText())) {
                    Inventory.getFilteredParts().add(part);
                    PartTable.setItems(Inventory.getFilteredParts());
                }
            }
            catch(NumberFormatException e){
                //ignore
            }
        }

        if(Inventory.getFilteredParts().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "No matching Part was found");
            alert.showAndWait();
            PartTable.setItems(Inventory.getAllParts());
            AddProdSearch.setText("");
            return Inventory.getAllParts();
        }

        return Inventory.getFilteredParts();
    }

    @FXML
    private Button SearchPartsButton;

    @FXML
    private Button AddProdAddButton;

    @FXML
    private Button AddProdCancel;

    @FXML
    private TextField AddProdID;

    @FXML
    private TextField AddProdInv;

    @FXML
    private TextField AddProdMax;

    @FXML
    private TextField AddProdMin;

    @FXML
    private TextField AddProdName;

    @FXML
    private TextField AddProdPrice;

    @FXML
    private Button AddProdRemoveAssocPart;

    @FXML
    private Button AddProdSave;

    @FXML
    private TextField AddProdSearch;

    /** Table View for Associated Part Table of Product to be Added **/
    @FXML
    private TableView<Part> AssociatedPartTable;

    @FXML
    private TableColumn<Part, Double> AssocPartTableCost;

    @FXML
    private TableColumn<Part, Integer> AssocPartTableInvLvl;

    @FXML
    private TableColumn<Part, Integer> AssocPartTablePartID;

    @FXML
    private TableColumn<Part, String> AssocPartTablePartName;

    /** Table View of Parts of Product to be Added **/
    @FXML
    private TableView<Part> PartTable;

    @FXML
    private TableColumn<Part, Double> PartTableCost;

    @FXML
    private TableColumn<Part, Integer> PartTableInvLvl;

    @FXML
    private TableColumn<Part, Integer> PartTablePartID;

    @FXML
    private TableColumn<Part, String> PartTablePartName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //This will get values for the Part Table in the Add Product Form from Inventory.java
        PartTable.setItems(Inventory.getAllParts());
        PartTablePartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        PartTableCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        PartTableInvLvl.setCellValueFactory(new PropertyValueFactory<>("stock"));
        PartTablePartName.setCellValueFactory(new PropertyValueFactory<>("name"));

        //TO DO : ADD ASSOCIATED PART TABLE, SHOULD DRAW FROM ASSOCIATED PARTS OBSERVABLE LIST,
        //WHICH BELONGS TO EACH PRODUCT

        AssociatedPartTable.setItems(bottomListOfParts);
        AssocPartTablePartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        AssocPartTablePartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        AssocPartTableCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        AssocPartTableInvLvl.setCellValueFactory(new PropertyValueFactory<>("stock"));

    }
}
