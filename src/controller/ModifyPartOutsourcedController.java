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
import model.Outsourced;
import model.Part;

import java.io.IOException;

/**
 * Currently this class, and its corresponding ModifyPartInHouseController allow the User to Switch between them by
 * clicking the corresponding Radio buttons. Not sure if this should be included, or disallowed by design. For Add
 * Part it makes sense, but should Users be able to modify an existing Part's In-House or Outsourced Status so
 * easily? What happens to the pre-existing MachineID or CompanyName? A dialog box appearing and warning Users of
 * this should probably be included.
 *
 *
 * RUNTIME ERROR: Threw a Number Format Exception when any field was left blank
 *
 * FUTURE ENHANCEMENT: Being able to switch Part types between In-House and Outsourced
 *
 */

public class ModifyPartOutsourcedController {



    Stage stage;
    Parent scene;

    @FXML
    private Button ModifyPartCancel;

    @FXML
    private TextField ModifyPartCompanyName;

    @FXML
    private TextField ModifyPartID;

    @FXML
    private TextField ModifyPartInv;

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
    void onActionSavePart(ActionEvent event) throws IOException {

        //NEEDS EXCEPTION HANDLING FOR EMPTY FIELDS/ETC

        try{
            int id = Integer.parseInt(ModifyPartID.getText());
            String name = ModifyPartName.getText();
            int min = Integer.parseInt(ModifyPartMin.getText());
            int max = Integer.parseInt(ModifyPartMax.getText());
            double price = Double.parseDouble(ModifyPartPrice.getText());
            int inv = Integer.parseInt(ModifyPartInv.getText());
            String companyName = ModifyPartCompanyName.getText();


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
                        Inventory.updatePart(index, new Outsourced(id, name, price, inv, min, max, companyName));
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
    void onActionDisplayMainForm(ActionEvent event) throws IOException {

        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionInHouseClicked(ActionEvent event) throws IOException {

        stage = (Stage) ((RadioButton)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ModifyPartInHouse.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionOutsourcedClicked(ActionEvent actionEvent) {

    }

    @FXML
    public void sendPart(Part part) {
        ModifyPartID.setText(String.valueOf(part.getId()));
        ModifyPartName.setText(part.getName());
        ModifyPartInv.setText(String.valueOf(part.getStock()));
        ModifyPartPrice.setText(String.valueOf(part.getPrice()));
        ModifyPartMin.setText(String.valueOf(part.getMin()));
        ModifyPartMax.setText(String.valueOf(part.getMax()));

        if(part instanceof Outsourced)
            ModifyPartCompanyName.setText(String.valueOf(((Outsourced) part).getCompanyName()));
    }

}