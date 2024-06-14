/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

package inc.nomard.spoty.core.views.forms;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import static inc.nomard.spoty.core.values.SharedResources.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.stock_ins.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.utils.*;
import io.github.palexdev.materialfx.utils.others.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.net.*;
import java.util.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class StockInDetailFormController implements Initializable {
    private static StockInDetailFormController instance;
    private final Stage stage;
    @FXML
    private MFXTextField quantity, description;
    @FXML
    private MFXFilterComboBox<Product> product;
    @FXML
    private MFXButton saveBtn, cancelBtn;
    @FXML
    private Label productValidationLabel, quantityValidationLabel;
    private List<Constraint> productConstraints, quantityConstraints;

    public StockInDetailFormController(Stage stage) {
        this.stage = stage;
    }

    public static StockInDetailFormController getInstance(Stage stage) {
        if (instance == null) {
            synchronized (StockInDetailFormController.class) {
                instance = new StockInDetailFormController(stage);
            }
        }
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindFormInputs();
        setupProductComboBox();
        requiredValidator();
        setupDialogActions();
    }

    private void bindFormInputs() {
        product.valueProperty().bindBidirectional(StockInDetailViewModel.productProperty());
        quantity.textProperty().bindBidirectional(StockInDetailViewModel.quantityProperty());
        description.textProperty().bindBidirectional(StockInDetailViewModel.descriptionProperty());
    }

    private void setupProductComboBox() {
        StringConverter<Product> productConverter = FunctionalStringConverter.to(
                productDetail -> productDetail == null ? "" : productDetail.getName());
        product.setConverter(productConverter);
        product.setFilterFunction(searchStr ->
                productDetail -> StringUtils.containsIgnoreCase(productConverter.toString(productDetail), searchStr));

        if (ProductViewModel.getProducts().isEmpty()) {
            ProductViewModel.getProducts().addListener((ListChangeListener<Product>) c -> product.setItems(ProductViewModel.getProducts()));
        } else {
            product.itemsProperty().bindBidirectional(ProductViewModel.productsProperty());
        }
    }

    private void setupDialogActions() {
        cancelBtn.setOnAction(this::resetForm);
        saveBtn.setOnAction(this::handleSaveAction);
    }

    private void resetForm(ActionEvent event) {
        closeDialog(event);
        StockInDetailViewModel.resetProperties();
        clearSelections();
        hideValidationLabels();
    }

    private void clearSelections() {
        product.clearSelection();
        product.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        quantity.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
    }

    private void hideValidationLabels() {
        productValidationLabel.setVisible(false);
        quantityValidationLabel.setVisible(false);
        productValidationLabel.setManaged(false);
        quantityValidationLabel.setManaged(false);
    }

    private void handleSaveAction(ActionEvent event) {
        validateInputs();
        if (productConstraints.isEmpty() && quantityConstraints.isEmpty()) {
            if (tempIdProperty().get() > -1) {
                StockInDetailViewModel.updateStockInDetail();
                successMessage("Product changed successfully");
            } else {
                StockInDetailViewModel.addStockInDetails();
                successMessage("Product added successfully");
            }
            resetForm(event);
            closeDialog(event);
        }
    }

    private void validateInputs() {
        productConstraints = product.validate();
        quantityConstraints = quantity.validate();

        if (!productConstraints.isEmpty()) {
            showValidationLabel(productValidationLabel, productConstraints.getFirst().getMessage(), product);
        }

        if (!quantityConstraints.isEmpty()) {
            showValidationLabel(quantityValidationLabel, quantityConstraints.getFirst().getMessage(), quantity);
        }
    }

    private void showValidationLabel(Label label, String message, Control control) {
        label.setManaged(true);
        label.setVisible(true);
        label.setText(message);
        control.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        MFXStageDialog dialog = (MFXStageDialog) control.getScene().getWindow();
        dialog.sizeToScene();
    }

    public void requiredValidator() {
        setupValidator(product, "Product is required");
        setupValidator(quantity, "Quantity is required");
    }

    private void setupValidator(MFXTextField control, String message) {
        Constraint constraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage(message)
                .setCondition(control.textProperty().length().greaterThan(0))
                .get();
        control.getValidator().constraint(constraint);

        control.getValidator().validProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                hideValidationLabel(control);
            }
        });
    }

    private void hideValidationLabel(Control control) {
        if (control.equals(product)) {
            productValidationLabel.setManaged(false);
            productValidationLabel.setVisible(false);
        } else if (control.equals(quantity)) {
            quantityValidationLabel.setManaged(false);
            quantityValidationLabel.setVisible(false);
        }
        control.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
    }

    private void successMessage(String message) {
        displayNotification(message, MessageVariants.SUCCESS, "fas-circle-check");
    }

    private void displayNotification(String message, MessageVariants type, String icon) {
        SpotyMessage notification = new SpotyMessage.MessageBuilder(message)
                .duration(MessageDuration.SHORT)
                .icon(icon)
                .type(type)
                .height(60)
                .build();
        AnchorPane.setTopAnchor(notification, 5.0);
        AnchorPane.setRightAnchor(notification, 5.0);

        var in = Animations.slideInDown(notification, Duration.millis(250));
        if (!BaseController.getInstance(stage).morphPane.getChildren().contains(notification)) {
            BaseController.getInstance(stage).morphPane.getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent -> SpotyMessage.delay(notification, stage));
        }
    }
}
