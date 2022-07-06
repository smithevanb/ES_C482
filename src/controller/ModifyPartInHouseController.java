package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;

/**
 * RUNTIME ERROR: Threw a Number Format Exception when any field was left blank
 *
 * FUTURE ENHANCEMENT: Being able to switch Part types between In-House and Outsourced
 */


public class ModifyPartInHouseController {

    Stage stage;
    Parent scene;

    @FXML
    private Button ModifyPartCancel;

    @FXML
    private TextField ModifyPartID;

    @FXML
    private TextField ModifyPartInv;

    @FXML
    private TextField ModifyPartMachineID;

    @FXML
    private TextField ModifyPartMax;

    @FXML
    private TextField ModifyPartMin;

    @FXML
    private TextField ModifyPartName;

    @FXML
    private TextField ModifyPartPrice;

    @FXML
    private RadioButton ModifyPartRadioInHouse;

    @FXML
    private RadioButton ModifyPartRadioOutsourced;

    @FXML
    private Button ModifyPartSave;

    @FXML
    private ToggleGroup ModifyPartTG;

    @FXML
    void onActionDisplayMainForm(ActionEvent event) throws IOException {

        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionInHouseClicked(ActionEvent event) {
        //currenly disabled
    }

    @FXML
    void onActionOutsourcedClicked(ActionEvent event) {
        //currently disabled
    }

    @FXML
    void onActionSavePart(ActionEvent event) throws IOException {

        try{
            int id = Integer.parseInt(ModifyPartID.getText());
            String name = ModifyPartName.getText();
            int min = Integer.parseInt(ModifyPartMin.getText());
            int max = Integer.parseInt(ModifyPartMax.getText());
            double price = Double.parseDouble(ModifyPartPrice.getText());
            int inv = Integer.parseInt(ModifyPartInv.getText());
            int machineId = Integer.parseInt(ModifyPartMachineID.getText());

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

                int index = -1;
                for(Part part : Inventory.getAllParts()) {
                    index++;
                    if(part.getId() == id) {
                        Inventory.updatePart(index, new InHouse(id, name, price, inv, min, max, machineId));
                    }
                }

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
        }

    }

    @FXML
    public void sendPart(Part part) {
        ModifyPartID.setText(String.valueOf(part.getId()));
        ModifyPartName.setText(part.getName());
        ModifyPartInv.setText(String.valueOf(part.getStock()));
        ModifyPartPrice.setText(String.valueOf(part.getPrice()));
        ModifyPartMin.setText(String.valueOf(part.getMin()));
        ModifyPartMax.setText(String.valueOf(part.getMax()));

        if(part instanceof InHouse)
            ModifyPartMachineID.setText(String.valueOf(((InHouse) part).getMachineId()));
    }

    @FXML
    void initialize() {
        /**
         * Receive Part data from Main Form Part Table selected Part, that was selected before User clicked "Modify
         * Part" Button to arrive at this page
         */


    }

}
