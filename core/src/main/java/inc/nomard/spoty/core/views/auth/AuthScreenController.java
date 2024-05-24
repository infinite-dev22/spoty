package inc.nomard.spoty.core.views.auth;

import atlantafx.base.util.*;
import inc.nomard.spoty.core.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.values.strings.*;
import static inc.nomard.spoty.core.values.strings.Values.*;
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
import static io.github.palexdev.materialfx.utils.StringUtils.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxresources.fonts.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javafx.animation.*;
import javafx.application.*;
import javafx.beans.binding.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.stage.*;
import javafx.util.*;

public class AuthScreenController implements Initializable {
    private final Stage stage;
    @FXML
    public MFXTextField email,
            signUpEmail,
            phoneNumber,
            otherName,
            lastName,
            firstName;
    @FXML
    public MFXPasswordField password,
            confirmPassword,
            signUpPassword;
    @FXML
    public Label forgotPassword,
            emailValidationLabel,
            passwordValidationLabel,
            loginAppNameLbl,
            loginPoweredByLbl,
            kycAppNameLbl,
            kycPoweredByLbl,
            signUpEmailValidationLabel,
            phoneNumberValidationLabel,
            lastNameValidationLabel,
            firstNameValidationLabel,
            signUpPoweredByLbl,
            signUpAppNameLbl,
            loginLink,
            confirmPasswordValidationLabel,
            signUpPasswordValidationLabel,
            signUpLink;
    @FXML
    public MFXButton loginBtn,
            nextBtn,
            backBtn,
            registerBtn,
            signUpBack;
    @FXML
    public MFXFontIcon closeLoginIcon,
            closeSignupIcon;
    @FXML
    public Pane contentPane;
    @FXML
    public AnchorPane actualContentPane;
    @FXML
    public VBox loginScreen,
            kycScreen,
            authCreateScreen;
    @FXML
    public HBox registerScreen;
    private SpotyMessage loginSuccess,
            signUpSuccess,
            badCredentialsMessage,
            errorOccurredMessage;

    public AuthScreenController(Stage primaryStage) {
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
                        UserViewModel.getAllUsers(this::onActivity, null, this::onFailed);
                        RoleViewModel.getAllRoles(this::onActivity, null, this::onFailed);
                        PermissionsViewModel.getAllPermissions(this::onActivity, null, this::onFailed);
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, SplashScreenController.class);
                    }
                });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initLoginMessages();
        signUpEmail.textProperty().bindBidirectional(SignupViewModel.emailProperty());
        phoneNumber.textProperty().bindBidirectional(SignupViewModel.phoneProperty());
        otherName.textProperty().bindBidirectional(SignupViewModel.otherNameProperty());
        lastName.textProperty().bindBidirectional(SignupViewModel.lastNameProperty());
        firstName.textProperty().bindBidirectional(SignupViewModel.firstNameProperty());
        signUpPassword.textProperty().bindBidirectional(SignupViewModel.passwordProperty());
        confirmPassword.textProperty().bindBidirectional(SignupViewModel.confirmPasswordProperty());
        email.textProperty().bindBidirectional(LoginViewModel.emailProperty());
        password.textProperty().bindBidirectional(LoginViewModel.passwordProperty());
        // Input listeners.
        requiredValidator();
        focusControl();
        signUpLink.setOnMouseClicked(event -> signUpLinkAction());
        loginLink.setOnMouseClicked(event -> backToLogin());

        loginBtn.setOnAction(actionEvent -> onLoginPressed());
        Rectangle clipRect = new Rectangle(contentPane.getPrefWidth(), contentPane.getPrefHeight());
        clipRect.setArcWidth(20);
        clipRect.setArcHeight(20);
        contentPane.setClip(clipRect);
        loginAppNameLbl.setText(Labels.APP_NAME);
        loginPoweredByLbl.setText("Powered by " + Labels.COMPANY_NAME);
    }

    private void onLoginPressed() {
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
            LoginViewModel.login(this::onActivity, this::onLoginSuccess, this::onLoginError, this::onBadCredentials);
        }
    }

    private void onActivity() {
//        loginBtn.setDisable(true);
//        loginBtn.setManaged(false);
//        activityIndicator.setVisible(true);
//        activityIndicator.setManaged(true);
    }

    private void onLoginSuccess() {
        var dataInit = dataInit();

        try {
            dataInit.join();
        } catch (InterruptedException e) {
            SpotyLogger.writeToFile(e, AuthScreenController.class);
        }

        addMessage(loginSuccess);
        Duration delay = Duration.millis(3500);

        KeyFrame keyFrame = new KeyFrame(delay, event -> {
            stage.hide();

            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            try {
                LoginViewModel.resetProperties();
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

    private void onSignupSuccess() {
        addMessage(signUpSuccess);
        SignupViewModel.resetProperties();
        backToLogin();
    }

    private void onBadCredentials() {
        loginBtn.setDisable(false);
//        loginBtn.setManaged(true);
//        activityIndicator.setVisible(false);
//        activityIndicator.setManaged(false);
        addMessage(badCredentialsMessage);
    }

    private void onLoginError() {
        loginBtn.setDisable(false);
//        loginBtn.setManaged(true);
//        activityIndicator.setVisible(false);
//        activityIndicator.setManaged(false);
        addMessage(errorOccurredMessage);
    }

    private void onFailed() {
        loginBtn.setDisable(false);
//        loginBtn.setManaged(true);
//        activityIndicator.setVisible(false);
//        activityIndicator.setManaged(false);
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

        signUpSuccess =
                new SpotyMessage.MessageBuilder("Account created successfully")
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
        AnchorPane.setRightAnchor(signUpSuccess, 40.0);
        AnchorPane.setTopAnchor(signUpSuccess, 10.0);
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

    private void requiredValidator() {
        // Sign up form validators.
        Constraint firstNameConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("First name is required")
                        .setCondition(firstName.textProperty().length().greaterThan(0))
                        .get();
        firstName.getValidator().constraint(firstNameConstraint);
        Constraint lastNameConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Last name is required")
                        .setCondition(lastName.textProperty().length().greaterThan(0))
                        .get();
        lastName.getValidator().constraint(lastNameConstraint);
        Constraint phoneConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Phone is required")
                        .setCondition(phoneNumber.textProperty().length().greaterThan(0))
                        .get();
        phoneNumber.getValidator().constraint(phoneConstraint);
        Constraint signUpEmailConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Email is required")
                        .setCondition(signUpEmail.textProperty().length().greaterThan(0))
                        .get();
        Constraint signUpEmailAlphaCharsConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Invalid email")
                        .setCondition(
                                Bindings.createBooleanBinding(
                                        () -> containsAny(signUpEmail.getText(), "", ALPHANUMERIC),
                                        signUpEmail.textProperty()))
                        .get();
        Constraint signUpEmailSpecialCharsConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Invalid email")
                        .setCondition(
                                Bindings.createBooleanBinding(
                                        () -> containsAll(signUpEmail.getText(), "", SPECIALS), signUpEmail.textProperty()))
                        .get();
        signUpEmail.getValidator().constraint(signUpEmailConstraint).constraint(signUpEmailAlphaCharsConstraint).constraint(signUpEmailSpecialCharsConstraint);
        Constraint signUpPasswordConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Password is required")
                        .setCondition(signUpPassword.textProperty().length().greaterThan(0))
                        .get();
        signUpPassword.getValidator()
                .constraint(signUpPasswordConstraint);
        Constraint passwordMatchConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Passwords do not match")
                        .setCondition(
                                Bindings.createBooleanBinding(
                                        () -> confirmPassword.getText().equals(signUpPassword.getText()),
                                        signUpPassword.textProperty(),
                                        confirmPassword.textProperty()))
                        .get();
        confirmPassword.getValidator().constraint(passwordMatchConstraint);
        // Login form validators.
        Constraint emailConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Email is required")
                        .setCondition(email.textProperty().length().greaterThan(0))
                        .get();
        Constraint alphaCharsConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Invalid email")
                        .setCondition(
                                Bindings.createBooleanBinding(
                                        () -> containsAny(email.getText(), "", ALPHANUMERIC),
                                        email.textProperty()))
                        .get();
        Constraint specialCharsConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Invalid email")
                        .setCondition(
                                Bindings.createBooleanBinding(
                                        () -> containsAll(email.getText(), "", SPECIALS), email.textProperty()))
                        .get();
        email.getValidator().constraint(emailConstraint).constraint(alphaCharsConstraint).constraint(specialCharsConstraint);
        Constraint passwordConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Password is required")
                        .setCondition(password.textProperty().length().greaterThan(0))
                        .get();
        password.getValidator().constraint(passwordConstraint);
        // Display error.
        firstName
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                firstNameValidationLabel.setManaged(false);
                                firstNameValidationLabel.setVisible(false);
                                firstName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        lastName
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                lastNameValidationLabel.setManaged(false);
                                lastNameValidationLabel.setVisible(false);
                                lastName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        phoneNumber
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                phoneNumberValidationLabel.setManaged(false);
                                phoneNumberValidationLabel.setVisible(false);
                                phoneNumber.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        signUpPassword
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                signUpPasswordValidationLabel.setManaged(false);
                                signUpPasswordValidationLabel.setVisible(false);
                                signUpPassword.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        confirmPassword
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                confirmPasswordValidationLabel.setManaged(false);
                                confirmPasswordValidationLabel.setVisible(false);
                                confirmPassword.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        signUpEmail
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                signUpEmailValidationLabel.setManaged(false);
                                signUpEmailValidationLabel.setVisible(false);
                                signUpEmail.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
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

    private void focusControl() {
        firstName.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (!firstName.getText().isEmpty() && !firstName.getText().isBlank() && lastName.getText().isEmpty() && lastName.getText().isBlank()) {
                    lastName.requestFocus();
                } else if (!firstName.getText().isEmpty() && !firstName.getText().isBlank() && !lastName.getText().isEmpty() && !lastName.getText().isBlank() && otherName.getText().isEmpty() && otherName.getText().isBlank()) {
                    otherName.requestFocus();
                } else if (!firstName.getText().isEmpty() && !firstName.getText().isBlank() && !lastName.getText().isEmpty() && !lastName.getText().isBlank() && !otherName.getText().isEmpty() && !otherName.getText().isBlank() && phoneNumber.getText().isEmpty() && phoneNumber.getText().isBlank()) {
                    phoneNumber.requestFocus();
                } else if (!firstName.getText().isEmpty() && !firstName.getText().isBlank() && !lastName.getText().isEmpty() && !lastName.getText().isBlank() && !otherName.getText().isEmpty() && !otherName.getText().isBlank() && !phoneNumber.getText().isEmpty() && !phoneNumber.getText().isBlank() && signUpEmail.getText().isEmpty() && signUpEmail.getText().isBlank()) {
                    signUpEmail.requestFocus();
                } else if (!lastName.getText().isEmpty() && !lastName.getText().isBlank() && firstName.getText().isEmpty() && firstName.getText().isBlank()) {
                    firstName.requestFocus();
                } else if (!firstName.getText().isEmpty() && !firstName.getText().isBlank() && !lastName.getText().isEmpty() && !lastName.getText().isBlank() && !otherName.getText().isEmpty() && !otherName.getText().isBlank() && !phoneNumber.getText().isEmpty() && !phoneNumber.getText().isBlank() && !signUpEmail.getText().isEmpty() && !signUpEmail.getText().isBlank()) {
                    nextBtn.fire();
                }
            }
        });
        lastName.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (!firstName.getText().isEmpty() && !firstName.getText().isBlank() && lastName.getText().isEmpty() && lastName.getText().isBlank()) {
                    lastName.requestFocus();
                } else if (!firstName.getText().isEmpty() && !firstName.getText().isBlank() && !lastName.getText().isEmpty() && !lastName.getText().isBlank() && otherName.getText().isEmpty() && otherName.getText().isBlank()) {
                    otherName.requestFocus();
                } else if (!firstName.getText().isEmpty() && !firstName.getText().isBlank() && !lastName.getText().isEmpty() && !lastName.getText().isBlank() && !otherName.getText().isEmpty() && !otherName.getText().isBlank() && phoneNumber.getText().isEmpty() && phoneNumber.getText().isBlank()) {
                    phoneNumber.requestFocus();
                } else if (!firstName.getText().isEmpty() && !firstName.getText().isBlank() && !lastName.getText().isEmpty() && !lastName.getText().isBlank() && !otherName.getText().isEmpty() && !otherName.getText().isBlank() && !phoneNumber.getText().isEmpty() && !phoneNumber.getText().isBlank() && signUpEmail.getText().isEmpty() && signUpEmail.getText().isBlank()) {
                    signUpEmail.requestFocus();
                } else if (!lastName.getText().isEmpty() && !lastName.getText().isBlank() && firstName.getText().isEmpty() && firstName.getText().isBlank()) {
                    firstName.requestFocus();
                } else if (!firstName.getText().isEmpty() && !firstName.getText().isBlank() && !lastName.getText().isEmpty() && !lastName.getText().isBlank() && !otherName.getText().isEmpty() && !otherName.getText().isBlank() && !phoneNumber.getText().isEmpty() && !phoneNumber.getText().isBlank() && !signUpEmail.getText().isEmpty() && !signUpEmail.getText().isBlank()) {
                    nextBtn.fire();
                }
            }
        });
        otherName.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (!firstName.getText().isEmpty() && !firstName.getText().isBlank() && lastName.getText().isEmpty() && lastName.getText().isBlank()) {
                    lastName.requestFocus();
                } else if (!firstName.getText().isEmpty() && !firstName.getText().isBlank() && !lastName.getText().isEmpty() && !lastName.getText().isBlank() && otherName.getText().isEmpty() && otherName.getText().isBlank()) {
                    otherName.requestFocus();
                } else if (!firstName.getText().isEmpty() && !firstName.getText().isBlank() && !lastName.getText().isEmpty() && !lastName.getText().isBlank() && !otherName.getText().isEmpty() && !otherName.getText().isBlank() && phoneNumber.getText().isEmpty() && phoneNumber.getText().isBlank()) {
                    phoneNumber.requestFocus();
                } else if (!firstName.getText().isEmpty() && !firstName.getText().isBlank() && !lastName.getText().isEmpty() && !lastName.getText().isBlank() && !otherName.getText().isEmpty() && !otherName.getText().isBlank() && !phoneNumber.getText().isEmpty() && !phoneNumber.getText().isBlank() && signUpEmail.getText().isEmpty() && signUpEmail.getText().isBlank()) {
                    signUpEmail.requestFocus();
                } else if (!lastName.getText().isEmpty() && !lastName.getText().isBlank() && firstName.getText().isEmpty() && firstName.getText().isBlank()) {
                    firstName.requestFocus();
                } else if (!firstName.getText().isEmpty() && !firstName.getText().isBlank() && !lastName.getText().isEmpty() && !lastName.getText().isBlank() && !otherName.getText().isEmpty() && !otherName.getText().isBlank() && !phoneNumber.getText().isEmpty() && !phoneNumber.getText().isBlank() && !signUpEmail.getText().isEmpty() && !signUpEmail.getText().isBlank()) {
                    nextBtn.fire();
                }
            }
        });
        phoneNumber.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (!firstName.getText().isEmpty() && !firstName.getText().isBlank() && lastName.getText().isEmpty() && lastName.getText().isBlank()) {
                    lastName.requestFocus();
                } else if (!firstName.getText().isEmpty() && !firstName.getText().isBlank() && !lastName.getText().isEmpty() && !lastName.getText().isBlank() && otherName.getText().isEmpty() && otherName.getText().isBlank()) {
                    otherName.requestFocus();
                } else if (!firstName.getText().isEmpty() && !firstName.getText().isBlank() && !lastName.getText().isEmpty() && !lastName.getText().isBlank() && !otherName.getText().isEmpty() && !otherName.getText().isBlank() && phoneNumber.getText().isEmpty() && phoneNumber.getText().isBlank()) {
                    phoneNumber.requestFocus();
                } else if (!firstName.getText().isEmpty() && !firstName.getText().isBlank() && !lastName.getText().isEmpty() && !lastName.getText().isBlank() && !otherName.getText().isEmpty() && !otherName.getText().isBlank() && !phoneNumber.getText().isEmpty() && !phoneNumber.getText().isBlank() && signUpEmail.getText().isEmpty() && signUpEmail.getText().isBlank()) {
                    signUpEmail.requestFocus();
                } else if (!lastName.getText().isEmpty() && !lastName.getText().isBlank() && firstName.getText().isEmpty() && firstName.getText().isBlank()) {
                    firstName.requestFocus();
                } else if (!firstName.getText().isEmpty() && !firstName.getText().isBlank() && !lastName.getText().isEmpty() && !lastName.getText().isBlank() && !otherName.getText().isEmpty() && !otherName.getText().isBlank() && !phoneNumber.getText().isEmpty() && !phoneNumber.getText().isBlank() && !signUpEmail.getText().isEmpty() && !signUpEmail.getText().isBlank()) {
                    nextBtn.fire();
                }
            }
        });

        signUpEmail.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (!signUpEmail.getText().isEmpty() && !signUpEmail.getText().isBlank() && password.getText().isEmpty() && password.getText().isBlank()) {
                    List<Constraint> signUpEmailConstraints = signUpEmail.validate();
                    if (!signUpEmailConstraints.isEmpty()) {
                        signUpEmailValidationLabel.setManaged(true);
                        signUpEmailValidationLabel.setVisible(true);
                        signUpEmailValidationLabel.setText(signUpEmailConstraints.getFirst().getMessage());
                        signUpEmail.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (signUpEmailConstraints.isEmpty()) {
                        nextBtn.fire();
                    }
                } else if (!firstName.getText().isEmpty() && !firstName.getText().isBlank() && lastName.getText().isEmpty() && lastName.getText().isBlank()) {
                    lastName.requestFocus();
                } else if (!firstName.getText().isEmpty() && !firstName.getText().isBlank() && !lastName.getText().isEmpty() && !lastName.getText().isBlank() && otherName.getText().isEmpty() && otherName.getText().isBlank()) {
                    otherName.requestFocus();
                } else if (!firstName.getText().isEmpty() && !firstName.getText().isBlank() && !lastName.getText().isEmpty() && !lastName.getText().isBlank() && !otherName.getText().isEmpty() && !otherName.getText().isBlank() && phoneNumber.getText().isEmpty() && phoneNumber.getText().isBlank()) {
                    phoneNumber.requestFocus();
                } else if (!firstName.getText().isEmpty() && !firstName.getText().isBlank() && !lastName.getText().isEmpty() && !lastName.getText().isBlank() && !otherName.getText().isEmpty() && !otherName.getText().isBlank() && !phoneNumber.getText().isEmpty() && !phoneNumber.getText().isBlank() && signUpEmail.getText().isEmpty() && signUpEmail.getText().isBlank()) {
                    signUpEmail.requestFocus();
                } else if (!lastName.getText().isEmpty() && !lastName.getText().isBlank() && firstName.getText().isEmpty() && firstName.getText().isBlank()) {
                    firstName.requestFocus();
                } else if (!firstName.getText().isEmpty() && !firstName.getText().isBlank() && !lastName.getText().isEmpty() && !lastName.getText().isBlank() && !otherName.getText().isEmpty() && !otherName.getText().isBlank() && !phoneNumber.getText().isEmpty() && !phoneNumber.getText().isBlank() && !signUpEmail.getText().isEmpty() && !signUpEmail.getText().isBlank()) {
                    nextBtn.fire();
                }
            }
        });
        signUpPassword.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (!signUpPassword.getText().isEmpty() && !signUpPassword.getText().isBlank() && confirmPassword.getText().isEmpty() && confirmPassword.getText().isBlank()) {
                    confirmPassword.requestFocus();
                } else if (!confirmPassword.getText().isEmpty() && !confirmPassword.getText().isBlank() && signUpPassword.getText().isEmpty() && signUpPassword.getText().isBlank()) {
                    signUpPassword.requestFocus();
                } else if (!signUpPassword.getText().isEmpty() && !signUpPassword.getText().isBlank() && !confirmPassword.getText().isEmpty() && !confirmPassword.getText().isBlank()) {
                    registerBtn.fire();
                }
            }
        });
        confirmPassword.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (!confirmPassword.getText().isEmpty() && !confirmPassword.getText().isBlank() && signUpPassword.getText().isEmpty() && signUpPassword.getText().isBlank()) {
                    signUpPassword.requestFocus();
                } else if (!signUpPassword.getText().isEmpty() && !signUpPassword.getText().isBlank() && confirmPassword.getText().isEmpty() && confirmPassword.getText().isBlank()) {
                    confirmPassword.requestFocus();
                } else if (!confirmPassword.getText().isEmpty() && !confirmPassword.getText().isBlank() && !signUpPassword.getText().isEmpty() && !signUpPassword.getText().isBlank()) {
                    registerBtn.fire();
                }
            }
        });

        email.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (!email.getText().isEmpty() && !email.getText().isBlank() && password.getText().isEmpty() && password.getText().isBlank()) {
                    List<Constraint> emailConstraints = email.validate();
                    if (!emailConstraints.isEmpty()) {
                        emailValidationLabel.setManaged(true);
                        emailValidationLabel.setVisible(true);
                        emailValidationLabel.setText(emailConstraints.getFirst().getMessage());
                        email.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (emailConstraints.isEmpty()) {
                        password.requestFocus();
                    }
                } else if (email.getText().isEmpty() && email.getText().isBlank() && !password.getText().isEmpty() && !password.getText().isBlank()) {
                    email.requestFocus();
                } else if (!email.getText().isEmpty() && !email.getText().isBlank() && !password.getText().isEmpty() && !password.getText().isBlank()) {
                    loginBtn.fire();
                }
            }
        });
        password.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (!password.getText().isEmpty() && !password.getText().isBlank() && email.getText().isEmpty() && email.getText().isBlank()) {
                    email.requestFocus();
                } else if (!email.getText().isEmpty() && !email.getText().isBlank() && password.getText().isEmpty() && password.getText().isBlank()) {
                    password.requestFocus();
                } else if (!password.getText().isEmpty() && !password.getText().isBlank() && !email.getText().isEmpty() && !email.getText().isBlank()) {
                    loginBtn.fire();
                }
            }
        });
    }

    private void signUpLinkAction() {
        closeLoginIcon.setVisible(false);
        closeLoginIcon.setManaged(false);
        loginScreen.setVisible(false);
        loginScreen.setManaged(false);
        closeSignupIcon.setVisible(true);
        closeSignupIcon.setManaged(true);
        registerScreen.setVisible(true);
        registerScreen.setManaged(true);
        LoginViewModel.resetProperties();
    }

    public void backToLogin() {
        closeSignupIcon.setVisible(false);
        closeSignupIcon.setManaged(false);
        registerScreen.setVisible(false);
        registerScreen.setManaged(false);
        closeLoginIcon.setVisible(true);
        closeLoginIcon.setManaged(true);
        loginScreen.setVisible(true);
        loginScreen.setManaged(true);
        signUpBack();
        SignupViewModel.resetProperties();
    }

    public void goNext() {
        List<Constraint> firstNameConstraints = firstName.validate();
        List<Constraint> lastNameConstraints = lastName.validate();
        List<Constraint> phoneNumberConstraints = phoneNumber.validate();
        List<Constraint> signUpEmailConstraints = signUpEmail.validate();
        if (!firstNameConstraints.isEmpty()) {
            firstNameValidationLabel.setManaged(true);
            firstNameValidationLabel.setVisible(true);
            firstNameValidationLabel.setText(firstNameConstraints.getFirst().getMessage());
            firstName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
        if (!lastNameConstraints.isEmpty()) {
            lastNameValidationLabel.setManaged(true);
            lastNameValidationLabel.setVisible(true);
            lastNameValidationLabel.setText(lastNameConstraints.getFirst().getMessage());
            lastName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
        if (!phoneNumberConstraints.isEmpty()) {
            phoneNumberValidationLabel.setManaged(true);
            phoneNumberValidationLabel.setVisible(true);
            phoneNumberValidationLabel.setText(phoneNumberConstraints.getFirst().getMessage());
            phoneNumber.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
        if (!signUpEmailConstraints.isEmpty()) {
            signUpEmailValidationLabel.setManaged(true);
            signUpEmailValidationLabel.setVisible(true);
            signUpEmailValidationLabel.setText(signUpEmailConstraints.getFirst().getMessage());
            signUpEmail.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
        if (firstNameConstraints.isEmpty()
                && lastNameConstraints.isEmpty()
                && phoneNumberConstraints.isEmpty()
                && signUpEmailConstraints.isEmpty()) {
            kycScreen.setVisible(false);
            kycScreen.setManaged(false);
//        registerScreen.setVisible(false);
//        registerScreen.setManaged(false);
            authCreateScreen.setVisible(true);
            authCreateScreen.setManaged(true);
//        loginScreen.setVisible(true);
//        loginScreen.setManaged(true);
        }
    }

    public void signUpBack() {
        authCreateScreen.setVisible(false);
        authCreateScreen.setManaged(false);
//        loginScreen.setVisible(false);
//        loginScreen.setManaged(false);
        kycScreen.setVisible(true);
        kycScreen.setManaged(true);
//        registerScreen.setVisible(true);
//        registerScreen.setManaged(true);
    }

    public void registerUser() {
        List<Constraint> signUpPasswordConstraints = signUpPassword.validate();
        List<Constraint> confirmPasswordConstraints = confirmPassword.validate();
        if (!signUpPasswordConstraints.isEmpty()) {
            signUpPasswordValidationLabel.setManaged(true);
            signUpPasswordValidationLabel.setVisible(true);
            signUpPasswordValidationLabel.setText(signUpPasswordConstraints.getFirst().getMessage());
            signUpPassword.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
        if (!confirmPasswordConstraints.isEmpty()) {
            confirmPasswordValidationLabel.setManaged(true);
            confirmPasswordValidationLabel.setVisible(true);
            confirmPasswordValidationLabel.setText(confirmPasswordConstraints.getFirst().getMessage());
            confirmPassword.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        }
        if (signUpPasswordConstraints.isEmpty()
                && confirmPasswordConstraints.isEmpty()) {
            SignupViewModel.registerUser(this::onActivity, this::onSignupSuccess, this::onLoginError);
        }
    }
}
