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

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.Validators.requiredValidator;
import static org.infinite.spoty.values.SharedResources.tempIdProperty;

import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.infinite.spoty.components.notification.SimpleNotification;
import org.infinite.spoty.components.notification.SimpleNotificationHolder;
import org.infinite.spoty.components.notification.enums.NotificationDuration;
import org.infinite.spoty.components.notification.enums.NotificationVariants;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.viewModels.ProductDetailViewModel;
import org.infinite.spoty.viewModels.QuotationDetailViewModel;

public class QuotationDetailFormController implements Initializable {
  private static QuotationDetailFormController instance;
  private final Stage stage;
  @FXML public MFXTextField quotationProductQnty;
  @FXML public MFXFilterComboBox<ProductDetail> quotationProductPdct;
  @FXML public MFXTextField quotationProductsOrderTax;
  @FXML public MFXTextField quotationProductsDiscount;
  @FXML public MFXButton quotationProductsSaveBtn;
  @FXML public MFXButton quotationProductsCancelBtn;
  @FXML public Label quotationProductsTitle;
  @FXML public Label quotationProductPdctValidationLabel;
  @FXML public Label quotationProductQntyValidationLabel;

  private QuotationDetailFormController(Stage stage) {
    this.stage = stage;
  }

  public static QuotationDetailFormController getInstance(Stage stage) {
    if (Objects.equals(instance, null)) instance = new QuotationDetailFormController(stage);
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Bind form input values.
    quotationProductQnty
        .textProperty()
        .bindBidirectional(QuotationDetailViewModel.quantityProperty());
    quotationProductPdct
        .valueProperty()
        .bindBidirectional(QuotationDetailViewModel.productProperty());
    quotationProductsOrderTax
        .textProperty()
        .bindBidirectional(QuotationDetailViewModel.taxProperty());
    quotationProductsDiscount
        .textProperty()
        .bindBidirectional(QuotationDetailViewModel.discountProperty());
    // Combo box properties.
    quotationProductPdct.setItems(ProductDetailViewModel.getProductDetails());
    quotationProductPdct.setConverter(
        new StringConverter<>() {
          @Override
          public String toString(ProductDetail object) {
            return object != null
                ? object.getProduct().getName()
                    + " "
                    + (object.getUnit() != null
                        ? (object.getName().isEmpty() ? "" : object.getName())
                            + " "
                            + object.getUnit().getName()
                        : object.getName())
                : "No products";
          }

          @Override
          public ProductDetail fromString(String string) {
            return null;
          }
        });
    // Input validators.
    requiredValidator(
        quotationProductPdct, "Product is required.", quotationProductPdctValidationLabel);
    requiredValidator(
        quotationProductQnty, "Quantity is required.", quotationProductQntyValidationLabel);
    dialogOnActions();
  }

  private void dialogOnActions() {
    quotationProductsCancelBtn.setOnAction(
        (e) -> {
          closeDialog(e);
          QuotationDetailViewModel.resetProperties();
          quotationProductPdct.clearSelection();
          quotationProductPdctValidationLabel.setVisible(false);
          quotationProductQntyValidationLabel.setVisible(false);
        });
    quotationProductsSaveBtn.setOnAction(
        (e) -> {
          SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

          if (!quotationProductPdctValidationLabel.isVisible()
              && !quotationProductQntyValidationLabel.isVisible()) {
            if (tempIdProperty().get() > -1) {
              QuotationDetailViewModel.updateQuotationDetail(QuotationDetailViewModel.getId());

              SimpleNotification notification =
                  new SimpleNotification.NotificationBuilder("Product changed successfully")
                      .duration(NotificationDuration.SHORT)
                      .icon("fas-circle-check")
                      .type(NotificationVariants.SUCCESS)
                      .build();
              notificationHolder.addNotification(notification);

              quotationProductPdct.clearSelection();

              QuotationMasterFormController.getInstance(stage)
                  .quotationDetailTable
                  .setItems(QuotationDetailViewModel.quotationDetailsList);

              closeDialog(e);
              return;
            }
            QuotationDetailViewModel.addQuotationDetails();

            SimpleNotification notification =
                new SimpleNotification.NotificationBuilder("Product added successfully")
                    .duration(NotificationDuration.SHORT)
                    .icon("fas-circle-check")
                    .type(NotificationVariants.SUCCESS)
                    .build();

            notificationHolder.addNotification(notification);

            quotationProductPdct.clearSelection();

            QuotationMasterFormController.getInstance(stage)
                .quotationDetailTable
                .setItems(QuotationDetailViewModel.quotationDetailsList);

            closeDialog(e);
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
