package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.viewModels.ProductDetailViewModel;
import org.infinite.spoty.viewModels.TransferDetailViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;

public class TransferDetailFormController implements Initializable {
    @FXML
    public MFXTextField transferDetailQnty;
    @FXML
    public MFXFilterComboBox<ProductDetail> transferDetailPdct;
    @FXML
    public MFXFilledButton transferDetailSaveBtn;
    @FXML
    public MFXOutlinedButton transferDetailCancelBtn;
    @FXML
    public Label transferDetailTitle;
    @FXML
    public MFXTextField transferDetailDescription;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Form input listeners.
        transferDetailPdct.textProperty().addListener((observable, oldValue, newValue) -> transferDetailPdct.setLeadingIcon(null));
        transferDetailQnty.textProperty().addListener((observable, oldValue, newValue) -> transferDetailQnty.setTrailingIcon(null));
        transferDetailDescription.textProperty().addListener((observable, oldValue, newValue) -> transferDetailDescription.setLeadingIcon(null));
        // Form input binding.
        transferDetailPdct.valueProperty().bindBidirectional(TransferDetailViewModel.productProperty());
        transferDetailQnty.textProperty().bindBidirectional(TransferDetailViewModel.quantityProperty());
        transferDetailDescription.textProperty().bindBidirectional(TransferDetailViewModel.descriptionProperty());
        // Combo box properties.
        transferDetailPdct.setItems(ProductDetailViewModel.purchaseDetailsList);
        transferDetailPdct.setConverter(new StringConverter<>() {
            @Override
            public String toString(ProductDetail object) {
                if (object != null)
                    return object.getProduct().getName() + " " + object.getName();
                else
                    return null;
            }

            @Override
            public ProductDetail fromString(String string) {
                return null;
            }
        });
        dialogOnActions();
    }

    private void dialogOnActions() {
        transferDetailCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            TransferDetailViewModel.resetProperties();
            transferDetailPdct.setLeadingIcon(null);
            transferDetailQnty.setTrailingIcon(null);
            transferDetailDescription.setLeadingIcon(null);
        });
        transferDetailSaveBtn.setOnAction((e) -> {
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);
            if (transferDetailPdct.getText().length() == 0) {
                transferDetailPdct.setLeadingIcon(icon);
            }
            if (transferDetailQnty.getText().length() == 0) {
                transferDetailQnty.setTrailingIcon(icon);
            }
            if (transferDetailDescription.getText().length() == 0) {
                transferDetailDescription.setLeadingIcon(icon);
            }
            if (transferDetailQnty.getText().length() > 0
                    && transferDetailPdct.getText().length() > 0
                    && transferDetailDescription.getText().length() > 0) {
                TransferDetailViewModel.addTransferDetails();
                TransferDetailViewModel.resetProperties();
                closeDialog(e);
            }
        });
    }
}
