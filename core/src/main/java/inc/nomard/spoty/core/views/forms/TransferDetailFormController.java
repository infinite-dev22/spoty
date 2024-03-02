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

import inc.nomard.spoty.core.components.notification.SimpleNotification;
import inc.nomard.spoty.core.components.notification.SimpleNotificationHolder;
import inc.nomard.spoty.core.components.notification.enums.NotificationDuration;
import inc.nomard.spoty.core.components.notification.enums.NotificationVariants;
import inc.nomard.spoty.core.viewModels.ProductViewModel;
import inc.nomard.spoty.core.viewModels.transfers.TransferDetailViewModel;
import inc.nomard.spoty.network_bridge.dtos.Product;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.SpotyThreader;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

import static inc.nomard.spoty.core.GlobalActions.closeDialog;
import static inc.nomard.spoty.core.Validators.requiredValidator;
import static inc.nomard.spoty.core.values.SharedResources.tempIdProperty;

public class TransferDetailFormController implements Initializable {
    private static TransferDetailFormController instance;
    @FXML
    public MFXTextField transferDetailQnty;
    @FXML
    public MFXFilterComboBox<Product> transferDetailPdct;
    @FXML
    public MFXButton transferDetailSaveBtn;
    @FXML
    public MFXButton transferDetailCancelBtn;
    @FXML
    public MFXTextField transferDetailDescription;
    @FXML
    public Label transferDetailQntyValidationLabel;
    @FXML
    public Label transferDetailPdctValidationLabel;

    public static TransferDetailFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new TransferDetailFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Form input binding.
        transferDetailPdct.valueProperty().bindBidirectional(TransferDetailViewModel.productProperty());
        transferDetailQnty.textProperty().bindBidirectional(TransferDetailViewModel.quantityProperty());
        transferDetailDescription
                .textProperty()
                .bindBidirectional(TransferDetailViewModel.descriptionProperty());

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
        transferDetailPdct.setItems(ProductViewModel.getProducts());
        transferDetailPdct.setConverter(productVariantConverter);
        transferDetailPdct.setFilterFunction(productVariantFilterFunction);

        // Input validators.
        requiredValidator(
                transferDetailPdct,
                "Product is required.",
                transferDetailPdctValidationLabel,
                transferDetailSaveBtn);
        requiredValidator(
                transferDetailQnty,
                "Quantity is required.",
                transferDetailQntyValidationLabel,
                transferDetailSaveBtn);

        dialogOnActions();
    }

    private void dialogOnActions() {
        transferDetailCancelBtn.setOnAction(
                (event) -> {
                    closeDialog(event);
                    TransferDetailViewModel.resetProperties();
                    transferDetailPdctValidationLabel.setVisible(false);
                    transferDetailQntyValidationLabel.setVisible(false);
                });
        transferDetailSaveBtn.setOnAction(
                (event) -> {
                    SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

                    if (!transferDetailPdctValidationLabel.isVisible()
                            && !transferDetailQntyValidationLabel.isVisible()) {
                        if (tempIdProperty().get() > -1) {
                            SpotyThreader.spotyThreadPool(() -> {
                                try {
                                    TransferDetailViewModel.updateTransferDetail();
                                } catch (Exception e) {
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

                            transferDetailPdct.clearSelection();

                            closeDialog(event);
                            return;
                        }
                        TransferDetailViewModel.addTransferDetails();

                        SimpleNotification notification =
                                new SimpleNotification.NotificationBuilder("Product added successfully")
                                        .duration(NotificationDuration.SHORT)
                                        .icon("fas-circle-check")
                                        .type(NotificationVariants.SUCCESS)
                                        .build();
                        notificationHolder.addNotification(notification);

                        transferDetailPdct.clearSelection();

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
