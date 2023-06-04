package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.converter.NumberStringConverter;
import org.infinite.spoty.viewModels.ProductCategoryViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.viewModels.ProductCategoryViewModel.*;

public class ProductCategoryFormController implements Initializable {
    public MFXTextField dialogCategoryID = new MFXTextField();
    @FXML
    public MFXTextField dialogCategoryCode;
    @FXML
    public MFXTextField dialogCategoryName;
    @FXML
    public MFXFilledButton dialogSaveBtn;
    @FXML
    public MFXOutlinedButton dialogCancelBtn;
    @FXML
    public static Label formTitle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dialogCategoryCode.textProperty().addListener((observable, oldValue, newValue) -> dialogCategoryCode.setTrailingIcon(null));
        dialogCategoryName.textProperty().addListener((observable, oldValue, newValue) -> dialogCategoryName.setTrailingIcon(null));

        dialogCategoryID.textProperty().bindBidirectional(ProductCategoryViewModel.idProperty(), new NumberStringConverter());
        dialogCategoryCode.textProperty().bindBidirectional(ProductCategoryViewModel.codeProperty());
        dialogCategoryName.textProperty().bindBidirectional(ProductCategoryViewModel.nameProperty());

        dialogOnActions();
    }

    private void dialogOnActions() {
        dialogCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            clearProductCategoryData();
            dialogCategoryCode.setTrailingIcon(null);
            dialogCategoryName.setTrailingIcon(null);
        });
        dialogSaveBtn.setOnAction((e) -> {
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

            if (dialogCategoryName.getText().length() == 0) {
                dialogCategoryName.setTrailingIcon(icon);
            }
            if (dialogCategoryCode.getText().length() == 0) {
                dialogCategoryCode.setTrailingIcon(icon);
            }
            if (dialogCategoryName.getText().length() > 0 && dialogCategoryCode.getText().length() > 0) {
                if (Integer.parseInt(dialogCategoryID.getText()) > 0)
                    updateItem(Integer.parseInt(dialogCategoryID.getText()));
                else
                    saveProductCategory();
                closeDialog(e);
            }
        });
    }
}
