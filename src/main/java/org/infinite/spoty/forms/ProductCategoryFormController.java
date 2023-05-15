package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import org.infinite.spoty.models.Category;
import org.infinite.spoty.viewModels.ProductCategoryFormViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;

public class ProductCategoryFormController implements Initializable {
    @FXML
    public MFXTextField dialogCategoryCode;
    @FXML
    public MFXTextField dialogCategoryName;
    @FXML
    public MFXFilledButton dialogSaveBtn;
    @FXML
    public MFXOutlinedButton dialogCancelBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dialogCategoryCode.textProperty().addListener((observable, oldValue, newValue) -> dialogCategoryCode.setTrailingIcon(null));
        dialogCategoryName.textProperty().addListener((observable, oldValue, newValue) -> dialogCategoryName.setTrailingIcon(null));

        dialogCategoryCode.textProperty().bindBidirectional(ProductCategoryFormViewModel.codeProperty());
        dialogCategoryName.textProperty().bindBidirectional(ProductCategoryFormViewModel.nameProperty());

        dialogOnActions();
    }

    private void dialogOnActions() {
        dialogCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            dialogCategoryCode.setText("");
            dialogCategoryName.setText("");
            dialogCategoryCode.setTrailingIcon(null);
            dialogCategoryName.setTrailingIcon(null);
        });
        dialogSaveBtn.setOnAction((e) -> {
            Category category = new Category();
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

            if (dialogCategoryName.getText().length() == 0) {
                dialogCategoryName.setTrailingIcon(icon);
            }
            if (dialogCategoryCode.getText().length() == 0) {
                dialogCategoryCode.setTrailingIcon(icon);
            }
            if (dialogCategoryName.getText().length() > 0 && dialogCategoryCode.getText().length() > 0) {
                category.setCategoryCode(dialogCategoryCode.getText());
                category.setCategoryName(dialogCategoryName.getText());
                dialogCategoryCode.setText("");
                dialogCategoryName.setText("");

                closeDialog(e);
            }
        });
    }
}
