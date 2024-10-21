package inc.nomard.spoty.core.views.forms;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.SpotyCoreResourceLoader;
import inc.nomard.spoty.core.util.validation.Constraint;
import inc.nomard.spoty.core.util.validation.Severity;
import inc.nomard.spoty.core.values.strings.Values;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.components.CustomButton;
import inc.nomard.spoty.core.views.components.validatables.ValidatableComboBox;
import inc.nomard.spoty.core.views.components.validatables.ValidatableNumberField;
import inc.nomard.spoty.core.views.components.validatables.ValidatableTextField;
import inc.nomard.spoty.core.views.util.FunctionalStringConverter;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.utils.SpotyImageUtils;
import javafx.collections.ListChangeListener;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

@Slf4j
public class ProductForm extends BorderPane {
    private static final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");
    private final ModalPane modalPane;
    public ValidatableTextField name,
            serialNumber;
    public ValidatableNumberField salePrice,
            costPrice,
            stockAlert;
    public ValidatableComboBox<Brand> brand;
    public ValidatableComboBox<ProductCategory> category;
    public ValidatableComboBox<UnitOfMeasure> unitOfMeasure;
    public ValidatableComboBox<String> barcodeType;
    public ValidatableComboBox<Discount> discount;
    public ValidatableComboBox<Tax> tax;
    public Rectangle productImageView;
    public CustomButton saveButton;
    public Button cancelButton;
    public Image productImage;
    public Label barcodeTypeValidationLabel,
            unitOfMeasureValidationLabel,
            categoryValidationLabel,
            brandValidationLabel,
            priceValidationLabel,
            nameValidationLabel;
    public HBox imageIcon;
    public VBox uploadImageButton,
            placeHolder;
    private FileChooser fileChooser;
    private List<Constraint> nameConstraints,
            priceConstraints,
            brandConstraints,
            categoryConstraints,
            unitOfMeasureConstraints,
            barcodeTypeConstraints;

    public ProductForm(ModalPane modalPane) {
        this.modalPane = modalPane;
        createDialog();
        getFieldBindings();
        getComboBoxProperties();
        dialogOnActions();
        addImage();
        requiredValidator();
        try {
            ProductViewModel.setImageFile(
                    SpotyImageUtils.getFileFromResource(
                            SpotyCoreResourceLoader.loadURL("images/product-image-placeholder.png")
                    ));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        customizeFields();
    }

    public void createDialog() {
        GridPane root = new GridPane();
        GridPane.setFillWidth(root, true);
        root.setHgap(30.0);
        root.setVgap(20.0);
        root.setPadding(new Insets(5.0));

        root.getColumnConstraints().addAll(
                createColumnConstraints(),
                createColumnConstraints()
        );

        root.getRowConstraints().addAll(
                createRowConstraints(),
                createRowConstraints(),
                createRowConstraints(),
                createRowConstraints(),
                createRowConstraints(),
                createRowConstraints()
        );

        root.add(createValidationBox(name = new ValidatableTextField(), "Name", nameValidationLabel = new Label()), 0, 0);
        root.add(createValidationBox(category = new ValidatableComboBox<>(), "Category", categoryValidationLabel = new Label()), 1, 0);
        root.add(createValidationBox(brand = new ValidatableComboBox<>(), "Brand", brandValidationLabel = new Label()), 0, 1);
        root.add(createValidationBox(unitOfMeasure = new ValidatableComboBox<>(), "Unit Of Measure", unitOfMeasureValidationLabel = new Label()), 1, 1);
        root.add(createSimpleBox(costPrice = new ValidatableNumberField(), "Cost Price (Optional)"), 0, 2);
        root.add(createValidationBox(salePrice = new ValidatableNumberField(), "Sale Price", priceValidationLabel = new Label()), 1, 2);
        root.add(createSimpleBox(discount = new ValidatableComboBox<>(), "Discount (Optional)"), 0, 3);
        root.add(createSimpleBox(tax = new ValidatableComboBox<>(), "Tax (Optional)"), 1, 3);
        root.add(createValidationBox(barcodeType = new ValidatableComboBox<>(), "Barcode Type", barcodeTypeValidationLabel = new Label()), 0, 4);
        root.add(createSimpleBox(serialNumber = new ValidatableTextField(), "Serial/Batch (Optional)"), 1, 4);
        root.add(createSimpleBox(stockAlert = new ValidatableNumberField(), "Stock Alert (Optional)"), 0, 5);
        root.add(createSimpleBox(createUploadImageBox()), 0, 6, 2, 2);

        this.setTop(buildTop());
        this.setBottom(createBottomButtons());
        this.setCenter(root);
    }

    private HBox buildTop() {
        var text = new Text("Product Form");
        text.getStyleClass().addAll(Styles.TEXT, Styles.TEXT_BOLD);

        var vbox = new VBox(text);
        vbox.setAlignment(Pos.CENTER_LEFT);
        return new HBox(vbox, new Separator());
    }

    private void customizeFields() {
        costPrice.setLeft(new Label("UGX"));
        salePrice.setLeft(new Label("UGX"));
    }

    private ColumnConstraints createColumnConstraints() {
        ColumnConstraints cc = new ColumnConstraints();
        cc.setHgrow(Priority.ALWAYS);
//        cc.setPrefWidth(1000d);
        return cc;
    }

    private RowConstraints createRowConstraints() {
        RowConstraints rc = new RowConstraints();
        rc.setVgrow(Priority.NEVER);
        return rc;
    }

    private VBox createValidationBox(ValidatableTextField textField, String promptText, Label validationLabel) {
        var label = new Label(promptText);
        textField.setPrefWidth(1000.0);

        return createValidationContainer(label, textField, validationLabel);
    }

    private <T> VBox createValidationBox(ValidatableComboBox<T> comboBox, String promptText, Label validationLabel) {
        var label = new Label(promptText);
        comboBox.setPrefWidth(1000.0);

        return createValidationContainer(label, comboBox, validationLabel);
    }

    private <T> VBox createSimpleBox(ValidatableComboBox<T> comboBox, String promptText) {
        var label = new Label(promptText);
        comboBox.setPrefWidth(1000.0);

        VBox box = new VBox(2.0);
        box.setPadding(new Insets(2.5, 0, 2.5, 0));
        box.getChildren().addAll(label, comboBox);
        return box;
    }

    private VBox createSimpleBox(TextField textField, String promptText) {
        var label = new Label(promptText);
        textField.setPrefWidth(1000.0);

        VBox box = new VBox(2.0);
        box.setPadding(new Insets(2.5, 0, 2.5, 0));
        box.getChildren().addAll(label, textField);
        return box;
    }

    private VBox createSimpleBox(AnchorPane pane) {
        var label = new Label("Product Image (Optional)");

        VBox box = new VBox(2.0);
        box.setPadding(new Insets(2.5, 0, 2.5, 0));
        box.getChildren().addAll(label, pane);
        return box;
    }

    private VBox createValidationContainer(Label label, Node control, Label validationLabel) {
        VBox box = new VBox(2.0);
        box.setPadding(new Insets(2.5, 0, 2.5, 0));

        validationLabel.setMaxWidth(Double.MAX_VALUE);
        validationLabel.setStyle("-fx-text-fill: red;");
        validationLabel.setVisible(false);
        validationLabel.setManaged(false);
        validationLabel.setWrapText(true);

        box.getChildren().addAll(label, control, validationLabel);
        return box;
    }

    private AnchorPane createUploadImageBox() {
        uploadImageButton = new VBox(8.0);
        uploadImageButton.setAlignment(Pos.CENTER);
        uploadImageButton.getStyleClass().add("card-flat");
        uploadImageButton.setCursor(Cursor.HAND);
        uploadImageButton.setPadding(new Insets(16.0));
        uploadImageButton.setPrefHeight(200d);

        imageIcon = new HBox();
        imageIcon.setAlignment(Pos.CENTER);

        var uploadImageLabel = new Label("Click to upload or Drag and drop png, jpg/jpeg or webp");
        uploadImageLabel.setTextAlignment(TextAlignment.CENTER);
        uploadImageLabel.setWrapText(true);

        placeHolder = new VBox(20, imageIcon, uploadImageLabel);
        placeHolder.setAlignment(Pos.CENTER);

        productImageView = new Rectangle();
        productImageView.setArcHeight(25.0);
        productImageView.setArcWidth(25.0);
        productImageView.setManaged(false);
        productImageView.setVisible(false);

        uploadImageButton.getChildren().addAll(placeHolder, productImageView);

        AnchorPane box = new AnchorPane();
        box.setPadding(new Insets(5));
        AnchorPane.setTopAnchor(uploadImageButton, 0.0);
        AnchorPane.setBottomAnchor(uploadImageButton, 0.0);
        AnchorPane.setLeftAnchor(uploadImageButton, 0.0);
        AnchorPane.setRightAnchor(uploadImageButton, 0.0);
        box.getChildren().add(uploadImageButton);
        return box;
    }

    private HBox createBottomButtons() {
        HBox box = new HBox(20.0);
        box.setAlignment(Pos.CENTER_RIGHT);
        box.setPadding(new Insets(10.0));

        saveButton = new CustomButton("Create");
        saveButton.getStyleClass().add(Styles.ACCENT);

        cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add(Styles.BUTTON_OUTLINED);

        box.getChildren().addAll(saveButton, cancelButton);
        return box;
    }

    private void setProductImage(String image) {
        productImage = new Image(image, 200, 200, true, false);
        if (productImage.getWidth() >= uploadImageButton.getWidth()) {
            productImageView.setWidth(uploadImageButton.getWidth() - 10);
        } else {
            productImageView.setWidth(productImage.getWidth());
        }
        if (productImage.getHeight() >= uploadImageButton.getHeight()) {
            productImageView.setHeight(uploadImageButton.getHeight() - 10);
        } else {
            productImageView.setHeight(productImage.getHeight());
        }
        productImageView.setFill(new ImagePattern(productImage));
    }

    private void getFieldBindings() {
        name.textProperty().bindBidirectional(ProductViewModel.nameProperty());
        serialNumber.textProperty().bindBidirectional(ProductViewModel.serialProperty());
        salePrice.textProperty().bindBidirectional(ProductViewModel.priceProperty());
        stockAlert.textProperty().bindBidirectional(ProductViewModel.stockAlertProperty());
        brand.valueProperty().bindBidirectional(ProductViewModel.brandProperty());
        category.valueProperty().bindBidirectional(ProductViewModel.categoryProperty());
        unitOfMeasure.valueProperty().bindBidirectional(ProductViewModel.unitProperty());
        barcodeType.valueProperty().bindBidirectional(ProductViewModel.barcodeTypeProperty());
        costPrice.textProperty().bindBidirectional(ProductViewModel.costProperty());
        discount.valueProperty().bindBidirectional(ProductViewModel.discountProperty());
        tax.valueProperty().bindBidirectional(ProductViewModel.taxProperty());
        try {
            ProductViewModel.setImageFile(
                    SpotyImageUtils.compress(
                            SpotyImageUtils.getFileFromResource(
                                    SpotyCoreResourceLoader.loadURL("images/product-image-placeholder.png")
                            )
                    )
            );
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void getComboBoxProperties() {
        // ComboBox Converters.
        StringConverter<UnitOfMeasure> uomConverter =
                FunctionalStringConverter.to(
                        unitOfMeasure -> (unitOfMeasure == null) ? "" : unitOfMeasure.getName());
        StringConverter<ProductCategory> productCategoryConverter =
                FunctionalStringConverter.to(
                        productCategory -> (productCategory == null) ? "" : productCategory.getName());
        StringConverter<Brand> brandConverter =
                FunctionalStringConverter.to(brand -> (brand == null) ? "" : brand.getName());
        StringConverter<Discount> discountConverter =
                FunctionalStringConverter.to(discount -> (discount == null) ? "" : discount.getName() + "(" + discount.getPercentage() + ")");
        StringConverter<Tax> taxConverter =
                FunctionalStringConverter.to(tax -> (tax == null) ? "" : tax.getName() + "(" + tax.getPercentage() + ")");
        // ComboBox Filter Functions.
        Function<String, Predicate<UnitOfMeasure>> uomFilterFunction =
                searchStr ->
                        unitOfMeasure ->
                                uomConverter.toString(unitOfMeasure).toLowerCase().contains(searchStr);
        Function<String, Predicate<ProductCategory>> productCategoryFilterFunction =
                searchStr ->
                        productCategory ->

                                productCategoryConverter.toString(productCategory).toLowerCase().contains(searchStr);
        Function<String, Predicate<Brand>> brandFilterFunction =
                searchStr ->
                        brand -> brandConverter.toString(brand).toLowerCase().contains(searchStr);
        // Unit of measure combo box
        unitOfMeasure.setConverter(uomConverter);
        if (UOMViewModel.getUnitsOfMeasure().isEmpty()) {
            UOMViewModel.getUnitsOfMeasure()
                    .addListener(
                            (ListChangeListener<UnitOfMeasure>)
                                    _ -> unitOfMeasure.setItems(UOMViewModel.getUnitsOfMeasure()));
        } else {
            unitOfMeasure.itemsProperty().bindBidirectional(UOMViewModel.unitsOfMeasureProperty());
        }
        // Product category combo box
        category.setConverter(productCategoryConverter);
        if (ProductCategoryViewModel.getCategories().isEmpty()) {
            ProductCategoryViewModel.getCategories()
                    .addListener(
                            (ListChangeListener<ProductCategory>)
                                    _ -> category.setItems(ProductCategoryViewModel.getCategories()));
        } else {
            category.itemsProperty().bindBidirectional(ProductCategoryViewModel.categoriesProperty());
        }
        // Brand combo box
        brand.setConverter(brandConverter);
        if (BrandViewModel.getBrands().isEmpty()) {
            BrandViewModel.getBrands()
                    .addListener(
                            (ListChangeListener<Brand>)
                                    _ -> brand.setItems(BrandViewModel.getBrands()));
        } else {
            brand.itemsProperty().bindBidirectional(BrandViewModel.brandsProperty());
        }
        // Discount combo box
        discount.setConverter(discountConverter);
        if (DiscountViewModel.getDiscounts().isEmpty()) {
            DiscountViewModel.getDiscounts()
                    .addListener(
                            (ListChangeListener<Discount>)
                                    _ -> discount.setItems(DiscountViewModel.getDiscounts()));
        } else {
            discount.itemsProperty().bindBidirectional(DiscountViewModel.discountsProperty());
        }
        // Tax combo box
        tax.setConverter(taxConverter);
        if (TaxViewModel.getTaxes().isEmpty()) {
            TaxViewModel.getTaxes()
                    .addListener(
                            (ListChangeListener<Tax>)
                                    _ -> tax.setItems(TaxViewModel.getTaxes()));
        } else {
            tax.itemsProperty().bindBidirectional(TaxViewModel.taxesProperty());
        }

        barcodeType.setItems(Values.BARCODE_TYPES);
    }

    private void dialogOnActions() {
        cancelButton.setOnAction((_) -> this.dispose());
        saveButton.setOnAction(
                (_) -> {
                    nameConstraints = name.validate();
                    priceConstraints = salePrice.validate();
                    brandConstraints = brand.validate();
                    categoryConstraints = category.validate();
                    unitOfMeasureConstraints = unitOfMeasure.validate();
                    barcodeTypeConstraints = barcodeType.validate();
                    if (!nameConstraints.isEmpty()) {
                        nameValidationLabel.setManaged(true);
                        nameValidationLabel.setVisible(true);
                        nameValidationLabel.setText(nameConstraints.getFirst().getMessage());
                        name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!priceConstraints.isEmpty()) {
                        priceValidationLabel.setManaged(true);
                        priceValidationLabel.setVisible(true);
                        priceValidationLabel.setText(priceConstraints.getFirst().getMessage());
                        salePrice.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!brandConstraints.isEmpty()) {
                        brandValidationLabel.setManaged(true);
                        brandValidationLabel.setVisible(true);
                        brandValidationLabel.setText(brandConstraints.getFirst().getMessage());
                        brand.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!categoryConstraints.isEmpty()) {
                        categoryValidationLabel.setManaged(true);
                        categoryValidationLabel.setVisible(true);
                        categoryValidationLabel.setText(categoryConstraints.getFirst().getMessage());
                        category.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!unitOfMeasureConstraints.isEmpty()) {
                        unitOfMeasureValidationLabel.setManaged(true);
                        unitOfMeasureValidationLabel.setVisible(true);
                        unitOfMeasureValidationLabel.setText(unitOfMeasureConstraints.getFirst().getMessage());
                        unitOfMeasure.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!barcodeTypeConstraints.isEmpty()) {
                        barcodeTypeValidationLabel.setManaged(true);
                        barcodeTypeValidationLabel.setVisible(true);
                        barcodeTypeValidationLabel.setText(barcodeTypeConstraints.getFirst().getMessage());
                        barcodeType.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (nameConstraints.isEmpty()
                            && priceConstraints.isEmpty()
                            && brandConstraints.isEmpty()
                            && categoryConstraints.isEmpty()
                            && unitOfMeasureConstraints.isEmpty()
                            && barcodeTypeConstraints.isEmpty()) {
                        saveButton.startLoading();
                        if (ProductViewModel.getId() > 0) {
                            ProductViewModel.updateProduct(this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
                        } else {
                            ProductViewModel.saveProduct(this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
                        }
                    }
                });
    }

    private void addImage() {
        var upload = new FontIcon(FontAwesomeRegular.FILE_IMAGE);
        upload.setIconSize(60);
        upload.setIconColor(Color.web("#C2C2C2"));
        imageIcon.getChildren().add(upload);

        if (Objects.equals(fileChooser, null)) {
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.png, *.jpeg)", "*.*.png", "*.jpeg");
            fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(extFilter);
        }
        uploadImageButton.setOnMouseClicked(_ -> {
            var file = fileChooser.showOpenDialog(new Stage());
            if (Objects.nonNull(file)) {
                if (SpotyUtils.getFileExtension(file).toLowerCase().contains("jpg")
                        || SpotyUtils.getFileExtension(file).toLowerCase().contains("jpeg")
                        || SpotyUtils.getFileExtension(file).toLowerCase().contains("webp")) {
                    setProductImage(new File(file.getPath()).toURI().toString());
                    placeHolder.setVisible(false);
                    placeHolder.setManaged(false);
                    productImageView.setVisible(true);
                    productImageView.setManaged(true);
                    try {
                        ProductViewModel.setImageFile(SpotyImageUtils.compress(file));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    SpotyUtils.errorMessage("Un-Supported file type: " + SpotyUtils.getFileExtension(file));
                }
            }
        });


        uploadImageButton.setOnDragOver(event -> {
            if (event.getGestureSource() != uploadImageButton
                    && event.getDragboard().hasFiles()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        uploadImageButton.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles() && db.getFiles().size() == 1) {
                if (SpotyUtils.getFileExtension(db.getFiles().getFirst()).toLowerCase().contains("jpg")
                        || SpotyUtils.getFileExtension(db.getFiles().getFirst()).toLowerCase().contains("jpeg")
                        || SpotyUtils.getFileExtension(db.getFiles().getFirst()).toLowerCase().contains("webp")) {
                    setProductImage(new File(db.getFiles().getFirst().getPath()).toURI().toString());
                    placeHolder.setVisible(false);
                    placeHolder.setManaged(false);
                    productImageView.setVisible(true);
                    productImageView.setManaged(true);
                    try {
                        ProductViewModel.setImageFile(SpotyImageUtils.compress(db.getFiles().getFirst()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    success = true;
                } else {
                    SpotyUtils.errorMessage("Un-Supported file type: " + SpotyUtils.getFileExtension(db.getFiles().getFirst()));
                }
            } else if (db.getFiles().size() > 1) {
                SpotyUtils.errorMessage("Un-Supported file action, drop only single file");
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void onSuccess() {
        saveButton.stopLoading();
        this.dispose();
        ProductViewModel.getAllProducts(null, null, null, null);
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint nameConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Name is required")
                        .setCondition(name.textProperty().length().greaterThan(0))
                        .get();
        name.getValidator().constraint(nameConstraint);
        Constraint priceConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Price is required")
                        .setCondition(salePrice.textProperty().length().greaterThan(0))
                        .get();
        salePrice.getValidator().constraint(priceConstraint);
        Constraint brandConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Brand is required")
                        .setCondition(brand.valueProperty().isNotNull())
                        .get();
        brand.getValidator().constraint(brandConstraint);
        Constraint categoryConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Category is required")
                        .setCondition(category.valueProperty().isNotNull())
                        .get();
        category.getValidator().constraint(categoryConstraint);
        Constraint unitOfMeasureConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Unit of measure is required")
                        .setCondition(unitOfMeasure.valueProperty().isNotNull())
                        .get();
        unitOfMeasure.getValidator().constraint(unitOfMeasureConstraint);
        Constraint barcodeTypeConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Barcode type is required")
                        .setCondition(barcodeType.valueProperty().isNotNull())
                        .get();
        barcodeType.getValidator().constraint(barcodeTypeConstraint);
        // Display error.
        name
                .getValidator()
                .validProperty()
                .addListener(
                        (_, _, newValue) -> {
                            if (newValue) {
                                nameValidationLabel.setManaged(false);
                                nameValidationLabel.setVisible(false);
                                name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        salePrice
                .getValidator()
                .validProperty()
                .addListener(
                        (_, _, newValue) -> {
                            if (newValue) {
                                priceValidationLabel.setManaged(false);
                                priceValidationLabel.setVisible(false);
                                salePrice.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        brand
                .getValidator()
                .validProperty()
                .addListener(
                        (_, _, newValue) -> {
                            if (newValue) {
                                brandValidationLabel.setManaged(false);
                                brandValidationLabel.setVisible(false);
                                brand.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        category
                .getValidator()
                .validProperty()
                .addListener(
                        (_, _, newValue) -> {
                            if (newValue) {
                                categoryValidationLabel.setManaged(false);
                                categoryValidationLabel.setVisible(false);
                                category.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        unitOfMeasure
                .getValidator()
                .validProperty()
                .addListener(
                        (_, _, newValue) -> {
                            if (newValue) {
                                unitOfMeasureValidationLabel.setManaged(false);
                                unitOfMeasureValidationLabel.setVisible(false);
                                unitOfMeasure.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        barcodeType
                .getValidator()
                .validProperty()
                .addListener(
                        (_, _, newValue) -> {
                            if (newValue) {
                                barcodeTypeValidationLabel.setManaged(false);
                                barcodeTypeValidationLabel.setVisible(false);
                                barcodeType.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }

    private void errorMessage(String message) {
        SpotyUtils.errorMessage(message);
        saveButton.stopLoading();
    }

    public void dispose() {
        modalPane.hide(true);
        modalPane.setPersistent(false);
        ProductViewModel.resetProperties();
        name = null;
        serialNumber = null;
        salePrice = null;
        stockAlert = null;
        brand = null;
        category = null;
        unitOfMeasure = null;
        barcodeType = null;
        costPrice = null;
        discount = null;
        tax = null;
        productImageView = null;
        saveButton = null;
        cancelButton = null;
        productImage = null;
        barcodeTypeValidationLabel = null;
        unitOfMeasureValidationLabel = null;
        categoryValidationLabel = null;
        brandValidationLabel = null;
        priceValidationLabel = null;
        nameValidationLabel = null;
        imageIcon = null;
        uploadImageButton = null;
        placeHolder = null;
        fileChooser = null;
        nameConstraints = null;
        priceConstraints = null;
        brandConstraints = null;
        categoryConstraints = null;
        unitOfMeasureConstraints = null;
        barcodeTypeConstraints = null;
    }
}
