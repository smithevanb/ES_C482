package controller;

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
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * RUNTIME ERROR: Threw a NUMBER FORMAT EXCEPTION within filterProducts() and filterParts(), until exception handling
 * was added to accommodate for both alphabetical input as well as numerical input.
 *
 * FUTURE ENHANCEMENT: Adding functionality to the Menu Bar would improve this page. Specifically, being able to use
 * File > Open to allow Users to import a .csv file, or other format file, in order to Add multiple Parts or Products
 * all at once, rather than having to add them one by one.
 */

public class MainFormController implements Initializable {

    Stage stage;
    Parent scene;

    /**
     * Changes scene to Add Part page, defaults to In-House
     */
    @FXML
    void onActionAddPart(ActionEvent event) throws IOException {

        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddPartInHouse.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Changes scene to Add Product page
     */
    @FXML
    void onActionAddProd(ActionEvent event) throws IOException {

        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Changes scene to Modify Part page
     */
    @FXML
    void onActionModifyPart(ActionEvent event) throws IOException {

        try{
            FXMLLoader loader = new FXMLLoader();

            if(MFPartTable.getSelectionModel().getSelectedItem() instanceof InHouse) {
                loader.setLocation(getClass().getResource("/view/ModifyPartInHouse.fxml")); //Opens Modify Part In-House
                loader.load();

                ModifyPartInHouseController MPartInController = loader.getController();
                MPartInController.sendPart(MFPartTable.getSelectionModel().getSelectedItem());
            }

            else if(MFPartTable.getSelectionModel().getSelectedItem() instanceof Outsourced) {
                loader.setLocation(getClass().getResource("/view/ModifyPartOutsourced.fxml")); //Opens Modify Part Outsourced
                loader.load();

                ModifyPartOutsourcedController MPartOutController = loader.getController();
                MPartOutController.sendPart(MFPartTable.getSelectionModel().getSelectedItem());
            }

            stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch(NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please select a Part to Modify");
            alert.showAndWait();
        }
    }

    /**
     * Changes scene to Modify Product page
     */
    @FXML
    void onActionModifyProd(ActionEvent event) throws IOException {

        Product selectedProduct = MFProdsTable.getSelectionModel().getSelectedItem();
        if(selectedProduct == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please select a Product to Modify");
            alert.showAndWait();
        }
        if(selectedProduct != null){
            ModifyProductController.sendProduct(selectedProduct);
            ModifyProductController.sendAssociatedParts(selectedProduct.getAllAssociatedParts());
            try {

                Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyProduct.fxml"));

                stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            }
            catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Please select a Product to Modify");
                alert.showAndWait();
            }
        }
    }

    /**
     * The Delete button under the Parts TableView deletes the selected part from the Parts TableView
     * or displays a descriptive error message in the UI or in a dialog box if a part is not deleted.
     */
    @FXML
    void onActionDeletePart(ActionEvent event) {

        if(MFPartTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please select a Part to Delete");
            alert.showAndWait();
        }
        if(MFPartTable.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "You are about to delete a Part. This cannot be undone. Press 'OK' to continue");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deletePart(MFPartTable.getSelectionModel().getSelectedItem());
                MFPartTable.setItems(Inventory.getAllParts()); //update the table
            }
        }
    }

    /**
     * The Delete button under the Products TableView deletes the selected product (if appropriate) from the Products TableView
     * or displays a descriptive error message in the UI or in a dialog box if a product is not deleted.
     */
    @FXML
    void onActionDeleteProd(ActionEvent event) {

        if(MFProdsTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please select a Product to Delete");
            alert.showAndWait();
        }
        if(!MFProdsTable.getSelectionModel().getSelectedItem().getAllAssociatedParts().isEmpty()) { //if the Product has Parts
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "You cannot Delete a Product that has Parts Associated with it.");
            alert.showAndWait();
        }
        if(MFProdsTable.getSelectionModel().getSelectedItem() != null && MFProdsTable.getSelectionModel().getSelectedItem().getAllAssociatedParts().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "You are about to delete a Product. This cannot be undone. Press 'OK' to continue");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deleteProduct(MFProdsTable.getSelectionModel().getSelectedItem());
                MFProdsTable.setItems(Inventory.getAllProducts()); //update the table
            }
        }
    }


    /**
     * This button closes the application
     */
    @FXML
    void onActionExitMainForm(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private TableView<Part> MFPartTable;

    @FXML
    private TableView<Product> MFProdsTable;

    @FXML
    private TableColumn<Part, Double> MFPartsTableCost;

    @FXML
    private TableColumn<Part, Integer> MFPartsTableInvLvl;

    @FXML
    private TableColumn<Part, Integer> MFPartsTablePartID;

    @FXML
    private TableColumn<Part, String> MFPartsTablePartName;

    @FXML
    private TableColumn<Product, Double> MFProdsTableCost;

    @FXML
    private TableColumn<Product, Integer> MFProdsTableInvLvl;

    @FXML
    private TableColumn<Product, Integer> MFProdsTableProdID;

    @FXML
    private TableColumn<Product, String> MFProdsTableProdName;

    @FXML
    private Button MainFormExit;

    @FXML
    private Button MainFormPartsAdd;

    @FXML
    private Button MainFormPartsDelete;

    @FXML
    private Button MainFormPartsModify;

    @FXML
    private TextField MainFormPartsSearch;

    @FXML
    private Button MainFormProdAdd;

    @FXML
    private Button MainFormProdDelete;

    @FXML
    private Button ProductsSearchButton;

    @FXML
    private Button MainFormProdModify;

    @FXML
    private TextField MainFormProdSearch;

    @FXML
    void onActionSearchProductsText() { //uses TextField MainFormProdSearch
        filterProducts(MainFormProdSearch.getText());
    }

    @FXML
    void onActionSearchProducts() { //uses Button ProductsSearchButton
        filterProducts(MainFormProdSearch.getText());
    }

    @FXML
    void onActionSearchPartsText(ActionEvent actionEvent) {
        filterParts(MainFormPartsSearch.getText());
    }

    @FXML
     void onActionSearchParts(ActionEvent actionEvent) {
        filterParts(MainFormPartsSearch.getText());
    }

    //CAUSED NUMBER FORMAT EXCEPT UNTIL ADDED "ELSE IF" BEFORE CHECKING FOR ID
    public ObservableList<Product> filterProducts(String name) {

        if(!(Inventory.getFilteredProducts().isEmpty()))
            Inventory.getFilteredProducts().clear();
        for (Product product : Inventory.getAllProducts()) {
            try {

                    if (product.getName().contains(name)) {
                        Inventory.getFilteredProducts().add(product);
                        MFProdsTable.setItems(Inventory.getFilteredProducts());
                    }
                    if (MainFormProdSearch.getText().isEmpty()) {
                        MFProdsTable.setItems(Inventory.getAllProducts());
                    }

                    else if (product.getId() == Integer.parseInt(MainFormProdSearch.getText())) {
                        Inventory.getFilteredProducts().add(product);
                        MFProdsTable.setItems(Inventory.getFilteredProducts());
                    }
            }
            catch(NumberFormatException e) {
                //ignore
            }
        }

        if(Inventory.getFilteredProducts().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "No matching Product was found");
            alert.showAndWait();
            MFProdsTable.setItems(Inventory.getAllProducts());
            MainFormProdSearch.setText("");
            return Inventory.getAllProducts();
        }

        return Inventory.getFilteredProducts();
    }

    //ALSO THREW NUMBER FORMAT EXCEPTION UNTIL ADDED "ELSE IF" BEFORE SEARCH FOR ID
    public ObservableList<Part> filterParts(String name) {

        if(!(Inventory.getFilteredParts().isEmpty())) //if Filtered Parts is Not Empty
            Inventory.getFilteredParts().clear(); //reset the filter
        for(Part part : Inventory.getAllParts()){
            try {
                //checks for String
                    if(part.getName().contains(name)) {
                        Inventory.getFilteredParts().add(part);
                        MFPartTable.setItems(Inventory.getFilteredParts());
                    }

                    if(MainFormPartsSearch.getText().isEmpty()) {
                        MFPartTable.setItems(Inventory.getAllParts());
                    }

                    else if(part.getId() == Integer.parseInt(MainFormPartsSearch.getText())) {
                        Inventory.getFilteredParts().add(part);
                        MFPartTable.setItems(Inventory.getFilteredParts());
                    }
            }
            catch (NumberFormatException e) {
                //ignore
            }
        }

        if(Inventory.getFilteredParts().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "No matching Part was found");
            alert.showAndWait();
            MFPartTable.setItems(Inventory.getAllParts());
            MainFormPartsSearch.setText("");
            return Inventory.getAllParts();
        }

        return Inventory.getFilteredParts();
    }

    /**
     * Initialize the controller class//
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //This will get values for the Part Table in the Main Form from Inventory
        MFPartTable.setItems(Inventory.getAllParts());
        MFPartTable.setItems(filterParts(MainFormPartsSearch.getText()));
        MFPartsTablePartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        MFPartsTableCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        MFPartsTableInvLvl.setCellValueFactory(new PropertyValueFactory<>("stock"));
        MFPartsTablePartName.setCellValueFactory(new PropertyValueFactory<>("name"));

        //This will get values for the Product Table in the Main Form from Inventory
        MFProdsTable.setItems(Inventory.getAllProducts());
        MFProdsTable.setItems(filterProducts(MainFormProdSearch.getText()));
        MFProdsTableProdID.setCellValueFactory(new PropertyValueFactory<>("id"));
        MFProdsTableCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        MFProdsTableInvLvl.setCellValueFactory(new PropertyValueFactory<>("stock"));
        MFProdsTableProdName.setCellValueFactory(new PropertyValueFactory<>("name"));

    }
}
