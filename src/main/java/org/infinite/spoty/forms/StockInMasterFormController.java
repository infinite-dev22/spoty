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

import static org.infinite.spoty.SpotyResourceLoader.fxmlLoader;
import static org.infinite.spoty.Validators.requiredValidator;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.filter.LongFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.infinite.spoty.GlobalActions;
import org.infinite.spoty.components.navigation.Pages;
import org.infinite.spoty.components.notification.SimpleNotification;
import org.infinite.spoty.components.notification.SimpleNotificationHolder;
import org.infinite.spoty.components.notification.enums.NotificationDuration;
import org.infinite.spoty.components.notification.enums.NotificationVariants;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.StockInDetail;
import org.infinite.spoty.viewModels.BranchViewModel;
import org.infinite.spoty.viewModels.StockInDetailViewModel;
import org.infinite.spoty.viewModels.StockInMasterViewModel;
import org.infinite.spoty.views.BaseController;

@SuppressWarnings("unchecked")
public class StockInMasterFormController implements Initializable {
  private static StockInMasterFormController instance;
  @FXML public MFXFilterComboBox<Branch> stockInMasterBranch;
  @FXML public MFXDatePicker stockInMasterDate;
  @FXML public MFXTableView<StockInDetail> stockInDetailTable;
  @FXML public MFXTextField stockInMasterNote;
  @FXML public BorderPane stockInMasterFormContentPane;
  @FXML public Label stockInMasterFormTitle;
  @FXML public MFXButton stockInMasterProductAddBtn;
  @FXML public Label stockInMasterDateValidationLabel;
  @FXML public Label stockInMasterBranchValidationLabel;
  public MFXButton stockInMasterSaveBtn;
  private MFXStageDialog dialog;

  private StockInMasterFormController(Stage stage) {
    Platform.runLater(
        () -> {
          try {
            quotationProductDialogPane(stage);
          } catch (IOException ex) {
            throw new RuntimeException(ex);
          }
        });
  }

  public static StockInMasterFormController getInstance(Stage stage) {
    if (instance == null) instance = new StockInMasterFormController(stage);
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Input binding.
    stockInMasterBranch.valueProperty().bindBidirectional(StockInMasterViewModel.branchProperty());
    stockInMasterDate.textProperty().bindBidirectional(StockInMasterViewModel.dateProperty());
    stockInMasterNote.textProperty().bindBidirectional(StockInMasterViewModel.noteProperty());

    // ComboBox Converters.
    StringConverter<Branch> branchConverter =
        FunctionalStringConverter.to(branch -> (branch == null) ? "" : branch.getName());

    // ComboBox Filter Functions.
    Function<String, Predicate<Branch>> branchFilterFunction =
        searchStr ->
            branch -> StringUtils.containsIgnoreCase(branchConverter.toString(branch), searchStr);

    // ComboBox properties.
    stockInMasterBranch.setItems(BranchViewModel.getBranchesComboBoxList());
    stockInMasterBranch.setConverter(branchConverter);
    stockInMasterBranch.setFilterFunction(branchFilterFunction);

    // input validators.
    requiredValidator(
        stockInMasterBranch,
        "Branch is required.",
        stockInMasterBranchValidationLabel,
        stockInMasterSaveBtn);
    requiredValidator(
        stockInMasterDate,
        "Date is required.",
        stockInMasterDateValidationLabel,
        stockInMasterSaveBtn);

    stockInMasterAddProductBtnClicked();

    Platform.runLater(this::setupTable);
  }

  private void setupTable() {
    MFXTableColumn<StockInDetail> productName =
        new MFXTableColumn<>(
            "Product", false, Comparator.comparing(StockInDetail::getProductName));
    MFXTableColumn<StockInDetail> productQuantity =
        new MFXTableColumn<>("Quantity", false, Comparator.comparing(StockInDetail::getQuantity));

    productName.setRowCellFactory(
        product -> new MFXTableRowCell<>(StockInDetail::getProductName));
    productQuantity.setRowCellFactory(product -> new MFXTableRowCell<>(StockInDetail::getQuantity));

    productName.prefWidthProperty().bind(stockInDetailTable.widthProperty().multiply(.4));
    productQuantity.prefWidthProperty().bind(stockInDetailTable.widthProperty().multiply(.4));

    stockInDetailTable.getTableColumns().addAll(productName, productQuantity);

    stockInDetailTable
        .getFilters()
        .addAll(
            new StringFilter<>("Name", StockInDetail::getProductName),
            new LongFilter<>("Quantity", StockInDetail::getQuantity));

    getStockInDetailTable();

    if (StockInDetailViewModel.getStockInDetails().isEmpty()) {
      StockInDetailViewModel.getStockInDetails()
          .addListener(
              (ListChangeListener<StockInDetail>)
                  c -> stockInDetailTable.setItems(StockInDetailViewModel.getStockInDetails()));
    } else {
      stockInDetailTable
          .itemsProperty()
          .bindBidirectional(StockInDetailViewModel.stockInDetailsProperty());
    }
  }

  private void getStockInDetailTable() {
    stockInDetailTable.setPrefSize(1000, 1000);
    stockInDetailTable.features().enableBounceEffect();
    stockInDetailTable.features().enableSmoothScrolling(0.5);

    stockInDetailTable.setTableRowFactory(
        t -> {
          MFXTableRow<StockInDetail> row = new MFXTableRow<>(stockInDetailTable, t);
          EventHandler<ContextMenuEvent> eventHandler =
              event -> {
                showContextMenu((MFXTableRow<StockInDetail>) event.getSource())
                    .show(
                        stockInDetailTable.getScene().getWindow(),
                        event.getScreenX(),
                        event.getScreenY());
                event.consume();
              };
          row.setOnContextMenuRequested(eventHandler);
          return row;
        });
  }

  private MFXContextMenu showContextMenu(MFXTableRow<StockInDetail> obj) {
    MFXContextMenu contextMenu = new MFXContextMenu(stockInDetailTable);
    MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
    MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

    // Actions
    // Delete
    delete.setOnAction(
        e -> {
          GlobalActions.spotyThreadPool()
              .execute(
                  () ->
                      StockInDetailViewModel.removeStockInDetail(
                          obj.getData().getId(),
                          StockInDetailViewModel.stockInDetailsList.indexOf(obj.getData())));

          e.consume();
        });

    // Edit
    edit.setOnAction(
        e -> {
          GlobalActions.spotyThreadPool()
              .execute(
                  () -> {
                    try {
                      StockInDetailViewModel.getItem(
                          obj.getData().getId(),
                          StockInDetailViewModel.stockInDetailsList.indexOf(obj.getData()));
                    } catch (SQLException ex) {
                      throw new RuntimeException(ex);
                    }
                  });

          dialog.showAndWait();
          e.consume();
        });

    contextMenu.addItems(edit, delete);

    return contextMenu;
  }

  private void stockInMasterAddProductBtnClicked() {
    stockInMasterProductAddBtn.setOnAction(e -> dialog.showAndWait());
  }

  private void quotationProductDialogPane(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = fxmlLoader("forms/StockInDetailForm.fxml");
    fxmlLoader.setControllerFactory(c -> StockInDetailFormController.getInstance());

    MFXGenericDialog dialogContent = fxmlLoader.load();

    dialogContent.setShowMinimize(false);
    dialogContent.setShowAlwaysOnTop(false);

    dialog =
        MFXGenericDialogBuilder.build(dialogContent)
            .toStageDialogBuilder()
            .initOwner(stage)
            .initModality(Modality.WINDOW_MODAL)
            .setOwnerNode(stockInMasterFormContentPane)
            .setScrimPriority(ScrimPriority.WINDOW)
            .setScrimOwner(true)
            .get();

    io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
  }

  public void stockInMasterSaveBtnClicked() {
    SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

    if (!stockInDetailTable.isDisabled() && StockInDetailViewModel.stockInDetailsList.isEmpty()) {
      SimpleNotification notification =
          new SimpleNotification.NotificationBuilder("Table can't be Empty")
              .duration(NotificationDuration.SHORT)
              .icon("fas-triangle-exclamation")
              .type(NotificationVariants.ERROR)
              .build();
      notificationHolder.addNotification(notification);
      return;
    }
    if (!stockInMasterBranchValidationLabel.isVisible()
        && !stockInMasterDateValidationLabel.isVisible()) {
      if (StockInMasterViewModel.getId() > 0) {
        GlobalActions.spotyThreadPool()
            .execute(
                () -> {
                  try {
                    StockInMasterViewModel.updateItem(StockInMasterViewModel.getId());
                  } catch (SQLException e) {
                    throw new RuntimeException(e);
                  }
                });

        SimpleNotification notification =
            new SimpleNotification.NotificationBuilder("Stock In updated successfully")
                .duration(NotificationDuration.MEDIUM)
                .icon("fas-circle-check")
                .type(NotificationVariants.SUCCESS)
                .build();
        notificationHolder.addNotification(notification);

        stockInMasterBranch.clearSelection();
        stockInMasterCancelBtnClicked();
        return;
      }
      GlobalActions.spotyThreadPool()
          .execute(
              () -> {
                try {
                  StockInMasterViewModel.saveStockInMaster();
                } catch (SQLException e) {
                  throw new RuntimeException(e);
                }
              });

      SimpleNotification notification =
          new SimpleNotification.NotificationBuilder("Stock In saved successfully")
              .duration(NotificationDuration.MEDIUM)
              .icon("fas-circle-check")
              .type(NotificationVariants.SUCCESS)
              .build();
      notificationHolder.addNotification(notification);

      stockInMasterBranch.clearSelection();
      stockInMasterCancelBtnClicked();
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

  public void stockInMasterCancelBtnClicked() {
    BaseController.navigation.navigate(Pages.getStockInPane());
    StockInMasterViewModel.resetProperties();
    stockInMasterBranchValidationLabel.setVisible(false);
    stockInMasterDateValidationLabel.setVisible(false);
    stockInMasterBranch.clearSelection();
  }
}
