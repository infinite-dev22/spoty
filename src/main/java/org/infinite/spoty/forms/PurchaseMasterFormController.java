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

import io.github.palexdev.materialfx.controls.MFXContextMenu;
import io.github.palexdev.materialfx.controls.MFXContextMenuItem;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableRow;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.LongFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
import org.infinite.spoty.components.navigation.Pages;
import org.infinite.spoty.components.notification.SimpleNotification;
import org.infinite.spoty.components.notification.SimpleNotificationHolder;
import org.infinite.spoty.components.notification.enums.NotificationDuration;
import org.infinite.spoty.components.notification.enums.NotificationVariants;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.PurchaseDetail;
import org.infinite.spoty.database.models.Supplier;
import org.infinite.spoty.values.strings.Values;
import org.infinite.spoty.viewModels.BranchViewModel;
import org.infinite.spoty.viewModels.PurchaseDetailViewModel;
import org.infinite.spoty.viewModels.PurchaseMasterViewModel;
import org.infinite.spoty.viewModels.SupplierViewModel;
import org.infinite.spoty.views.BaseController;

@SuppressWarnings("unchecked")
public class PurchaseMasterFormController implements Initializable {
  private static PurchaseMasterFormController instance;
  @FXML public Label purchaseFormTitle;
  @FXML public MFXDatePicker purchaseDate;
  @FXML public MFXFilterComboBox<Supplier> purchaseSupplier;
  @FXML public MFXFilterComboBox<Branch> purchaseBranch;
  @FXML public MFXTableView<PurchaseDetail> purchaseDetailTable;
  @FXML public MFXTextField purchaseNote;
  @FXML public BorderPane purchaseFormContentPane;
  @FXML public MFXFilterComboBox<String> purchaseStatus;
  @FXML public Label purchaseBranchValidationLabel;
  @FXML public Label purchaseSupplierValidationLabel;
  @FXML public Label purchaseDateValidationLabel;
  @FXML public Label purchaseStatusValidationLabel;
  @FXML public MFXButton purchaseMasterSaveBtn;
  private MFXStageDialog dialog;

  private PurchaseMasterFormController(Stage stage) {
    Platform.runLater(
        () -> {
          try {
            createPurchaseProductDialog(stage);
          } catch (IOException ex) {
            throw new RuntimeException(ex);
          }
        });
  }

  public static PurchaseMasterFormController getInstance(Stage stage) {
    if (instance == null) instance = new PurchaseMasterFormController(stage);
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Bi~Directional Binding.
    purchaseDate.textProperty().bindBidirectional(PurchaseMasterViewModel.dateProperty());
    purchaseSupplier.valueProperty().bindBidirectional(PurchaseMasterViewModel.supplierProperty());
    purchaseBranch.valueProperty().bindBidirectional(PurchaseMasterViewModel.branchProperty());
    purchaseStatus.textProperty().bindBidirectional(PurchaseMasterViewModel.statusProperty());
    purchaseNote.textProperty().bindBidirectional(PurchaseMasterViewModel.noteProperty());

    // ComboBox Converters.
    StringConverter<Supplier> supplierConverter =
        FunctionalStringConverter.to(supplier -> (supplier == null) ? "" : supplier.getName());

    StringConverter<Branch> branchConverter =
        FunctionalStringConverter.to(branch -> (branch == null) ? "" : branch.getName());

    // ComboBox Filter Functions.
    Function<String, Predicate<Supplier>> supplierFilterFunction =
        searchStr ->
            supplier ->
                StringUtils.containsIgnoreCase(supplierConverter.toString(supplier), searchStr);

    Function<String, Predicate<Branch>> branchFilterFunction =
        searchStr ->
            branch -> StringUtils.containsIgnoreCase(branchConverter.toString(branch), searchStr);

    // Set items to combo boxes and display custom text.
    purchaseSupplier.setItems(SupplierViewModel.getSuppliersComboBoxList());
    purchaseSupplier.setConverter(supplierConverter);
    purchaseSupplier.setFilterFunction(supplierFilterFunction);

    purchaseBranch.setItems(BranchViewModel.getBranchesComboBoxList());
    purchaseBranch.setConverter(branchConverter);
    purchaseBranch.setFilterFunction(branchFilterFunction);

    purchaseStatus.setItems(FXCollections.observableArrayList(Values.PURCHASE_STATUSES));

    // input validators.
    requiredValidator(
        purchaseBranch,
        "Branch is required.",
        purchaseBranchValidationLabel,
        purchaseMasterSaveBtn);
    requiredValidator(
        purchaseSupplier,
        "Supplier is required.",
        purchaseSupplierValidationLabel,
        purchaseMasterSaveBtn);
    requiredValidator(
        purchaseDate, "Date is required.", purchaseDateValidationLabel, purchaseMasterSaveBtn);
    requiredValidator(
        purchaseStatus,
        "Status is required.",
        purchaseStatusValidationLabel,
        purchaseMasterSaveBtn);

    setupTable();
  }

  private void createPurchaseProductDialog(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = fxmlLoader("forms/PurchaseDetailForm.fxml");
    fxmlLoader.setControllerFactory(c -> PurchaseDetailFormController.getInstance());

    MFXGenericDialog dialogContent = fxmlLoader.load();

    dialogContent.setShowMinimize(false);
    dialogContent.setShowAlwaysOnTop(false);

    dialog =
        MFXGenericDialogBuilder.build(dialogContent)
            .toStageDialogBuilder()
            .initOwner(stage)
            .initModality(Modality.WINDOW_MODAL)
            .setOwnerNode(purchaseFormContentPane)
            .setScrimPriority(ScrimPriority.WINDOW)
            .setScrimOwner(true)
            .get();

    io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
  }

  public void saveBtnClicked() {
    SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

    if (!purchaseDetailTable.isDisabled() && PurchaseDetailViewModel.purchaseDetailList.isEmpty()) {
      SimpleNotification notification =
          new SimpleNotification.NotificationBuilder("Table can't be Empty")
              .duration(NotificationDuration.SHORT)
              .icon("fas-triangle-exclamation")
              .type(NotificationVariants.ERROR)
              .build();
      notificationHolder.addNotification(notification);
      return;
    }
    if (!purchaseBranchValidationLabel.isVisible()
        && !purchaseSupplierValidationLabel.isVisible()
        && !purchaseDateValidationLabel.isVisible()
        && !purchaseStatusValidationLabel.isVisible()) {
      if (PurchaseMasterViewModel.getId() > 0) {
        PurchaseMasterViewModel.updateItem(PurchaseMasterViewModel.getId());

        SimpleNotification notification =
            new SimpleNotification.NotificationBuilder("Purchase updated successfully")
                .duration(NotificationDuration.MEDIUM)
                .icon("fas-circle-check")
                .type(NotificationVariants.SUCCESS)
                .build();
        notificationHolder.addNotification(notification);

        purchaseSupplier.clearSelection();
        purchaseBranch.clearSelection();
        purchaseStatus.clearSelection();

        cancelBtnClicked();
        return;
      }
      PurchaseMasterViewModel.savePurchaseMaster();

      SimpleNotification notification =
          new SimpleNotification.NotificationBuilder("Purchase saved successfully")
              .duration(NotificationDuration.MEDIUM)
              .icon("fas-circle-check")
              .type(NotificationVariants.SUCCESS)
              .build();
      notificationHolder.addNotification(notification);

      purchaseSupplier.clearSelection();
      purchaseBranch.clearSelection();
      purchaseStatus.clearSelection();

      cancelBtnClicked();
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

  private void setupTable() {
    // Set table column titles.
    MFXTableColumn<PurchaseDetail> product =
        new MFXTableColumn<>(
            "Product", false, Comparator.comparing(PurchaseDetail::getProductName));
    MFXTableColumn<PurchaseDetail> quantity =
        new MFXTableColumn<>("Quantity", false, Comparator.comparing(PurchaseDetail::getQuantity));
    MFXTableColumn<PurchaseDetail> tax =
        new MFXTableColumn<>("Tax", false, Comparator.comparing(PurchaseDetail::getNetTax));
    MFXTableColumn<PurchaseDetail> discount =
        new MFXTableColumn<>("Discount", false, Comparator.comparing(PurchaseDetail::getDiscount));
    MFXTableColumn<PurchaseDetail> price =
        new MFXTableColumn<>("Price", false, Comparator.comparing(PurchaseDetail::getPrice));
    MFXTableColumn<PurchaseDetail> totalPrice =
        new MFXTableColumn<>(
            "Total Price", false, Comparator.comparing(PurchaseDetail::getTotalPrice));

    // Set table column data.
    product.setRowCellFactory(
        purchaseDetail -> new MFXTableRowCell<>(PurchaseDetail::getProductName));
    quantity.setRowCellFactory(
        purchaseDetail -> new MFXTableRowCell<>(PurchaseDetail::getQuantity));
    tax.setRowCellFactory(purchaseDetail -> new MFXTableRowCell<>(PurchaseDetail::getNetTax));
    discount.setRowCellFactory(
        purchaseDetail -> new MFXTableRowCell<>(PurchaseDetail::getDiscount));
    price.setRowCellFactory(purchaseDetail -> new MFXTableRowCell<>(PurchaseDetail::getPrice));
    totalPrice.setRowCellFactory(
        purchaseDetail -> new MFXTableRowCell<>(PurchaseDetail::getTotalPrice));

    // Set table column width.
    product.prefWidthProperty().bind(purchaseDetailTable.widthProperty().multiply(.25));
    quantity.prefWidthProperty().bind(purchaseDetailTable.widthProperty().multiply(.25));
    tax.prefWidthProperty().bind(purchaseDetailTable.widthProperty().multiply(.25));
    discount.prefWidthProperty().bind(purchaseDetailTable.widthProperty().multiply(.25));
    price.prefWidthProperty().bind(purchaseDetailTable.widthProperty().multiply(.25));
    totalPrice.prefWidthProperty().bind(purchaseDetailTable.widthProperty().multiply(.25));

    // Set table filter.
    purchaseDetailTable
        .getTableColumns()
        .addAll(product, quantity, tax, discount, price, totalPrice);

    purchaseDetailTable
        .getFilters()
        .addAll(
            new StringFilter<>("Product", PurchaseDetail::getProductName),
            new LongFilter<>("Quantity", PurchaseDetail::getQuantity),
            new DoubleFilter<>("Tax", PurchaseDetail::getNetTax),
            new DoubleFilter<>("Discount", PurchaseDetail::getDiscount),
            new DoubleFilter<>("Price", PurchaseDetail::getPrice),
            new DoubleFilter<>("Total Price", PurchaseDetail::getTotalPrice));

    styleTable();

    // Populate table.
    if (PurchaseDetailViewModel.getPurchaseDetails().isEmpty()) {
      PurchaseDetailViewModel.getPurchaseDetails()
          .addListener(
              (ListChangeListener<PurchaseDetail>)
                  c -> purchaseDetailTable.setItems(PurchaseDetailViewModel.getPurchaseDetails()));
    } else {
      purchaseDetailTable
          .itemsProperty()
          .bindBidirectional(PurchaseDetailViewModel.purchaseDetailsProperty());
    }
  }

  private void styleTable() {
    purchaseDetailTable.setPrefSize(1000, 1000);
    purchaseDetailTable.features().enableBounceEffect();
    purchaseDetailTable.features().enableSmoothScrolling(0.5);

    purchaseDetailTable.setTableRowFactory(
        t -> {
          MFXTableRow<PurchaseDetail> row = new MFXTableRow<>(purchaseDetailTable, t);
          EventHandler<ContextMenuEvent> eventHandler =
              event -> {
                showContextMenu((MFXTableRow<PurchaseDetail>) event.getSource())
                    .show(
                        purchaseDetailTable.getScene().getWindow(),
                        event.getScreenX(),
                        event.getScreenY());
                event.consume();
              };
          row.setOnContextMenuRequested(eventHandler);
          return row;
        });
  }

  private MFXContextMenu showContextMenu(MFXTableRow<PurchaseDetail> obj) {
    MFXContextMenu contextMenu = new MFXContextMenu(purchaseDetailTable);
    MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
    MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

    // Actions
    // Delete
    delete.setOnAction(
        e -> {
          PurchaseDetailViewModel.removePurchaseDetail(
              obj.getData().getId(),
              PurchaseDetailViewModel.purchaseDetailList.indexOf(obj.getData()));
          e.consume();
        });
    // Edit
    edit.setOnAction(
        e -> {
          PurchaseDetailViewModel.getItem(
              obj.getData().getId(),
              PurchaseDetailViewModel.purchaseDetailList.indexOf(obj.getData()));
          dialog.showAndWait();
          e.consume();
        });

    contextMenu.addItems(edit, delete);

    return contextMenu;
  }

  public void cancelBtnClicked() {
    BaseController.navigation.navigate(Pages.getPurchasePane());

    PurchaseMasterViewModel.resetProperties();

    purchaseBranchValidationLabel.setVisible(false);
    purchaseSupplierValidationLabel.setVisible(false);
    purchaseDateValidationLabel.setVisible(false);
    purchaseStatusValidationLabel.setVisible(false);
    purchaseSupplier.clearSelection();
    purchaseBranch.clearSelection();
    purchaseStatus.clearSelection();
  }

  public void addBtnClicked() {
    dialog.showAndWait();
  }
}
