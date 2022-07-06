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

/** This page opens after selecting "Modify" Underneath the Products Table View, allowing you to add and remove Parts
 * from the selected Product. It also allows you to save any modification to Products, which then returns the User
 * to the Main Form.
 *
 * RUNTIME ERROR: This page threw a Invocation Exception Error when trying to add Associated parts to the list belonging
 * to the selected Product, until Controller communications issues were fixed properly, and two temporary Observable
 * Lists were created, one for holding Parts for the page and populating the bottom Table View, and a second for doing
 * a final shifting from the bottom Table View into the Product's own List of Associated Parts, after the User has
 * pressed the "Save" button. This way, accidental duplicates are avoided, and modifications are only saved by actually
 * pressing "Save", rather than Cancel or Exiting out of the application from this page.
 *
 * FUTURE ENHANCEMENT: This page could also include a running tab of Associated Parts' costs, to give an idea what the
 * Product's Price ought to actually be. That way, a Product won't be listed lower than the sum of its Parts.
 */
public class ModifyProductController implements Initializable {

    Stage stage;
    Parent scene;
    public ObservableList<Part> bottomListOfParts = FXCollections.observableArrayList(); //Populates the bottom table

    /** "Cancel" button, goes back to Main Form **/
    @FXML
    void onActionDisplayMainForm(ActionEvent event) throws IOException {

        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Adds a Part to the Product that was selected for Modification **/
    @FXML
    void onActionAddPart(ActionEvent event) {
        //Take a currently selected Part from Parts table, and add it to Associated Parts table, and into the Observable
        //List bottomListOfParts

        Product product = sentProduct;

        try{
            Part part = ModifyProdPartTable.getSelectionModel().getSelectedItem();

            if(part == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Please select a Part to Associate with this Product");
                alert.showAndWait();
            }
            else{
                bottomListOfParts.add(part);
                System.out.println("Part added!");
                System.out.println(bottomListOfParts);
                System.out.println(product.getAllAssociatedParts());
            }
        }
        catch(NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please select a Part to Associate with this Product");
            alert.showAndWait();
        }
    }

    /** Removes a Part from being associated with the selected Product **/
    @FXML
    void onActionRemoveAssocPart(ActionEvent event) {

        if(ModifyProdAssocPartTable.getSelectionModel().getSelectedItem() != null) {
            bottomListOfParts.remove(ModifyProdAssocPartTable.getSelectionModel().getSelectedItem());
            System.out.println("Part removed");
            System.out.println(bottomListOfParts);
        }
        if(ModifyProdAssocPartTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please select a Part to Delete from this Product");
            alert.showAndWait();
        }
    }

    /** Save Modification of the selected Product, and return to Main Form (presumably???)
     * RUNTIME EXCEPTION on trying to add associatedParts to product, Concurrent Modification Exception
     * **/
    @FXML
    void onActionModifyProdSave(ActionEvent event) throws IOException {

        Product product = sentProduct;
        ObservableList<Part> Temp = FXCollections.observableArrayList();

        try {
            int id = Integer.parseInt(ModifyProdID.getText());
            String name = ModifyProdName.getText();
            int min = Integer.parseInt(ModifyProdMin.getText());
            int max = Integer.parseInt(ModifyProdMax.getText());
            double price = Double.parseDouble(ModifyProdPrice.getText());
            int inv = Integer.parseInt(ModifyProdInv.getText());

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
            if(inv > max || inv < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Inventory level must be less than or equal to Max, and greater than or equal to Min");
                alert.showAndWait();
            }
            if(inv <= max && inv >= min) {

                //Load the associated parts into a Temp list, and then empty the old associated parts list, and replac-
                //-ing it with the "bottom list of parts". This method avoids accidentally saving associated parts as
                //they appear in the Associated Parts Table after clicking "Cancel", and gets rid of any duplicates that
                //might appear in Associated Parts Table and the old associated parts list

                Temp.clear();
                Temp.addAll(bottomListOfParts);
                product.getAllAssociatedParts().clear();
                product.getAllAssociatedParts().addAll(Temp);

                //Update basic parts of the product
                product.setName(name);
                product.setMax(max);
                product.setPrice(price);
                product.setMin(min);
                product.setStock(inv);

                //Return to Main Form
                Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
                stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            }
        }
        catch(NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please enter valid values into Text Fields");
            alert.showAndWait();
        }
    }

    /** Table View for Associated Parts of Products **/
    @FXML
    private TableView<Part> ModifyProdAssocPartTable;

    @FXML
    private TableColumn<Part, Double> ModProdAssocPTCost;

    @FXML
    private TableColumn<Part, Integer> ModProdAssocPTInvLvl;

    @FXML
    private TableColumn<Part, Integer> ModProdAssocPTPartID;

    @FXML
    private TableColumn<Part, String> ModProdAssocPTPartName;

    /** Table View for Parts of Products **/
    @FXML
    private TableView<Part> ModifyProdPartTable;

    @FXML
    private TableColumn<Part, Double> ModProdPTCost;

    @FXML
    private TableColumn<Part, Integer> ModProdPTInvLvl;

    @FXML
    private TableColumn<Part, Integer> ModProdPTPartID;

    @FXML
    private TableColumn<Part, String> ModProdPTPartName;
    /** end of Table View for Parts of Products **/

    @FXML
    private Button ModifyPartAddButton;

    @FXML
    private Button ModifyProdCancel;

    @FXML
    private Button ModifyProdRemoveAssocPart;

    @FXML
    private Button ModifyProdSave;

    @FXML
    private TextField ModifyProdID;

    @FXML
    private TextField ModifyProdInv;

    @FXML
    private TextField ModifyProdMax;

    @FXML
    private TextField ModifyProdMin;

    @FXML
    private TextField ModifyProdName;

    @FXML
    private TextField ModifyProdPrice;

    @FXML
    private TextField ModifyProdSearch;

    @FXML
    void onActionPartSearch() { //uses ModifyProdSearch
        filterParts(ModifyProdSearch.getText());
    }

    public ObservableList<Part> filterParts(String name) {

        if (!(Inventory.getFilteredParts().isEmpty())) //if Filtered Parts is Not Empty
            Inventory.getFilteredParts().clear(); //reset the filter
        for (Part part : Inventory.getAllParts()) {
            try {
                //checks for String
                if (part.getName().contains(name)) {
                    Inventory.getFilteredParts().add(part);
                    ModifyProdPartTable.setItems(Inventory.getFilteredParts());
                }
                if (ModifyProdSearch.getText().isEmpty()) {
                    ModifyProdPartTable.setItems(Inventory.getAllParts());
                }
                else if (part.getId() == Integer.parseInt(ModifyProdSearch.getText())) {
                    Inventory.getFilteredParts().add(part);
                    ModifyProdPartTable.setItems(Inventory.getFilteredParts());
                }
            }
            catch(NumberFormatException e){
                //ignore
            }
        }

        if(Inventory.getFilteredParts().isEmpty()) {
            System.out.println("Here!");
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "No matching Part was found");
            alert.showAndWait();
            ModifyProdPartTable.setItems(Inventory.getAllParts());
            ModifyProdSearch.setText("");
            return Inventory.getAllParts();
        }

        return Inventory.getFilteredParts();
    }

    public static ObservableList<Part> oldAssociatedParts = FXCollections.observableArrayList();

    public static Product sentProduct = null;

    public static void sendProduct(Product product) {
        sentProduct = product;
    }

    public static void sendAssociatedParts(ObservableList<Part> sendAssociatedParts) {
        oldAssociatedParts = sendAssociatedParts;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //This will get values for the Part Table in the Modify Product Form from Inventory.java
        ModifyProdPartTable.setItems(Inventory.getAllParts());
        ModProdPTPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        ModProdPTCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        ModProdPTInvLvl.setCellValueFactory(new PropertyValueFactory<>("stock"));
        ModProdPTPartName.setCellValueFactory(new PropertyValueFactory<>("name"));


        Product product = sentProduct;
        ModifyProdID.setText(String.valueOf(product.getId()));
        ModifyProdName.setText(product.getName());
        ModifyProdInv.setText(String.valueOf(product.getStock()));
        ModifyProdPrice.setText(String.valueOf(product.getPrice()));
        ModifyProdMin.setText(String.valueOf(product.getMin()));
        ModifyProdMax.setText(String.valueOf(product.getMax()));

        bottomListOfParts.addAll(oldAssociatedParts);
        ModifyProdAssocPartTable.setItems(bottomListOfParts);
        ModProdAssocPTPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        ModProdAssocPTPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        ModProdAssocPTCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        ModProdAssocPTInvLvl.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }
}
