package inc.nomard.spoty.core.views.login;

import atlantafx.base.util.*;
import inc.nomard.spoty.core.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import static inc.nomard.spoty.core.Validators.*;
import inc.nomard.spoty.core.components.animations.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.values.strings.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.adjustments.*;
import inc.nomard.spoty.core.viewModels.hrm.employee.*;
import inc.nomard.spoty.core.viewModels.purchases.*;
import inc.nomard.spoty.core.viewModels.quotations.*;
import inc.nomard.spoty.core.viewModels.requisitions.*;
import inc.nomard.spoty.core.viewModels.returns.purchases.*;
import inc.nomard.spoty.core.viewModels.returns.sales.*;
import inc.nomard.spoty.core.viewModels.sales.*;
import inc.nomard.spoty.core.viewModels.stock_ins.*;
import inc.nomard.spoty.core.viewModels.transfers.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.core.views.splash.*;
import inc.nomard.spoty.utils.*;
import static inc.nomard.spoty.utils.SpotyThreader.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxresources.fonts.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javafx.animation.*;
import javafx.application.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.stage.*;
import javafx.util.*;

public class LoginController implements Initializable {
    private final Stage stage;
    @FXML
    public MFXTextField email;
    @FXML
    public MFXPasswordField password;
    @FXML
    public Label forgotPassword,
            emailValidationLabel,
            passwordValidationLabel,
            appNameLbl,
            poweredByLbl;
    @FXML
    public MFXButton loginBtn;
    @FXML
    public MFXFontIcon closeIcon;
    @FXML
    public ActivityIndicator activityIndicator;
    @FXML
    public Pane contentPane;
    @FXML
    public AnchorPane actualContentPane;
    private SpotyMessage loginSuccess,
            badCredentialsMessage,
            errorOccurredMessage;

    public LoginController(Stage primaryStage) {
        stage = primaryStage;
    }

    private Thread dataInit() {
        return singleThreadCreator(
                "data-tracker",
                () -> {
                    try {
                        AdjustmentMasterViewModel.getAllAdjustmentMasters(this::onActivity, null, this::onFailed);
                        BranchViewModel.getAllBranches(this::onActivity, null, this::onFailed);
                        BrandViewModel.getAllBrands(this::onActivity, null, this::onFailed);
                        BankViewModel.getAllBanks(this::onActivity, null, this::onFailed);
                        CurrencyViewModel.getAllCurrencies(this::onActivity, null, this::onFailed);
                        CustomerViewModel.getAllCustomers(this::onActivity, null, this::onFailed);
                        DesignationViewModel.getAllDesignations(this::onActivity, null, this::onFailed);
                        EmploymentStatusViewModel.getAllEmploymentStatuses(this::onActivity, null, this::onFailed);
                        ExpenseCategoryViewModel.getAllCategories(this::onActivity, null, this::onFailed);
                        ExpensesViewModel.getAllExpenses(this::onActivity, null, this::onFailed);
                        ProductCategoryViewModel.getAllProductCategories(this::onActivity, null, this::onFailed);
                        ProductViewModel.getAllProducts(this::onActivity, null, this::onFailed);
                        PurchaseMasterViewModel.getAllPurchaseMasters(this::onActivity, null, this::onFailed);
                        PurchaseReturnMasterViewModel.getPurchaseReturnMasters(this::onActivity, null, this::onFailed);
                        QuotationMasterViewModel.getAllQuotationMasters(this::onActivity, null, this::onFailed);
                        RequisitionMasterViewModel.getAllRequisitionMasters(this::onActivity, null, this::onFailed);
                        SaleMasterViewModel.getAllSaleMasters(this::onActivity, null, this::onFailed);
                        SaleReturnMasterViewModel.getSaleReturnMasters(this::onActivity, null, this::onFailed);
                        StockInMasterViewModel.getAllStockInMasters(this::onActivity, null, this::onFailed);
                        SupplierViewModel.getAllSuppliers(this::onActivity, null, this::onFailed);
                        TransferMasterViewModel.getTransferMasters(this::onActivity, null, this::onFailed);
                        UOMViewModel.getAllUOMs(this::onActivity, null, this::onFailed);
                        UserViewModel.getAllUserProfiles(this::onActivity, null, this::onFailed);
                        RoleViewModel.getAllRoles(this::onActivity, null, this::onFailed);
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, SplashScreenController.class);
                    }
                });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initLoginMessages();
        email.textProperty().bindBidirectional(LoginViewModel.usernameProperty());
        password.textProperty().bindBidirectional(LoginViewModel.passwordProperty());
        // Input listeners.
        emailValidator(email, emailValidationLabel, loginBtn);
        requiredValidator();

        loginBtn.setOnAction(actionEvent -> onLoginPressed());
        Rectangle clipRect = new Rectangle(contentPane.getPrefWidth(), contentPane.getPrefHeight());
        clipRect.setArcWidth(20);
        clipRect.setArcHeight(20);
        contentPane.setClip(clipRect);
        appNameLbl.setText(Labels.APP_NAME);
        poweredByLbl.setText("Powered by " + Labels.COMPANY_NAME);
    }

    public void onLoginPressed() {
        List<Constraint> emailConstraints = email.validate();
        List<Constraint> passwordConstraints = password.validate();
        if (!emailConstraints.isEmpty()) {
            emailValidationLabel.setManaged(true);
            emailValidationLabel.setVisible(true);
            emailValidationLabel.setText(emailConstraints.getFirst().getMessage());
            email.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
        if (!passwordConstraints.isEmpty()) {
            passwordValidationLabel.setManaged(true);
            passwordValidationLabel.setVisible(true);
            passwordValidationLabel.setText(passwordConstraints.getFirst().getMessage());
            password.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
        if (emailConstraints.isEmpty()
                && passwordConstraints.isEmpty()) {
            LoginViewModel.login(this::onActivity, this::onSuccess, this::onLoginError, this::onBadCredentials);
        }
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

        addMessage(loginSuccess);
        Duration delay = Duration.millis(3500);

        KeyFrame keyFrame = new KeyFrame(delay, event -> {
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
                SpotyMessageHolder.setMessageOwner(stage);
            } catch (IOException e) {
                SpotyLogger.writeToFile(e, LoginViewModel.class);
            }
        });

        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }

    private void onBadCredentials() {
        loginBtn.setVisible(true);
        loginBtn.setManaged(true);
        activityIndicator.setVisible(false);
        activityIndicator.setManaged(false);
        addMessage(badCredentialsMessage);
    }

    private void onLoginError() {
        loginBtn.setVisible(true);
        loginBtn.setManaged(true);
        activityIndicator.setVisible(false);
        activityIndicator.setManaged(false);
        addMessage(errorOccurredMessage);
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

    private void initLoginMessages() {
        loginSuccess =
                new SpotyMessage.MessageBuilder("Authentication successful")
                        .height(60)
                        .width(300)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();

        badCredentialsMessage =
                new SpotyMessage.MessageBuilder("Wrong email or password")
                        .height(60)
                        .width(300)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .build();

        errorOccurredMessage =
                new SpotyMessage.MessageBuilder("An error occurred")
                        .height(60)
                        .width(300)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .build();

        AnchorPane.setRightAnchor(loginSuccess, 40.0);
        AnchorPane.setTopAnchor(loginSuccess, 10.0);
        AnchorPane.setRightAnchor(badCredentialsMessage, 40.0);
        AnchorPane.setTopAnchor(badCredentialsMessage, 10.0);
        AnchorPane.setRightAnchor(errorOccurredMessage, 40.0);
        AnchorPane.setTopAnchor(errorOccurredMessage, 10.0);
    }

    private void addMessage(SpotyMessage message) {
        var in = Animations.slideInDown(message, Duration.millis(250));
        if (!actualContentPane.getChildren().contains(message)) {
            actualContentPane.getChildren().add(message);
            in.playFromStart();
            in.setOnFinished(actionEvent -> delay(message));
        }
    }

    private void delay(SpotyMessage message) {
        Duration delay = Duration.seconds(3);

        KeyFrame keyFrame = new KeyFrame(delay, event -> {
            var out = Animations.slideOutUp(message, Duration.millis(250));
            out.playFromStart();
            out.setOnFinished(actionEvent -> actualContentPane.getChildren().remove(message));
        });

        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint eamilConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Email can't be empty")
                        .setCondition(email.textProperty().length().greaterThan(0))
                        .get();
        email.getValidator().constraint(eamilConstraint);
        Constraint passwordConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Password can't be empty")
                        .setCondition(password.textProperty().length().greaterThan(0))
                        .get();
        password.getValidator().constraint(passwordConstraint);
        // Display error.
        email
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                emailValidationLabel.setManaged(false);
                                emailValidationLabel.setVisible(false);
                                email.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        password
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                passwordValidationLabel.setManaged(false);
                                passwordValidationLabel.setVisible(false);
                                password.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }
}
