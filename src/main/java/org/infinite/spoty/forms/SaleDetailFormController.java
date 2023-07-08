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
import org.infinite.spoty.viewModels.SaleDetailViewModel;

public class SaleDetailFormController implements Initializable {
  private static SaleDetailFormController instance;
  private final Stage stage;
  @FXML public MFXTextField saleDetailQnty;
  @FXML public MFXFilterComboBox<ProductDetail> saleDetailPdct;
  @FXML public MFXTextField saleDetailOrderTax;
  @FXML public MFXTextField saleDetailDiscount;
  @FXML public MFXButton saleProductsSaveBtn;
  @FXML public MFXButton saleProductsCancelBtn;
  @FXML public Label saleProductsTitle;
  @FXML public Label saleDetailPdctValidationLabel;
  @FXML public Label saleDetailQntyValidationLabel;

  private SaleDetailFormController(Stage stage) {
    this.stage = stage;
  }

  public static SaleDetailFormController getInstance(Stage stage) {
    if (Objects.equals(instance, null)) instance = new SaleDetailFormController(stage);
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Set combo box options.
    saleDetailPdct.setItems(ProductDetailViewModel.getProductDetailsComboBoxList());
    saleDetailPdct.setOnShowing(
        e -> saleDetailPdct.setItems(ProductDetailViewModel.getProductDetailsComboBoxList()));
    saleDetailPdct.setConverter(
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
    // Property binding.
    saleDetailQnty.textProperty().bindBidirectional(SaleDetailViewModel.quantityProperty());
    saleDetailPdct.valueProperty().bindBidirectional(SaleDetailViewModel.productProperty());
    saleDetailOrderTax.textProperty().bindBidirectional(SaleDetailViewModel.netTaxProperty());
    saleDetailDiscount.textProperty().bindBidirectional(SaleDetailViewModel.discountProperty());
    // Input validators.
    requiredValidator(saleDetailPdct, "Product is required.", saleDetailPdctValidationLabel);
    requiredValidator(saleDetailQnty, "Quantity is required.", saleDetailQntyValidationLabel);
    dialogOnActions();
  }

  private void dialogOnActions() {
    saleProductsCancelBtn.setOnAction(
        (e) -> {
          closeDialog(e);
          SaleDetailViewModel.resetProperties();
          saleDetailPdct.clearSelection();
          saleDetailPdctValidationLabel.setVisible(false);
          saleDetailQntyValidationLabel.setVisible(false);
        });
    saleProductsSaveBtn.setOnAction(
        (e) -> {
          SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

          if (!saleDetailPdctValidationLabel.isVisible()
              && !saleDetailQntyValidationLabel.isVisible()) {
            if (tempIdProperty().get() > -1) {
              SaleDetailViewModel.updateSaleDetail(SaleDetailViewModel.getId());

              SimpleNotification notification =
                  new SimpleNotification.NotificationBuilder("Product changed successfully")
                      .duration(NotificationDuration.SHORT)
                      .icon("fas-circle-check")
                      .type(NotificationVariants.SUCCESS)
                      .build();
              notificationHolder.addNotification(notification);

              saleDetailPdct.clearSelection();

              SaleMasterFormController.getInstance(stage)
                  .saleDetailTable
                  .setItems(SaleDetailViewModel.saleDetailList);

              closeDialog(e);
              return;
            }
            SaleDetailViewModel.addSaleDetail();

            SimpleNotification notification =
                new SimpleNotification.NotificationBuilder("Product added successfully")
                    .duration(NotificationDuration.SHORT)
                    .icon("fas-circle-check")
                    .type(NotificationVariants.SUCCESS)
                    .build();
            notificationHolder.addNotification(notification);

            saleDetailPdct.clearSelection();

            SaleMasterFormController.getInstance(stage)
                .saleDetailTable
                .setItems(SaleDetailViewModel.saleDetailList);

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
