package inc.nomard.spoty.core.views.forms;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import inc.nomard.spoty.core.*;
import inc.nomard.spoty.core.values.strings.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.utils.*;
import io.github.palexdev.materialfx.utils.others.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxcore.controls.Label;
import io.github.palexdev.mfxresources.fonts.*;
import java.net.*;
import java.util.*;
import java.util.function.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class ProductFormController implements Initializable {
    private static ProductFormController instance;
    private final String placeholderImage =
            SpotyCoreResourceLoader.load("images/upload_image_placeholder.png");
    private final Stage stage;
    @FXML
    public MFXTextField name,
            serialNumber,
            salePrice,
            stockAlert;
    @FXML
    public TextArea description;
    @FXML
    public MFXFilterComboBox<Brand> brand;
    @FXML
    public MFXFilterComboBox<ProductCategory> category;
    @FXML
    public MFXFilterComboBox<UnitOfMeasure> unitOfMeasure;
    @FXML
    public MFXComboBox<String> barcodeType;
    @FXML
    public MFXTextField costPrice;
    @FXML
    public MFXComboBox<Discount> discount;
    @FXML
    public MFXComboBox<Tax> tax;
    @FXML
    public Rectangle productImageView;
    @FXML
    public MFXButton saveBtn,
            cancelBtn;
    public Image productImage;
    @FXML
    public Label barcodeTypeValidationLabel,
            unitOfMeasureValidationLabel,
            categoryValidationLabel,
            brandValidationLabel,
            priceValidationLabel,
            nameValidationLabel;
    @FXML
    public HBox imageIcon;
    @FXML
    public VBox uploadImageBtn,
            placeHolder;
    private FileChooser fileChooser;
    private List<Constraint> nameConstraints,
            priceConstraints,
            brandConstraints,
            categoryConstraints,
            unitOfMeasureConstraints,
            barcodeTypeConstraints;
    private ActionEvent actionEvent = null;

    public ProductFormController(Stage stage) {
        this.stage = stage;
    }

    public static ProductFormController getInstance(Stage stage) {
        if (Objects.equals(instance, null)) {
            synchronized (ProductFormController.class) {
                instance = new ProductFormController(stage);
            }
        }
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getFieldBindings();
        getComboBoxProperties();
        dialogOnActions();
        addImage();
        requiredValidator();
        setProductImage(placeholderImage);
    }

    private void setProductImage(String image) {
        if (Objects.equals(productImage, null)) {
            productImage = new Image(image, 200, 200, true, false);
        }

        productImageView.setHeight(productImage.getHeight());
        productImageView.setWidth(productImage.getWidth());
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
        unitOfMeasure.setFilterFunction(uomFilterFunction);
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
        category.setFilterFunction(productCategoryFilterFunction);
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
        brand.setFilterFunction(brandFilterFunction);
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
        cancelBtn.setOnAction(
                (event) -> {
                    ProductViewModel.resetProperties();

                    closeDialog(event);

                    brand.clearSelection();
                    category.clearSelection();
                    unitOfMeasure.clearSelection();
                    barcodeType.clearSelection();

                    barcodeTypeValidationLabel.setVisible(false);
                    unitOfMeasureValidationLabel.setVisible(false);
                    categoryValidationLabel.setVisible(false);
                    brandValidationLabel.setVisible(false);
                    priceValidationLabel.setVisible(false);
                    nameValidationLabel.setVisible(false);

                    barcodeTypeValidationLabel.setManaged(false);
                    unitOfMeasureValidationLabel.setManaged(false);
                    categoryValidationLabel.setManaged(false);
                    brandValidationLabel.setManaged(false);
                    priceValidationLabel.setManaged(false);
                    nameValidationLabel.setManaged(false);

                    name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    salePrice.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    brand.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    category.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    unitOfMeasure.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    barcodeType.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);

                    placeHolder.setVisible(true);
                    placeHolder.setManaged(true);
                    productImageView.setVisible(false);
                    productImageView.setManaged(false);
                });
        saveBtn.setOnAction(
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
        uploadImageBtn.setOnMouseClicked(event -> {
            var file = fileChooser.showOpenDialog(new Stage());
            if (Objects.nonNull(file)) {
                setProductImage(file.getPath());
                placeHolder.setVisible(false);
                placeHolder.setManaged(false);
                productImageView.setVisible(true);
                productImageView.setManaged(true);
                System.out.println("Added: " + file.getName());
            }
        });


        uploadImageBtn.setOnDragOver(event -> {
            if (event.getGestureSource() != uploadImageBtn
                    && event.getDragboard().hasFiles()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        uploadImageBtn.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                setProductImage(db.getFiles().getFirst().getPath());
                placeHolder.setVisible(false);
                placeHolder.setManaged(false);
                productImageView.setVisible(true);
                productImageView.setManaged(true);
                System.out.println("Dropped: " + db.getString() + " " + db.getFiles().toString());
                success = true;
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void onSuccess() {
        brand.clearSelection();
        category.clearSelection();
        unitOfMeasure.clearSelection();
        barcodeType.clearSelection();
        placeHolder.setVisible(true);
        placeHolder.setManaged(true);
        productImageView.setVisible(false);
        productImageView.setManaged(false);
        closeDialog(actionEvent);
        ProductViewModel.resetProperties();
        ProductViewModel.getAllProducts(null, null);
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
                        .setCondition(brand.textProperty().length().greaterThan(0))
                        .get();
        brand.getValidator().constraint(brandConstraint);
        Constraint categoryConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Category is required")
                        .setCondition(category.textProperty().length().greaterThan(0))
                        .get();
        category.getValidator().constraint(categoryConstraint);
        Constraint unitOfMeasureConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Unit of measure is required")
                        .setCondition(unitOfMeasure.textProperty().length().greaterThan(0))
                        .get();
        unitOfMeasure.getValidator().constraint(unitOfMeasureConstraint);
        Constraint barcodeTypeConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Barcode type is required")
                        .setCondition(barcodeType.textProperty().length().greaterThan(0))
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
        if (!BaseController.getInstance(stage).morphPane.getChildren().contains(notification)) {
            BaseController.getInstance(stage).morphPane.getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent -> SpotyMessage.delay(notification, stage));
        }
    }
}
