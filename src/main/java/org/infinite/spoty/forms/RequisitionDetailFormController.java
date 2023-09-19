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

package org.infinite.spoty.forms;

import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import org.infinite.spoty.GlobalActions;
import org.infinite.spoty.components.notification.SimpleNotification;
import org.infinite.spoty.components.notification.SimpleNotificationHolder;
import org.infinite.spoty.components.notification.enums.NotificationDuration;
import org.infinite.spoty.components.notification.enums.NotificationVariants;
import org.infinite.spoty.database.models.Product;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.viewModels.ProductViewModel;
import org.infinite.spoty.viewModels.RequisitionDetailViewModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.Validators.requiredValidator;
import static org.infinite.spoty.values.SharedResources.tempIdProperty;

public class RequisitionDetailFormController implements Initializable {
    private static RequisitionDetailFormController instance;
    @FXML
    public MFXTextField requisitionDetailQnty;
    @FXML
    public MFXFilterComboBox<Product> requisitionDetailPdct;
    @FXML
    public MFXButton requisitionDetailSaveBtn;
    @FXML
    public MFXButton requisitionDetailCancelBtn;
    @FXML
    public MFXTextField requisitionDetailDescription;
    @FXML
    public Label requisitionDetailPdctValidationLabel;
    @FXML
    public Label requisitionDetailQntyValidationLabel;

    public static RequisitionDetailFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new RequisitionDetailFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Form input binding.
        requisitionDetailPdct
                .valueProperty()
                .bindBidirectional(RequisitionDetailViewModel.productProperty());
        requisitionDetailQnty
                .textProperty()
                .bindBidirectional(RequisitionDetailViewModel.quantityProperty());
        requisitionDetailDescription
                .textProperty()
                .bindBidirectional(RequisitionDetailViewModel.descriptionProperty());

        // Combo box Converter.
        StringConverter<Product> productVariantConverter =
                FunctionalStringConverter.to(
                        productDetail -> (productDetail == null) ? "" : productDetail.getName());

        // Combo box Filter Function.
        Function<String, Predicate<Product>> productVariantFilterFunction =
                searchStr ->
                        productDetail ->
                                StringUtils.containsIgnoreCase(
                                        productVariantConverter.toString(productDetail), searchStr);

        // Combo box properties.
        requisitionDetailPdct.setItems(ProductViewModel.getProducts());
        requisitionDetailPdct.setConverter(productVariantConverter);
        requisitionDetailPdct.setFilterFunction(productVariantFilterFunction);

        // Input validators.
        requiredValidator(
                requisitionDetailPdct,
                "Product is required.",
                requisitionDetailPdctValidationLabel,
                requisitionDetailSaveBtn);
        requiredValidator(
                requisitionDetailQnty,
                "Quantity is required.",
                requisitionDetailQntyValidationLabel,
                requisitionDetailSaveBtn);
        dialogOnActions();
    }

    private void dialogOnActions() {
        requisitionDetailCancelBtn.setOnAction(
                (event) -> {
                    closeDialog(event);
                    RequisitionDetailViewModel.resetProperties();
                    requisitionDetailPdct.clearSelection();
                    requisitionDetailPdctValidationLabel.setVisible(false);
                    requisitionDetailQntyValidationLabel.setVisible(false);
                });
        requisitionDetailSaveBtn.setOnAction(
                (event) -> {
                    SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

                    if (!requisitionDetailPdctValidationLabel.isVisible()
                            && !requisitionDetailQntyValidationLabel.isVisible()) {
                        if (tempIdProperty().get() > -1) {
                            GlobalActions.spotyThreadPool().execute(() -> {
                                try {
                                    RequisitionDetailViewModel.updateRequisitionDetail(
                                            RequisitionDetailViewModel.getId());
                                } catch (SQLException e) {
                                    SpotyLogger.writeToFile(e, this.getClass());
                                }
                            });

                            SimpleNotification notification =
                                    new SimpleNotification.NotificationBuilder("Product changed successfully")
                                            .duration(NotificationDuration.SHORT)
                                            .icon("fas-circle-check")
                                            .type(NotificationVariants.SUCCESS)
                                            .build();
                            notificationHolder.addNotification(notification);

                            requisitionDetailPdct.clearSelection();

                            closeDialog(event);
                            return;
                        }
                        RequisitionDetailViewModel.addRequisitionDetails();

                        SimpleNotification notification =
                                new SimpleNotification.NotificationBuilder("Product added successfully")
                                        .duration(NotificationDuration.SHORT)
                                        .icon("fas-circle-check")
                                        .type(NotificationVariants.SUCCESS)
                                        .build();
                        notificationHolder.addNotification(notification);

                        requisitionDetailPdct.clearSelection();

                        closeDialog(event);
                        return;
                    }
                    SimpleNotification notification =
                            new SimpleNotification.NotificationBuilder("Required fields missing")
                                    .duration(NotificationDuration.SHORT)
                                    .icon("fas-triangle-exclamation")
                                    .type(NotificationVariants.ERROR)
                                    .build();
                    notificationHolder.addNotification(notification);
                });
    }
}
