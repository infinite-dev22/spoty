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

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXElevatedButton;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.infinite.spoty.database.dao.ProductDetailDao;
import org.infinite.spoty.database.models.ProductDetail;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.viewModels.ProductDetailViewModel;
import org.infinite.spoty.viewModels.ProductMasterViewModel;
import org.infinite.spoty.viewModels.BranchViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

@SuppressWarnings("unchecked")
public class ProductMasterFormController implements Initializable {
    public MFXTextField productDetailID = new MFXTextField();
    public MFXTextField productMasterID = new MFXTextField();
    @FXML
    public MFXFilterComboBox<Branch> productBranchId;
    @FXML
    public MFXDatePicker productDate;
    @FXML
    public MFXTableView<ProductDetail> productDetailTable;
    @FXML
    public MFXTextField productNote;
    @FXML
    public AnchorPane productFormContentPane;
    @FXML
    public Label productFormTitle;
    @FXML
    public MFXElevatedButton productProductAddBtn;
    private Dialog<ButtonType> dialog;

    public ProductMasterFormController(Stage stage) {
        Platform.runLater(() -> {
            try {
                quotationProductDialogPane(stage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input listener.
//        productBranchId.textProperty().addListener((observable, oldValue, newValue) -> productBranchId.setTrailingIcon(null));
//        productDate.textProperty().addListener((observable, oldValue, newValue) -> productDate.setTrailingIcon(null));
        // Input binding.
        productDetailID.textProperty().bindBidirectional(ProductDetailViewModel.idProperty());
        productMasterID.textProperty().bindBidirectional(ProductMasterViewModel.idProperty(), new NumberStringConverter());
        productBranchId.valueProperty().bindBidirectional(ProductMasterViewModel.branchProperty());
        // TODO: Add multiple selection for branches.
        productBranchId.setItems(BranchViewModel.branchesList);
        productBranchId.setConverter(new StringConverter<>() {
            @Override
            public String toString(Branch object) {
                if (object != null)
                    return object.getName();
                else
                    return null;
            }

            @Override
            public Branch fromString(String string) {
                return null;
            }
        });
//        productDate.textProperty().bindBidirectional(ProductMasterViewModel.dateProperty());
        productNote.textProperty().bindBidirectional(ProductMasterViewModel.noteProperty());

        productAddProductBtnClicked();
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<ProductDetail> productQuantity = new MFXTableColumn<>("Quantity", false, Comparator.comparing(ProductDetail::getQuantity));
        MFXTableColumn<ProductDetail> productSerial = new MFXTableColumn<>("Serial", false, Comparator.comparing(ProductDetail::getSerialNumber));

        productQuantity.setRowCellFactory(product -> new MFXTableRowCell<>(ProductDetail::getQuantity));
        productSerial.setRowCellFactory(product -> new MFXTableRowCell<>(ProductDetail::getSerialNumber));

        productQuantity.prefWidthProperty().bind(productDetailTable.widthProperty().multiply(.4));
        productSerial.prefWidthProperty().bind(productDetailTable.widthProperty().multiply(.4));

        productDetailTable.getTableColumns().addAll(productQuantity, productSerial);
        productDetailTable.getFilters().addAll(
                new IntegerFilter<>("Quantity", ProductDetail::getQuantity),
                new StringFilter<>("Serial", ProductDetail::getSerialNumber)
        );
        getProductDetailTable();
        productDetailTable.setItems(ProductDetailViewModel.productDetailTempList);
    }

    private void getProductDetailTable() {
        productDetailTable.setPrefSize(1000, 1000);
        productDetailTable.features().enableBounceEffect();
        productDetailTable.features().enableSmoothScrolling(0.5);

        productDetailTable.setTableRowFactory(t -> {
            MFXTableRow<ProductDetail> row = new MFXTableRow<>(productDetailTable, t);
            EventHandler<ContextMenuEvent> eventHandler = event -> {
                showContextMenu((MFXTableRow<ProductDetail>) event.getSource())
                        .show(productDetailTable.getScene().getWindow(), event.getScreenX(), event.getScreenY());
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
        delete.setOnAction(e -> {
            ProductDetailViewModel.getItem(obj.getData(), ProductDetailViewModel.productDetailTempList.indexOf(obj.getData()));
            try {
                if (Integer.parseInt(productDetailID.getText()) > 0)
                    ProductDetailDao.deleteProductDetail(Integer.parseInt(productDetailID.getText()));
            } catch (NumberFormatException ignored) {
                ProductDetailViewModel.removeProductDetail(ProductDetailViewModel.productDetailTempList.indexOf(obj.getData()));
            }
            ProductDetailViewModel.getProductDetails();
            e.consume();
        });
        // Edit
        edit.setOnAction(e -> {
            ProductDetailViewModel.getItem(obj.getData(), ProductDetailViewModel.productDetailTempList.indexOf(obj.getData()));
            dialog.showAndWait();
            e.consume();
        });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    private void productAddProductBtnClicked() {
        productProductAddBtn.setOnAction(e -> dialog.showAndWait());
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
        MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

        if (productBranchId.getText().length() == 0) {
            productBranchId.setTrailingIcon(icon);
        }
        if (productDate.getText().length() == 0) {
            productDate.setTrailingIcon(icon);
        }
        if (productDetailTable.getTableColumns().isEmpty()) {
            // Notify table can't be empty
            System.out.println("Table can't be empty");
        }
        if (productBranchId.getText().length() > 0
                && productDate.getText().length() > 0
                && !productDetailTable.getTableColumns().isEmpty()) {
            if (Integer.parseInt(productMasterID.getText()) > 0) {
                ProductMasterViewModel.updateItem(Integer.parseInt(productMasterID.getText()));
                productCancelBtnClicked();
            } else
                ProductMasterViewModel.saveProductMaster();
            ProductMasterViewModel.resetProperties();
            ProductDetailViewModel.productDetailTempList.clear();
        }
    }

    public void productCancelBtnClicked() {
        ProductMasterViewModel.resetProperties();
        ProductDetailViewModel.productDetailTempList.clear();
        ((StackPane) productFormContentPane.getParent()).getChildren().get(0).setVisible(true);
        ((StackPane) productFormContentPane.getParent()).getChildren().remove(1);
    }
}
