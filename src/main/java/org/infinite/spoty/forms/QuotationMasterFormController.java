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
import org.infinite.spoty.database.models.Customer;
import org.infinite.spoty.database.models.QuotationDetail;
import org.infinite.spoty.values.strings.Values;
import org.infinite.spoty.viewModels.BranchViewModel;
import org.infinite.spoty.viewModels.CustomerViewModel;
import org.infinite.spoty.viewModels.QuotationDetailViewModel;
import org.infinite.spoty.viewModels.QuotationMasterViewModel;
import org.infinite.spoty.views.BaseController;

@SuppressWarnings("unchecked")
public class QuotationMasterFormController implements Initializable {
  private static QuotationMasterFormController instance;
  @FXML public Label quotationFormTitle;
  @FXML public MFXDatePicker quotationDate;
  @FXML public MFXFilterComboBox<Customer> quotationCustomer;
  @FXML public MFXFilterComboBox<Branch> quotationBranch;
  @FXML public MFXTableView<QuotationDetail> quotationDetailTable;
  @FXML public MFXTextField quotationNote;
  @FXML public BorderPane quotationFormContentPane;
  @FXML public MFXFilterComboBox<String> quotationStatus;
  @FXML public Label quotationDateValidationLabel;
  @FXML public Label quotationCustomerValidationLabel;
  @FXML public Label quotationBranchValidationLabel;
  @FXML public Label quotationStatusValidationLabel;
  @FXML public MFXButton quotationMasterSaveBtn;
  private MFXStageDialog dialog;

  private QuotationMasterFormController(Stage stage) {
    Platform.runLater(
        () -> {
          try {
            quotationProductDialogPane(stage);
          } catch (IOException ex) {
            throw new RuntimeException(ex);
          }
        });
  }

  public static QuotationMasterFormController getInstance(Stage stage) {
    if (instance == null) instance = new QuotationMasterFormController(stage);
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Form input binding.
    quotationDate.textProperty().bindBidirectional(QuotationMasterViewModel.dateProperty());
    quotationCustomer
        .valueProperty()
        .bindBidirectional(QuotationMasterViewModel.customerProperty());
    quotationBranch.valueProperty().bindBidirectional(QuotationMasterViewModel.branchProperty());
    quotationStatus.textProperty().bindBidirectional(QuotationMasterViewModel.statusProperty());
    quotationNote.textProperty().bindBidirectional(QuotationMasterViewModel.noteProperty());

    // ComboBox Converters.
    StringConverter<Customer> customerConverter =
            FunctionalStringConverter.to(customer -> (customer == null) ? "" : customer.getName());

    StringConverter<Branch> branchConverter =
            FunctionalStringConverter.to(branch -> (branch == null) ? "" : branch.getName());

    // ComboBox Filter Functions.
    Function<String, Predicate<Customer>> customerFilterFunction =
            searchStr ->
                    customer ->
                            StringUtils.containsIgnoreCase(
                                    customerConverter.toString(customer), searchStr);

    Function<String, Predicate<Branch>> branchFilterFunction =
            searchStr ->
                    branch ->
                            StringUtils.containsIgnoreCase(branchConverter.toString(branch), searchStr);

    // Combo box properties.
    quotationCustomer.setItems(CustomerViewModel.getCustomersComboBoxList());
    quotationCustomer.setConverter(customerConverter);
    quotationCustomer.setFilterFunction(customerFilterFunction);

    quotationBranch.setItems(BranchViewModel.getBranchesComboBoxList());
    quotationBranch.setConverter(branchConverter);
    quotationBranch.setFilterFunction(branchFilterFunction);
    
    quotationStatus.setItems(FXCollections.observableArrayList(Values.QUOTATION_TYPE));

    // input validators.
    requiredValidator(
        quotationBranch,
        "Branch is required.",
        quotationBranchValidationLabel,
        quotationMasterSaveBtn);
    requiredValidator(
        quotationCustomer,
        "Customer is required.",
        quotationCustomerValidationLabel,
        quotationMasterSaveBtn);
    requiredValidator(
        quotationDate, "Date is required.", quotationDateValidationLabel, quotationMasterSaveBtn);
    requiredValidator(
        quotationStatus,
        "Status is required.",
        quotationStatusValidationLabel,
        quotationMasterSaveBtn);

    Platform.runLater(this::setupTable);
  }

  private void setupTable() {
    MFXTableColumn<QuotationDetail> productName =
        new MFXTableColumn<>(
            "Product", false, Comparator.comparing(QuotationDetail::getProductName));
    MFXTableColumn<QuotationDetail> productQuantity =
        new MFXTableColumn<>("Quantity", false, Comparator.comparing(QuotationDetail::getQuantity));
    MFXTableColumn<QuotationDetail> productDiscount =
        new MFXTableColumn<>("Discount", false, Comparator.comparing(QuotationDetail::getDiscount));
    MFXTableColumn<QuotationDetail> productTax =
        new MFXTableColumn<>("Tax", false, Comparator.comparing(QuotationDetail::getNetTax));

    productName.setRowCellFactory(
        product -> new MFXTableRowCell<>(QuotationDetail::getProductName));
    productQuantity.setRowCellFactory(
        product -> new MFXTableRowCell<>(QuotationDetail::getQuantity));
    productDiscount.setRowCellFactory(
        product -> new MFXTableRowCell<>(QuotationDetail::getDiscount));
    productTax.setRowCellFactory(product -> new MFXTableRowCell<>(QuotationDetail::getNetTax));

    productName.prefWidthProperty().bind(quotationDetailTable.widthProperty().multiply(.25));
    productQuantity.prefWidthProperty().bind(quotationDetailTable.widthProperty().multiply(.25));
    productDiscount.prefWidthProperty().bind(quotationDetailTable.widthProperty().multiply(.25));
    productTax.prefWidthProperty().bind(quotationDetailTable.widthProperty().multiply(.25));

    quotationDetailTable
        .getTableColumns()
        .addAll(productName, productQuantity, productDiscount, productTax);

    quotationDetailTable
        .getFilters()
        .addAll(
            new StringFilter<>("Product", QuotationDetail::getProductName),
            new LongFilter<>("Quantity", QuotationDetail::getQuantity),
            new DoubleFilter<>("Discount", QuotationDetail::getDiscount),
            new DoubleFilter<>("Tax", QuotationDetail::getNetTax));

    getQuotationDetailTable();

    if (QuotationDetailViewModel.getQuotationDetails().isEmpty()) {
      QuotationDetailViewModel.getQuotationDetails()
          .addListener(
              (ListChangeListener<QuotationDetail>)
                  c ->
                      quotationDetailTable.setItems(
                          QuotationDetailViewModel.getQuotationDetails()));
    } else {
      quotationDetailTable
          .itemsProperty()
          .bindBidirectional(QuotationDetailViewModel.quotationDetailsProperty());
    }
  }

  private void getQuotationDetailTable() {
    quotationDetailTable.setPrefSize(1000, 1000);
    quotationDetailTable.features().enableBounceEffect();
    quotationDetailTable.features().enableSmoothScrolling(0.5);

    quotationDetailTable.setTableRowFactory(
        t -> {
          MFXTableRow<QuotationDetail> row = new MFXTableRow<>(quotationDetailTable, t);
          EventHandler<ContextMenuEvent> eventHandler =
              event -> {
                showContextMenu((MFXTableRow<QuotationDetail>) event.getSource())
                    .show(
                        quotationDetailTable.getScene().getWindow(),
                        event.getScreenX(),
                        event.getScreenY());
                event.consume();
              };
          row.setOnContextMenuRequested(eventHandler);
          return row;
        });
  }

  private MFXContextMenu showContextMenu(MFXTableRow<QuotationDetail> obj) {
    MFXContextMenu contextMenu = new MFXContextMenu(quotationDetailTable);
    MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
    MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

    // Actions
    // Delete
    delete.setOnAction(
        e -> {
          QuotationDetailViewModel.removeQuotationDetail(
              obj.getData().getId(),
              QuotationDetailViewModel.quotationDetailsList.indexOf(obj.getData()));
          e.consume();
        });
    // Edit
    edit.setOnAction(
        e -> {
          QuotationDetailViewModel.getItem(
              obj.getData().getId(),
              QuotationDetailViewModel.quotationDetailsList.indexOf(obj.getData()));
          dialog.showAndWait();
          e.consume();
        });

    contextMenu.addItems(edit, delete);

    return contextMenu;
  }

  private void quotationProductDialogPane(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = fxmlLoader("forms/QuotationDetailForm.fxml");
    fxmlLoader.setControllerFactory(c -> QuotationDetailFormController.getInstance());

    MFXGenericDialog dialogContent = fxmlLoader.load();

    dialogContent.setShowMinimize(false);
    dialogContent.setShowAlwaysOnTop(false);

    dialog =
        MFXGenericDialogBuilder.build(dialogContent)
            .toStageDialogBuilder()
            .initOwner(stage)
            .initModality(Modality.WINDOW_MODAL)
            .setOwnerNode(quotationFormContentPane)
            .setScrimPriority(ScrimPriority.WINDOW)
            .setScrimOwner(true)
            .get();

    io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
  }

  public void saveBtnClicked() {
    SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

    if (!quotationDetailTable.isDisabled()
        && QuotationDetailViewModel.quotationDetailsList.isEmpty()) {
      SimpleNotification notification =
          new SimpleNotification.NotificationBuilder("Table can't be Empty")
              .duration(NotificationDuration.SHORT)
              .icon("fas-triangle-exclamation")
              .type(NotificationVariants.ERROR)
              .build();
      notificationHolder.addNotification(notification);
      return;
    }

    if (!quotationBranchValidationLabel.isVisible()
        && !quotationCustomerValidationLabel.isVisible()
        && !quotationDateValidationLabel.isVisible()
        && !quotationStatusValidationLabel.isVisible()) {
      if (QuotationMasterViewModel.getId() > 0) {
        QuotationMasterViewModel.updateItem(QuotationMasterViewModel.getId());

        SimpleNotification notification =
            new SimpleNotification.NotificationBuilder("Quotation updated successfully")
                .duration(NotificationDuration.MEDIUM)
                .icon("fas-circle-check")
                .type(NotificationVariants.SUCCESS)
                .build();
        notificationHolder.addNotification(notification);

        quotationCustomer.clearSelection();
        quotationBranch.clearSelection();
        quotationStatus.clearSelection();

        cancelBtnClicked();
        return;
      }
      QuotationMasterViewModel.saveQuotationMaster();

      SimpleNotification notification =
          new SimpleNotification.NotificationBuilder("Quotation saved successfully")
              .duration(NotificationDuration.MEDIUM)
              .icon("fas-circle-check")
              .type(NotificationVariants.SUCCESS)
              .build();
      notificationHolder.addNotification(notification);

      quotationCustomer.clearSelection();
      quotationBranch.clearSelection();
      quotationStatus.clearSelection();

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

  public void cancelBtnClicked() {
    BaseController.navigation.navigate(Pages.getQuotationPane());

    QuotationMasterViewModel.resetProperties();

    quotationBranchValidationLabel.setVisible(false);
    quotationCustomerValidationLabel.setVisible(false);
    quotationDateValidationLabel.setVisible(false);
    quotationStatusValidationLabel.setVisible(false);
    quotationCustomer.clearSelection();
    quotationBranch.clearSelection();
    quotationStatus.clearSelection();
  }

  public void addBtnClicked() {
    dialog.showAndWait();
  }
}
