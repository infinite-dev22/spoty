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

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXElevatedButton;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
import org.infinite.spoty.database.models.Brand;
import org.infinite.spoty.database.models.ProductCategory;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.values.strings.Values;
import org.infinite.spoty.viewModels.BrandViewModel;
import org.infinite.spoty.viewModels.ProductCategoryViewModel;
import org.infinite.spoty.viewModels.ProductDetailViewModel;
import org.infinite.spoty.viewModels.ProductMasterViewModel;
import org.infinite.spoty.views.BaseController;

@SuppressWarnings("unchecked")
public class ProductMasterFormController implements Initializable {
  private static ProductMasterFormController instance;
  /**
   * TODO: Add has variants toggle. TODO: Products can be assigned to specific branches by the head
   * branch, Add this possibility. TODO: Make Branch combo multi-choice. TODO: Product variants can
   * later have images. Same as to Products without variants.
   */
  public MFXTextField productMasterID = new MFXTextField();

  @FXML public MFXTextField productFormName;
  @FXML public MFXTableView<ProductDetail> productDetailTable;
  @FXML public BorderPane productFormContentPane;
  @FXML public Label productFormTitle;
  @FXML public MFXElevatedButton productVariantAddBtn;
  @FXML public MFXFilterComboBox<ProductCategory> productFormCategory;
  @FXML public MFXFilterComboBox<Brand> productFormBrand;
  @FXML public MFXComboBox<String> productFormBarCodeType;
  @FXML public MFXToggleButton notForSaleToggle;
  @FXML public MFXToggleButton hasVariantToggle;
  @FXML public Label productFormNameValidationLabel;
  @FXML public Label productFormCategoryValidationLabel;
  @FXML public Label productFormBrandValidationLabel;
  private Dialog<ButtonType> dialog;

  private ProductMasterFormController(Stage stage) {
    Platform.runLater(
        () -> {
          try {
            quotationProductDialogPane(stage);
          } catch (IOException ex) {
            throw new RuntimeException(ex);
          }
        });
  }

  public static ProductMasterFormController getInstance(Stage stage) {
    if (instance == null) instance = new ProductMasterFormController(stage);
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Input listeners.
    hasVariantToggle
        .selectedProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              productVariantAddBtn.setDisable(!ProductMasterViewModel.getHasVariants());
              productDetailTable.setDisable(!ProductMasterViewModel.getHasVariants());
            });
    // input validators.
    requiredValidator(
        productFormCategory, "Category is required.", productFormCategoryValidationLabel);
    requiredValidator(productFormBrand, "Brand is required.", productFormBrandValidationLabel);
    requiredValidator(productFormName, "Name is required.", productFormNameValidationLabel);
    // Input binding.
    productMasterID
        .textProperty()
        .bindBidirectional(ProductMasterViewModel.idProperty(), new NumberStringConverter());
    productFormCategory
        .valueProperty()
        .bindBidirectional(ProductMasterViewModel.categoryProperty());
    productFormBrand.valueProperty().bindBidirectional(ProductMasterViewModel.brandProperty());
    productFormName.textProperty().bindBidirectional(ProductMasterViewModel.nameProperty());
    hasVariantToggle
        .selectedProperty()
        .bindBidirectional(ProductMasterViewModel.hasVariantsProperty());
    notForSaleToggle
        .selectedProperty()
        .bindBidirectional(ProductMasterViewModel.notForSaleProperty());

    productFormCategory.setItems(ProductCategoryViewModel.categoriesList);
    productFormCategory.setConverter(
        new StringConverter<>() {
          @Override
          public String toString(ProductCategory object) {
            if (object != null) return object.getName();
            else return null;
          }

          @Override
          public ProductCategory fromString(String string) {
            return null;
          }
        });

    productFormBrand.setItems(BrandViewModel.brandsList);
    productFormBrand.setConverter(
        new StringConverter<>() {
          @Override
          public String toString(Brand object) {
            if (object != null) return object.getName();
            else return null;
          }

          @Override
          public Brand fromString(String string) {
            return null;
          }
        });
    productFormBarCodeType.setItems(FXCollections.observableArrayList(Values.BARCODETYPES));

    productAddProductBtnClicked();
    Platform.runLater(this::setupTable);
  }

  private void setupTable() {
    MFXTableColumn<ProductDetail> productName =
        new MFXTableColumn<>("Name", false, Comparator.comparing(ProductDetail::getName));
    MFXTableColumn<ProductDetail> productUOM =
        new MFXTableColumn<>(
            "Unit Of Measure", false, Comparator.comparing(ProductDetail::getUnitName));
    MFXTableColumn<ProductDetail> productSerial =
        new MFXTableColumn<>("Serial", false, Comparator.comparing(ProductDetail::getSerialNumber));

    productName.setRowCellFactory(product -> new MFXTableRowCell<>(ProductDetail::getName));
    productUOM.setRowCellFactory(product -> new MFXTableRowCell<>(ProductDetail::getUnitName));
    productSerial.setRowCellFactory(
        product -> new MFXTableRowCell<>(ProductDetail::getSerialNumber));

    productName.prefWidthProperty().bind(productDetailTable.widthProperty().multiply(.4));
    productUOM.prefWidthProperty().bind(productDetailTable.widthProperty().multiply(.4));
    productSerial.prefWidthProperty().bind(productDetailTable.widthProperty().multiply(.4));

    productDetailTable.getTableColumns().addAll(productName, productUOM, productSerial);
    productDetailTable
        .getFilters()
        .addAll(
            new IntegerFilter<>("Name", ProductDetail::getQuantity),
            new StringFilter<>("Unit Of Measure", ProductDetail::getUnitName),
            new StringFilter<>("Serial", ProductDetail::getSerialNumber));
    getProductDetailTable();
    productDetailTable.setItems(ProductDetailViewModel.productDetailTempList);
  }

  private void getProductDetailTable() {
    productDetailTable.setPrefSize(1000, 1000);
    productDetailTable.features().enableBounceEffect();
    productDetailTable.features().enableSmoothScrolling(0.5);

    productDetailTable.setTableRowFactory(
        t -> {
          MFXTableRow<ProductDetail> row = new MFXTableRow<>(productDetailTable, t);
          EventHandler<ContextMenuEvent> eventHandler =
              event -> {
                showContextMenu((MFXTableRow<ProductDetail>) event.getSource())
                    .show(
                        productDetailTable.getScene().getWindow(),
                        event.getScreenX(),
                        event.getScreenY());
                event.consume();
              };
          row.setOnContextMenuRequested(eventHandler);
          return row;
        });
  }

  private MFXContextMenu showContextMenu(MFXTableRow<ProductDetail> obj) {
    MFXContextMenu contextMenu = new MFXContextMenu(productDetailTable);
    MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
    MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

    // Actions
    // Delete
    delete.setOnAction(
        e -> {
          ProductDetailViewModel.removeProductDetail(
              obj.getData().getId(),
              ProductDetailViewModel.productDetailTempList.indexOf(obj.getData()));
          ProductDetailViewModel.getProductDetails();
          e.consume();
        });
    // Edit
    edit.setOnAction(
        e -> {
          ProductDetailViewModel.getItem(
              obj.getData().getId(),
              ProductDetailViewModel.productDetailTempList.indexOf(obj.getData()));
          dialog.showAndWait();
          e.consume();
        });

    contextMenu.addItems(edit, delete);

    return contextMenu;
  }

  private void productAddProductBtnClicked() {
    productVariantAddBtn.setOnAction(e -> dialog.showAndWait());
  }

  private void quotationProductDialogPane(Stage stage) throws IOException {
    DialogPane dialogPane = fxmlLoader("forms/ProductDetailForm.fxml").load();
    dialog = new Dialog<>();
    dialog.setDialogPane(dialogPane);
    dialog.initOwner(stage);
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.initStyle(StageStyle.UNDECORATED);
  }

  public void productSaveBtnClicked() {
    if (!productDetailTable.isDisabled()
        && ProductDetailViewModel.productDetailTempList.isEmpty()) {
      // Notify table can't be empty
      System.out.println("Table can't be empty");
      return;
    }
    if (!productFormCategoryValidationLabel.isVisible()
        && !productFormBrandValidationLabel.isVisible()
        && !productFormNameValidationLabel.isVisible()) {
      if (Integer.parseInt(productMasterID.getText()) > 0) {
        ProductMasterViewModel.updateItem(Integer.parseInt(productMasterID.getText()));
        productCancelBtnClicked();
      } else ProductMasterViewModel.saveProductMaster();
      ProductMasterViewModel.resetProperties();
      ProductDetailViewModel.productDetailTempList.clear();
    }
  }

  public void productCancelBtnClicked() {
    BaseController.navigation.navigate(Pages.getProductPane());
    ProductMasterViewModel.resetProperties();
    ProductDetailViewModel.productDetailTempList.clear();
    productFormCategoryValidationLabel.setVisible(false);
    productFormBrandValidationLabel.setVisible(false);
    productFormNameValidationLabel.setVisible(false);
  }
}
