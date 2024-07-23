package inc.nomard.spoty.core.views.forms;

import atlantafx.base.theme.*;
import atlantafx.base.util.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import inc.nomard.spoty.core.*;
import inc.nomard.spoty.core.values.strings.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.components.label_components.controls.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.utils.*;
import io.github.palexdev.materialfx.utils.others.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import io.github.palexdev.mfxcomponents.controls.buttons.*;
import io.github.palexdev.mfxcore.controls.*;
import io.github.palexdev.mfxcore.controls.Label;
import io.github.palexdev.mfxresources.fonts.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.function.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.*;
import lombok.extern.java.*;

@Log
public class ProductForm extends ModalPage {
    public LabeledTextField name,
            serialNumber,
            salePrice,
            stockAlert;
    public LabeledTextArea description;
    public LabeledComboBox<Brand> brand;
    public LabeledComboBox<ProductCategory> category;
    public LabeledComboBox<UnitOfMeasure> unitOfMeasure;
    public LabeledComboBox<String> barcodeType;
    public LabeledTextField costPrice;
    public LabeledComboBox<Discount> discount;
    public LabeledComboBox<Tax> tax;
    public Rectangle productImageView;
    public Button saveButton,
            cancelButton;
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
    private ActionEvent actionEvent = null;

    public ProductForm() {
        init();
    }

    @SneakyThrows
    public void init() {
        createDialog();
        getFieldBindings();
        getComboBoxProperties();
        dialogOnActions();
        addImage();
        requiredValidator();
        ProductViewModel.setImageFile(
                SpotyImageUtils.getFileFromResource(
                        SpotyCoreResourceLoader.loadURL("images/product-image-placeholder.png")
                ));
    }

    public void createDialog() {
        GridPane root = new GridPane();
        root.setHgap(30.0);
        root.setVgap(20.0);
        root.setPadding(new Insets(5.0));

        root.getColumnConstraints().addAll(
                createColumnConstraints(300.0),
                createColumnConstraints(300.0),
                createColumnConstraints(220.0)
        );

        root.getRowConstraints().addAll(
                createRowConstraints(),
                createRowConstraints(),
                createRowConstraints(),
                createRowConstraints(),
                createRowConstraints(),
                createRowConstraints()
        );

        root.add(createValidationBox(name = new LabeledTextField(), "Name", nameValidationLabel = new Label(), 400.0), 0, 0);
        root.add(createValidationBox(category = new LabeledComboBox<>(), "Category", categoryValidationLabel = new Label(), 400.0), 1, 0);
        root.add(createValidationBox(brand = new LabeledComboBox<>(), "Brand", brandValidationLabel = new Label(), 400.0), 0, 1);
        root.add(createValidationBox(unitOfMeasure = new LabeledComboBox<>(), "Unit Of Measure", unitOfMeasureValidationLabel = new Label(), 400.0), 1, 1);
        root.add(createSimpleBox(costPrice = new LabeledTextField(), "Cost Price", 400.0), 0, 2);
        root.add(createValidationBox(salePrice = new LabeledTextField(), "Sale Price", priceValidationLabel = new Label(), 400.0), 1, 2);
        root.add(createSimpleBox(discount = new LabeledComboBox<>(), "Discount", 400.0), 0, 3);
        root.add(createSimpleBox(tax = new LabeledComboBox<>(), "Tax", 400.0), 1, 3);
        root.add(createValidationBox(barcodeType = new LabeledComboBox<>(), "Barcode Type", barcodeTypeValidationLabel = new Label(), 400.0), 0, 4);
        root.add(createSimpleBox(serialNumber = new LabeledTextField(), "Serial/Batch", 400.0), 1, 4);
        root.add(createSimpleBox(stockAlert = new LabeledTextField(), "Stock Alert", 400.0), 0, 5);
        root.add(createSimpleTextArea(description = new LabeledTextArea(), "Description", 400.0, 100.0), 1, 5);

        root.add(createUploadImageBox(), 2, 0, 1, 6);

        this.setBottom(createBottomButtons());
        this.setCenter(root);
    }

    private ColumnConstraints createColumnConstraints(double prefWidth) {
        ColumnConstraints cc = new ColumnConstraints();
        cc.setHgrow(Priority.SOMETIMES);
        cc.setPrefWidth(prefWidth);
        return cc;
    }

    private RowConstraints createRowConstraints() {
        RowConstraints rc = new RowConstraints();
        rc.setVgrow(Priority.NEVER);
        return rc;
    }

    private VBox createValidationBox(LabeledTextField textField, String promptText, Label validationLabel, double width) {
        textField.setLabel(promptText);
        textField.setPrefWidth(width);

        return createValidationContainer(textField, validationLabel);
    }

    private <T> VBox createValidationBox(LabeledComboBox<T> comboBox, String promptText, Label validationLabel, double width) {
        comboBox.setLabel(promptText);
        comboBox.setPrefWidth(width);

        return createValidationContainer(comboBox, validationLabel);
    }

    private <T> VBox createSimpleBox(LabeledComboBox<T> comboBox, String promptText, double width) {
        comboBox.setLabel(promptText);
        comboBox.setPrefWidth(width);

        VBox box = new VBox(2.0);
        box.setPadding(new Insets(2.5, 0, 2.5, 0));
        box.getChildren().add(comboBox);
        return box;
    }

    private VBox createSimpleBox(LabeledTextField textField, String promptText, double width) {
        textField.setLabel(promptText);
        textField.setPrefWidth(width);

        VBox box = new VBox(2.0);
        box.setPadding(new Insets(2.5, 0, 2.5, 0));
        box.getChildren().add(textField);
        return box;
    }

    private VBox createSimpleTextArea(LabeledTextArea textArea, String promptText, double width, double height) {
        textArea.setLabel(promptText);
        textArea.setPrefWidth(width);
        textArea.setPrefHeight(height);

        VBox box = new VBox(2.0);
        box.setPadding(new Insets(2.5, 0, 2.5, 0));
        box.getChildren().add(textArea);
        return box;
    }

    private VBox createValidationContainer(Node control, Label validationLabel) {
        VBox box = new VBox(2.0);
        box.setPadding(new Insets(2.5, 0, 2.5, 0));

        validationLabel.setMaxWidth(Double.MAX_VALUE);
        validationLabel.setStyle("-fx-text-fill: red;");
        validationLabel.setVisible(false);
        validationLabel.setManaged(false);
        validationLabel.setWrapText(true);

        box.getChildren().addAll(control, validationLabel);
        return box;
    }

    private AnchorPane createUploadImageBox() {
        uploadImageButton = new VBox(8.0);
        uploadImageButton.setAlignment(Pos.CENTER);
        uploadImageButton.getStyleClass().add("card-flat");
        uploadImageButton.setCursor(Cursor.HAND);
        uploadImageButton.setPadding(new Insets(16.0));

        imageIcon = new HBox();
        imageIcon.setAlignment(Pos.CENTER);

        var uploadImageLabel = new Label("Click to upload or Drag and drop png or jpg. (max. size 800 x 400 px)");
        uploadImageLabel.setTextAlignment(TextAlignment.CENTER);
        uploadImageLabel.setWrapText(true);

        placeHolder = new VBox(imageIcon, uploadImageLabel);

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

        saveButton = new Button("Create");
        saveButton.setDefaultButton(true);

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
        description.textProperty().bindBidirectional(ProductViewModel.descriptionProperty());
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
                                StringUtils.containsIgnoreCase(uomConverter.toString(unitOfMeasure), searchStr);
        Function<String, Predicate<ProductCategory>> productCategoryFilterFunction =
                searchStr ->
                        productCategory ->
                                StringUtils.containsIgnoreCase(
                                        productCategoryConverter.toString(productCategory), searchStr);
        Function<String, Predicate<Brand>> brandFilterFunction =
                searchStr ->
                        brand -> StringUtils.containsIgnoreCase(brandConverter.toString(brand), searchStr);
        // Unit of measure combo box
        unitOfMeasure.setConverter(uomConverter);
        if (UOMViewModel.getUnitsOfMeasure().isEmpty()) {
            UOMViewModel.getUnitsOfMeasure()
                    .addListener(
                            (ListChangeListener<UnitOfMeasure>)
                                    c -> unitOfMeasure.setItems(UOMViewModel.getUnitsOfMeasure()));
        } else {
            unitOfMeasure.itemsProperty().bindBidirectional(UOMViewModel.unitsOfMeasureProperty());
        }
        // Product category combo box
        category.setConverter(productCategoryConverter);
        if (ProductCategoryViewModel.getCategories().isEmpty()) {
            ProductCategoryViewModel.getCategories()
                    .addListener(
                            (ListChangeListener<ProductCategory>)
                                    c -> category.setItems(ProductCategoryViewModel.getCategories()));
        } else {
            category.itemsProperty().bindBidirectional(ProductCategoryViewModel.categoriesProperty());
        }
        // Brand combo box
        brand.setConverter(brandConverter);
        if (BrandViewModel.getBrands().isEmpty()) {
            BrandViewModel.getBrands()
                    .addListener(
                            (ListChangeListener<Brand>)
                                    c -> brand.setItems(BrandViewModel.getBrands()));
        } else {
            brand.itemsProperty().bindBidirectional(BrandViewModel.brandsProperty());
        }
        // Discount combo box
        discount.setConverter(discountConverter);
        if (DiscountViewModel.getDiscounts().isEmpty()) {
            DiscountViewModel.getDiscounts()
                    .addListener(
                            (ListChangeListener<Discount>)
                                    c -> discount.setItems(DiscountViewModel.getDiscounts()));
        } else {
            discount.itemsProperty().bindBidirectional(DiscountViewModel.discountsProperty());
        }
        // Tax combo box
        tax.setConverter(taxConverter);
        if (TaxViewModel.getTaxes().isEmpty()) {
            TaxViewModel.getTaxes()
                    .addListener(
                            (ListChangeListener<Tax>)
                                    c -> tax.setItems(TaxViewModel.getTaxes()));
        } else {
            tax.itemsProperty().bindBidirectional(TaxViewModel.taxesProperty());
        }

        barcodeType.setItems(FXCollections.observableArrayList(Values.BARCODE_TYPES));
    }

    private void dialogOnActions() {
        cancelButton.setOnAction(
                (event) -> {
                    closeDialog(event);
                    ProductViewModel.resetProperties();
                    this.dispose();
                });
        saveButton.setOnAction(
                (event) -> {
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
                        MFXStageDialog dialog = (MFXStageDialog) name.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!priceConstraints.isEmpty()) {
                        priceValidationLabel.setManaged(true);
                        priceValidationLabel.setVisible(true);
                        priceValidationLabel.setText(priceConstraints.getFirst().getMessage());
                        salePrice.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) salePrice.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!brandConstraints.isEmpty()) {
                        brandValidationLabel.setManaged(true);
                        brandValidationLabel.setVisible(true);
                        brandValidationLabel.setText(brandConstraints.getFirst().getMessage());
                        brand.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) brand.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!categoryConstraints.isEmpty()) {
                        categoryValidationLabel.setManaged(true);
                        categoryValidationLabel.setVisible(true);
                        categoryValidationLabel.setText(categoryConstraints.getFirst().getMessage());
                        category.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) category.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!unitOfMeasureConstraints.isEmpty()) {
                        unitOfMeasureValidationLabel.setManaged(true);
                        unitOfMeasureValidationLabel.setVisible(true);
                        unitOfMeasureValidationLabel.setText(unitOfMeasureConstraints.getFirst().getMessage());
                        unitOfMeasure.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) unitOfMeasure.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!barcodeTypeConstraints.isEmpty()) {
                        barcodeTypeValidationLabel.setManaged(true);
                        barcodeTypeValidationLabel.setVisible(true);
                        barcodeTypeValidationLabel.setText(barcodeTypeConstraints.getFirst().getMessage());
                        barcodeType.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) barcodeType.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (nameConstraints.isEmpty()
                            && priceConstraints.isEmpty()
                            && brandConstraints.isEmpty()
                            && categoryConstraints.isEmpty()
                            && unitOfMeasureConstraints.isEmpty()
                            && barcodeTypeConstraints.isEmpty()) {
                        actionEvent = event;
                        if (ProductViewModel.getId() > 0) {
                            ProductViewModel.updateProduct(this::onSuccess, this::successMessage, this::errorMessage);
                            return;
                        }
                        ProductViewModel.saveProduct(this::onSuccess, this::successMessage, this::errorMessage);
                    }
                });
    }

    private void addImage() {
        var upload = new MFXFontIcon();
        upload.setIconsProvider(IconsProviders.FONTAWESOME_REGULAR);
        upload.setDescription("far-file-image");
        upload.setSize(60);
        upload.setColor(Color.web("#C2C2C2"));
        imageIcon.getChildren().add(upload);

        if (Objects.equals(fileChooser, null)) {
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.png, *.jpeg)", "*.*.png", "*.jpeg");
            fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(extFilter);
        }
        uploadImageButton.setOnMouseClicked(event -> {
            var file = fileChooser.showOpenDialog(new Stage());
            if (Objects.nonNull(file)) {
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
            if (db.hasFiles()) {
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
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void onSuccess() {
        closeDialog(actionEvent);
        ProductViewModel.resetProperties();
        ProductViewModel.getAllProducts(null, null);
        this.dispose();
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
                        (observable, oldValue, newValue) -> {
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
                        (observable, oldValue, newValue) -> {
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
                        (observable, oldValue, newValue) -> {
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
                        (observable, oldValue, newValue) -> {
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
                        (observable, oldValue, newValue) -> {
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
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                barcodeTypeValidationLabel.setManaged(false);
                                barcodeTypeValidationLabel.setVisible(false);
                                barcodeType.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }

    private void successMessage(String message) {
        displayNotification(message, MessageVariants.SUCCESS, "fas-circle-check");
    }

    private void errorMessage(String message) {
        displayNotification(message, MessageVariants.ERROR, "fas-triangle-exclamation");
    }

    private void displayNotification(String message, MessageVariants type, String icon) {
        SpotyMessage notification = new SpotyMessage.MessageBuilder(message)
                .duration(MessageDuration.SHORT)
                .icon(icon)
                .type(type)
                .height(60)
                .build();
        AnchorPane.setTopAnchor(notification, 5.0);
        AnchorPane.setRightAnchor(notification, 5.0);

        var in = Animations.slideInDown(notification, Duration.millis(250));
        if (!AppManager.getMorphPane().getChildren().contains(notification)) {
            AppManager.getMorphPane().getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent -> SpotyMessage.delay(notification));
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        name = null;
        serialNumber = null;
        salePrice = null;
        stockAlert = null;
        description = null;
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
        actionEvent = null;
    }
}
