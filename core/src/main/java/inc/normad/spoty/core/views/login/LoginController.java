package inc.normad.spoty.core.views.login;

import inc.normad.spoty.core.SpotyCoreResourceLoader;
import inc.normad.spoty.core.components.animations.ActivityIndicator;
import inc.normad.spoty.core.components.navigation.Pages;
import inc.normad.spoty.core.components.notification.SimpleNotificationHolder;
import inc.normad.spoty.core.values.strings.Labels;
import inc.normad.spoty.core.viewModels.*;
import inc.normad.spoty.core.viewModels.adjustments.AdjustmentMasterViewModel;
import inc.normad.spoty.core.viewModels.hrm.employee.UserViewModel;
import inc.normad.spoty.core.viewModels.purchases.PurchaseMasterViewModel;
import inc.normad.spoty.core.viewModels.quotations.QuotationMasterViewModel;
import inc.normad.spoty.core.viewModels.requisitions.RequisitionMasterViewModel;
import inc.normad.spoty.core.viewModels.returns.purchases.PurchaseReturnMasterViewModel;
import inc.normad.spoty.core.viewModels.returns.sales.SaleReturnMasterViewModel;
import inc.normad.spoty.core.viewModels.sales.SaleMasterViewModel;
import inc.normad.spoty.core.viewModels.stock_ins.StockInMasterViewModel;
import inc.normad.spoty.core.viewModels.transfers.TransferMasterViewModel;
import inc.normad.spoty.core.views.BaseController;
import inc.normad.spoty.core.views.splash.SplashScreenController;
import inc.normad.spoty.utils.SpotyLogger;
import inc.normad.spoty.utils.SpotyThreader;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static inc.normad.spoty.core.SpotyCoreResourceLoader.fxmlLoader;
import static inc.normad.spoty.core.Validators.requiredValidator;
import static inc.normad.spoty.utils.SpotyThreader.singleThreadCreator;

public class LoginController implements Initializable {
    private final Stage stage;
    @FXML
    public ImageView loginImage;
    @FXML
    public MFXTextField usernameField;
    @FXML
    public MFXPasswordField passwordField;
    @FXML
    public Label forgotPassword;
    @FXML
    public MFXButton loginBtn;
    @FXML
    public Label usernameFieldValidationLabel;
    @FXML
    public Label passwordFieldValidationLabel;
    @FXML
    public MFXFontIcon closeIcon;
    @FXML
    public ActivityIndicator activityIndicator;

    public LoginController(Stage primaryStage) {
        stage = primaryStage;
    }

    @NotNull
    private Thread dataInit() {
        return singleThreadCreator(
                "data-tracker",
                () -> {
                    try {
                        Pages.setPaneWithInitData();
                        AdjustmentMasterViewModel.getAllAdjustmentMasters(this::onActivity, this::onFailed);
                        BranchViewModel.getAllBranches(this::onActivity, this::onFailed);
                        BrandViewModel.getItems(this::onActivity, this::onFailed);
                        CurrencyViewModel.getAllCurrencies(this::onActivity, this::onFailed);
                        CustomerViewModel.getAllCustomers(this::onActivity, this::onFailed);
                        ExpenseCategoryViewModel.getAllCategories(this::onActivity, this::onFailed);
                        ExpensesViewModel.getAllExpenses(this::onActivity, this::onFailed);
                        ProductCategoryViewModel.getItems(this::onActivity, this::onFailed);
                        ProductViewModel.getAllProducts(this::onActivity, this::onFailed);
                        PurchaseMasterViewModel.getPurchaseMasters(this::onActivity, this::onFailed);
                        PurchaseReturnMasterViewModel.getPurchaseReturnMasters();
                        QuotationMasterViewModel.getQuotationMasters(this::onActivity, this::onFailed);
                        RequisitionMasterViewModel.getRequisitionMasters(this::onActivity, this::onFailed);
                        SaleMasterViewModel.getSaleMasters(this::onActivity, this::onFailed);
                        SaleReturnMasterViewModel.getSaleReturnMasters();
                        StockInMasterViewModel.getStockInMasters(this::onActivity, this::onFailed);
                        SupplierViewModel.getAllSuppliers(this::onActivity, this::onFailed);
                        TransferMasterViewModel.getTransferMasters(this::onActivity, this::onFailed);
                        UOMViewModel.getItems(this::onActivity, this::onFailed);
                        UserViewModel.getAllUserProfiles(this::onActivity, this::onFailed);
                        RoleViewModel.getAllRoles();
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, SplashScreenController.class);
                    }
                });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image(SpotyCoreResourceLoader.load("images/login-img-1.png"));

        loginImage.setImage(image);
        loginImage.setCache(true);
        loginImage.setCacheHint(CacheHint.SPEED);


        usernameField.textProperty().bindBidirectional(LoginViewModel.usernameProperty());
        passwordField.textProperty().bindBidirectional(LoginViewModel.passwordProperty());
        // Input listeners.
        requiredValidator(
                usernameField, "No email provided.", usernameFieldValidationLabel, loginBtn);
        requiredValidator(
                passwordField, "No password provided.", passwordFieldValidationLabel, loginBtn);

        loginBtn.setOnAction(actionEvent -> {
            onLoginPressed();
        });
    }

    public void onLoginPressed() {
        LoginViewModel.login(this::onActivity, this::onSuccess, this::onFailed);
    }

    private void onActivity() {
        loginBtn.setVisible(false);
        loginBtn.setManaged(false);
        activityIndicator.setVisible(true);
        activityIndicator.setManaged(true);
    }

    private void onSuccess() {
        var dataInit = dataInit();

        try {
            dataInit.join();
        } catch (InterruptedException e) {
            SpotyLogger.writeToFile(e, LoginViewModel.class);
        }

        stage.hide();

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        try {
            // Set base view.
            FXMLLoader loader = fxmlLoader("views/Base.fxml");
            loader.setControllerFactory(c -> BaseController.getInstance(stage));
            // Base view parent.
            Parent root = loader.load();
            Scene scene = new Scene(root);
            // Set application scene theme to MFX modern themes.
            io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(scene);
            // Fixes black edges showing in main app scene.
            scene.setFill(null);
            stage.setScene(scene);
            // stage.initStyle(StageStyle.TRANSPARENT);
            // Set initial window size.
            stage.setHeight(primScreenBounds.getHeight());
            stage.setWidth(primScreenBounds.getWidth());
            // Set window title name, this name will only be seen when cursor hovers over app icon in
            // taskbar. Not necessary too but added since other apps also do this.
            stage.setTitle(Labels.APP_NAME);
            stage.getIcons().add(new Image(SpotyCoreResourceLoader.load("icon.png")));
            stage.show();
            // Set window position to center of screen.
            // This isn't necessary, just felt like adding it here.
            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
            // Initialize app notification handler.
            SimpleNotificationHolder.setNotificationOwner(stage);
        } catch (IOException e) {
            SpotyLogger.writeToFile(e, LoginViewModel.class);
        }
        stage.show();
    }

    private void onFailed() {
        loginBtn.setVisible(true);
        loginBtn.setManaged(true);
        activityIndicator.setVisible(false);
        activityIndicator.setManaged(false);
    }

    public void closeIconClicked() {
        stage.hide();
        stage.close();
        SpotyThreader.disposeSpotyThreadPool();
        Platform.exit();
        System.exit(0);
    }
}
