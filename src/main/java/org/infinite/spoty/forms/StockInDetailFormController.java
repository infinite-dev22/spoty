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
import org.infinite.spoty.viewModels.StockInDetailViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;

public class StockInDetailFormController implements Initializable {
    @FXML
    public MFXTextField stockInDetailQnty;
    @FXML
    public MFXFilterComboBox<ProductDetail> stockInDetailPdct;
    @FXML
    public MFXFilledButton stockInDetailSaveBtn;
    @FXML
    public MFXOutlinedButton stockInDetailCancelBtn;
    @FXML
    public Label stockInDetailTitle;
    @FXML
    public MFXTextField stockInDetailDescription;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Form input listeners.
        stockInDetailPdct.textProperty().addListener((observable, oldValue, newValue) -> stockInDetailPdct.setLeadingIcon(null));
        stockInDetailQnty.textProperty().addListener((observable, oldValue, newValue) -> stockInDetailQnty.setTrailingIcon(null));
        stockInDetailDescription.textProperty().addListener((observable, oldValue, newValue) -> stockInDetailDescription.setLeadingIcon(null));
        // Form input binding.
        stockInDetailPdct.valueProperty().bindBidirectional(StockInDetailViewModel.productProperty());
        stockInDetailQnty.textProperty().bindBidirectional(StockInDetailViewModel.quantityProperty());
        stockInDetailDescription.textProperty().bindBidirectional(StockInDetailViewModel.descriptionProperty());
        // Combo box properties.
        stockInDetailPdct.setItems(ProductDetailViewModel.purchaseDetailsList);
        stockInDetailPdct.setConverter(new StringConverter<>() {
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
        stockInDetailCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            StockInDetailViewModel.resetProperties();
            stockInDetailPdct.setLeadingIcon(null);
            stockInDetailQnty.setTrailingIcon(null);
            stockInDetailDescription.setLeadingIcon(null);
        });
        stockInDetailSaveBtn.setOnAction((e) -> {
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);
            if (stockInDetailPdct.getText().length() == 0) {
                stockInDetailPdct.setLeadingIcon(icon);
            }
            if (stockInDetailQnty.getText().length() == 0) {
                stockInDetailQnty.setTrailingIcon(icon);
            }
            if (stockInDetailDescription.getText().length() == 0) {
                stockInDetailDescription.setLeadingIcon(icon);
            }
            if (stockInDetailQnty.getText().length() > 0
                    && stockInDetailPdct.getText().length() > 0
                    && stockInDetailDescription.getText().length() > 0) {
                StockInDetailViewModel.addStockInDetails();
                StockInDetailViewModel.resetProperties();
                closeDialog(e);
            }
        });
    }
}
