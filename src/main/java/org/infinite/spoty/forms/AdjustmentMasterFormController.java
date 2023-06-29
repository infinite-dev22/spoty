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

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.Validators.requiredValidator;

import io.github.palexdev.materialfx.controls.MFXContextMenu;
import io.github.palexdev.materialfx.controls.MFXContextMenuItem;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableRow;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.infinite.spoty.components.navigation.Pages;
import org.infinite.spoty.components.notification.SimpleNotification;
import org.infinite.spoty.components.notification.SimpleNotificationHolder;
import org.infinite.spoty.components.notification.enums.NotificationDuration;
import org.infinite.spoty.components.notification.enums.NotificationVariants;
import org.infinite.spoty.database.models.AdjustmentDetail;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.viewModels.AdjustmentDetailViewModel;
import org.infinite.spoty.viewModels.AdjustmentMasterViewModel;
import org.infinite.spoty.viewModels.BranchViewModel;
import org.infinite.spoty.views.BaseController;

@SuppressWarnings("unchecked")
public class AdjustmentMasterFormController implements Initializable {
  private static AdjustmentMasterFormController instance;
  public MFXTextField adjustmentMasterID = new MFXTextField();
  @FXML public MFXFilterComboBox<Branch> adjustmentBranch;
  @FXML public MFXDatePicker adjustmentDate;
  @FXML public MFXTableView<AdjustmentDetail> adjustmentDetailTable;
  @FXML public MFXTextField adjustmentNote;
  @FXML public BorderPane adjustmentFormContentPane;
  @FXML public Label adjustmentFormTitle;
  @FXML public MFXButton adjustmentProductAddBtn;
  @FXML public Label adjustmentBranchValidationLabel;
  @FXML public Label adjustmentDateValidationLabel;
  private Dialog<ButtonType> dialog;

  private AdjustmentMasterFormController(Stage stage) {
    Platform.runLater(
        () -> {
          try {
            quotationProductDialogPane(stage);
          } catch (IOException ex) {
            throw new RuntimeException(ex);
          }
        });
  }

  public static AdjustmentMasterFormController getInstance(Stage stage) {
    if (instance == null) instance = new AdjustmentMasterFormController(stage);
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Input binding.
    adjustmentMasterID
        .textProperty()
        .bindBidirectional(AdjustmentMasterViewModel.idProperty(), new NumberStringConverter());
    adjustmentBranch.valueProperty().bindBidirectional(AdjustmentMasterViewModel.branchProperty());
    adjustmentBranch.setItems(BranchViewModel.getBranches());
    adjustmentBranch.setResetOnPopupHidden(true);
    adjustmentBranch.setConverter(
        new StringConverter<>() {
          @Override
          public String toString(Branch object) {
            if (object != null) {
              System.out.println(object.getName());
              return object.getName();
            } else return null;
          }

          @Override
          public Branch fromString(String string) {
            return null;
          }
        });
    adjustmentDate.textProperty().bindBidirectional(AdjustmentMasterViewModel.dateProperty());
    adjustmentNote.textProperty().bindBidirectional(AdjustmentMasterViewModel.noteProperty());
    // input validators.
    requiredValidator(adjustmentBranch, "Branch is required.", adjustmentBranchValidationLabel);
    requiredValidator(adjustmentDate, "Date is required.", adjustmentDateValidationLabel);
    adjustmentAddProductBtnClicked();
    Platform.runLater(this::setupTable);
  }

  private void setupTable() {
    MFXTableColumn<AdjustmentDetail> productName =
        new MFXTableColumn<>(
            "Product", false, Comparator.comparing(AdjustmentDetail::getProductDetailName));
    MFXTableColumn<AdjustmentDetail> productQuantity =
        new MFXTableColumn<>(
            "Quantity", false, Comparator.comparing(AdjustmentDetail::getQuantity));
    MFXTableColumn<AdjustmentDetail> adjustmentType =
        new MFXTableColumn<>(
            "Adjustment Type", false, Comparator.comparing(AdjustmentDetail::getAdjustmentType));

    productName.setRowCellFactory(
        product -> new MFXTableRowCell<>(AdjustmentDetail::getProductDetailName));
    productQuantity.setRowCellFactory(
        product -> new MFXTableRowCell<>(AdjustmentDetail::getQuantity));
    adjustmentType.setRowCellFactory(
        product -> new MFXTableRowCell<>(AdjustmentDetail::getAdjustmentType));

    productName.prefWidthProperty().bind(adjustmentDetailTable.widthProperty().multiply(.4));
    productQuantity.prefWidthProperty().bind(adjustmentDetailTable.widthProperty().multiply(.4));
    adjustmentType.prefWidthProperty().bind(adjustmentDetailTable.widthProperty().multiply(.4));

    adjustmentDetailTable.getTableColumns().addAll(productName, productQuantity, adjustmentType);
    adjustmentDetailTable
        .getFilters()
        .addAll(
            new StringFilter<>("Name", AdjustmentDetail::getProductDetailName),
            new IntegerFilter<>("Quantity", AdjustmentDetail::getQuantity),
            new StringFilter<>("Adjustment Type", AdjustmentDetail::getAdjustmentType));
    getAdjustmentDetailTable();
    adjustmentDetailTable.setItems(AdjustmentDetailViewModel.getAdjustmentDetails());
  }

  private void getAdjustmentDetailTable() {
    adjustmentDetailTable.setPrefSize(1000, 1000);
    adjustmentDetailTable.features().enableBounceEffect();
    adjustmentDetailTable.features().enableSmoothScrolling(0.5);

    adjustmentDetailTable.setTableRowFactory(
        t -> {
          MFXTableRow<AdjustmentDetail> row = new MFXTableRow<>(adjustmentDetailTable, t);
          EventHandler<ContextMenuEvent> eventHandler =
              event -> {
                showContextMenu((MFXTableRow<AdjustmentDetail>) event.getSource())
                    .show(
                        adjustmentDetailTable.getScene().getWindow(),
                        event.getScreenX(),
                        event.getScreenY());
                event.consume();
              };
          row.setOnContextMenuRequested(eventHandler);
          return row;
        });
  }

  private MFXContextMenu showContextMenu(MFXTableRow<AdjustmentDetail> obj) {
    MFXContextMenu contextMenu = new MFXContextMenu(adjustmentDetailTable);
    MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
    MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

    // Actions
    // Delete
    delete.setOnAction(
        e -> {
          AdjustmentDetailViewModel.removeAdjustmentDetail(
              obj.getData().getId(),
              AdjustmentDetailViewModel.adjustmentDetailsTempList.indexOf(obj.getData()));
          AdjustmentDetailViewModel.getAdjustmentDetails();
          e.consume();
        });
    // Edit
    edit.setOnAction(
        e -> {
          AdjustmentDetailViewModel.getItem(
              obj.getData().getId(),
              AdjustmentDetailViewModel.adjustmentDetailsTempList.indexOf(obj.getData()));
          dialog.showAndWait();
          e.consume();
        });

    contextMenu.addItems(edit, delete);

    return contextMenu;
  }

  private void adjustmentAddProductBtnClicked() {
    adjustmentProductAddBtn.setOnAction(e -> dialog.showAndWait());
  }

  private void quotationProductDialogPane(Stage stage) throws IOException {
    DialogPane dialogPane = fxmlLoader("forms/AdjustmentDetailForm.fxml").load();
    dialog = new Dialog<>();
    dialog.setDialogPane(dialogPane);
    dialog.initOwner(stage);
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.initStyle(StageStyle.UNDECORATED);
  }

  public void adjustmentSaveBtnClicked() {
    SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();
    if (!adjustmentDetailTable.isDisabled()
        && AdjustmentDetailViewModel.adjustmentDetailsTempList.isEmpty()) {
      SimpleNotification notification =
          new SimpleNotification.NotificationBuilder("Table can't be Empty")
              .duration(NotificationDuration.SHORT)
              .icon("fas-triangle-exclamation")
              .type(NotificationVariants.ERROR)
              .build();
      notificationHolder.addNotification(notification);
      return;
    }
    if (!adjustmentBranchValidationLabel.isVisible()
        && !adjustmentDateValidationLabel.isVisible()) {
      if (Integer.parseInt(adjustmentMasterID.getText()) > 0) {
        AdjustmentMasterViewModel.updateItem(Integer.parseInt(adjustmentMasterID.getText()));
        SimpleNotification notification =
            new SimpleNotification.NotificationBuilder("Product adjustment updated successfully")
                .duration(NotificationDuration.MEDIUM)
                .icon("fas-circle-check")
                .type(NotificationVariants.SUCCESS)
                .build();
        notificationHolder.addNotification(notification);
        adjustmentCancelBtnClicked();
        return;
      }
        AdjustmentMasterViewModel.saveAdjustmentMaster();
        SimpleNotification notification =
            new SimpleNotification.NotificationBuilder("Product adjustment saved successfully")
                .duration(NotificationDuration.MEDIUM)
                .icon("fas-circle-check")
                .type(NotificationVariants.SUCCESS)
                .build();
        notificationHolder.addNotification(notification);
      adjustmentCancelBtnClicked();
      return;
    }
    SimpleNotification notification =
            new SimpleNotification.NotificationBuilder("Required fields missing")
                    .duration(NotificationDuration.SHORT)
                    .icon("fas-triangle-exclamation")
                    .type(NotificationVariants.ERROR)
                    .build();
    notificationHolder.addNotification(notification);
  }

  public void adjustmentCancelBtnClicked() {
    BaseController.navigation.navigate(Pages.getAdjustmentPane());
    AdjustmentMasterViewModel.resetProperties();
    AdjustmentDetailViewModel.adjustmentDetailsTempList.clear();
    adjustmentBranchValidationLabel.setVisible(false);
    adjustmentDateValidationLabel.setVisible(false);
  }
}
