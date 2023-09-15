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
import static org.infinite.spoty.values.SharedResources.getTempId;
import static org.infinite.spoty.values.SharedResources.tempIdProperty;

import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;
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
import org.infinite.spoty.viewModels.ProductViewModel;
import org.infinite.spoty.viewModels.SaleDetailViewModel;

public class SaleDetailFormController implements Initializable {
  private static SaleDetailFormController instance;
  @FXML public MFXTextField saleDetailQnty;
  @FXML public MFXFilterComboBox<Product> saleDetailPdct;
  @FXML public MFXTextField saleDetailOrderTax;
  @FXML public MFXTextField saleDetailDiscount;
  @FXML public MFXButton saleProductsSaveBtn;
  @FXML public MFXButton saleProductsCancelBtn;
  @FXML public Label saleDetailPdctValidationLabel;
  @FXML public Label saleDetailQntyValidationLabel;

  public static SaleDetailFormController getInstance() {
    if (Objects.equals(instance, null)) instance = new SaleDetailFormController();
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Property binding.
    saleDetailQnty.textProperty().bindBidirectional(SaleDetailViewModel.quantityProperty());
    saleDetailPdct.valueProperty().bindBidirectional(SaleDetailViewModel.productProperty());
    saleDetailOrderTax.textProperty().bindBidirectional(SaleDetailViewModel.netTaxProperty());
    saleDetailDiscount.textProperty().bindBidirectional(SaleDetailViewModel.discountProperty());

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

    // Set combo box options.
    saleDetailPdct.setItems(ProductViewModel.getProducts());
    saleDetailPdct.setConverter(productVariantConverter);
    saleDetailPdct.setFilterFunction(productVariantFilterFunction);

    // Input validators.
    requiredValidator(
        saleDetailPdct, "Product is required.", saleDetailPdctValidationLabel, saleProductsSaveBtn);
    requiredValidator(
        saleDetailQnty,
        "Quantity is required.",
        saleDetailQntyValidationLabel,
        saleProductsSaveBtn);

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
              GlobalActions.spotyThreadPool()
                  .execute(
                      () -> {
                        try {
                          SaleDetailViewModel.updateSaleDetail(getTempId());
                        } catch (SQLException ex) {
                          throw new RuntimeException(ex);
                        }
                      });

              SimpleNotification notification =
                  new SimpleNotification.NotificationBuilder("Product changed successfully")
                      .duration(NotificationDuration.SHORT)
                      .icon("fas-circle-check")
                      .type(NotificationVariants.SUCCESS)
                      .build();
              notificationHolder.addNotification(notification);

              saleDetailPdct.clearSelection();

              closeDialog(e);
              return;
            }
            GlobalActions.spotyThreadPool().execute(SaleDetailViewModel::addSaleDetail);

            SimpleNotification notification =
                new SimpleNotification.NotificationBuilder("Product added successfully")
                    .duration(NotificationDuration.SHORT)
                    .icon("fas-circle-check")
                    .type(NotificationVariants.SUCCESS)
                    .build();
            notificationHolder.addNotification(notification);

            saleDetailPdct.clearSelection();

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
