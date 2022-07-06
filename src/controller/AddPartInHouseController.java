package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This Form, which looks identical to AddPart, except that "In-House radio button
 * is clicked, has almost the exact same function as AddPart, and AddPartOutsourced.
 * Clicking on the "Outsourced" radio button should send the user to AddPartOutsourced
 * Form, and vice versa for that form to this one.
 */

public class AddPartInHouseController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    void onActionDisplayMainForm(ActionEvent event) throws IOException {
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionInHouseClicked(ActionEvent event) throws IOException {

        //Opens the "AddPartInHouse" Form
        stage = (Stage) ((RadioButton)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddPartInHouse.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionOutsourcedClicked(ActionEvent event) throws IOException {

        //Opens the "AddPartOutsourced" Form
        stage = (Stage) ((RadioButton)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddPartOutsourced.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionSavePart(ActionEvent event) throws IOException {

        /**
         * Reading text input data from the user to create a new In-House Part.
         * I don't know if this method of creating a PartID, by taking the length of the
         * AllParts List, and adding one, will create a problem or shared ID.
         *
         * A FUTURE ENHANCEMENT could be creating a dialog box when not all of the
         * Text Fields are filled out, giving the user an error message on this. As
         * it is now, the form simply refuses to Save the new Part, which is sort
         * of good enough.
         */
        try {
            Inventory.partsIdCounter+= 2;
            int id = Inventory.partsIdCounter;
            String name = AddPartName.getText();
            int stock = Integer.parseInt(AddPartInv.getText());
            double price = Double.parseDouble(AddPartPrice.getText());
            int min = Integer.parseInt(AddPartMin.getText());
            int max = Integer.parseInt(AddPartMax.getText());
            int machineId = Integer.parseInt(AddPartMachineID.getText());

            if(max < min){
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Max must be greater than min");
                alert.showAndWait();
            }
            if(stock > max || stock < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Inventory Level must be greater than or equal to Min, and less than or equal to Max");
                alert.showAndWait();
            }
            if(max > min && stock <= max && stock >= min) {
                Inventory.addPart(new InHouse(id, name, price, stock, min, max, machineId));

                //Returns to Main Form after Saving the new product
                stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }

        }

        catch (NumberFormatException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please enter valid values into Text Fields");
            alert.showAndWait();
            //System.out.println("Please enter valid values");
        }


    }

    @FXML
    private Button AddPartCancel;

    @FXML
    private TextField AddPartID;

    @FXML
    private TextField AddPartInv;

    @FXML
    private TextField AddPartMachineID;

    @FXML
    private TextField AddPartMax;

    @FXML
    private TextField AddPartMin;

    @FXML
    private TextField AddPartName;

    @FXML
    private TextField AddPartPrice;

    @FXML
    private RadioButton AddPartRadioInHouse;

    @FXML
    private RadioButton AddPartRadioOutsourced;

    @FXML
    private Button AddPartSave;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
