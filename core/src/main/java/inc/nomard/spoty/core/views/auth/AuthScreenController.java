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
import inc.nomard.spoty.utils.*;
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
import javafx.concurrent.*;
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
import lombok.extern.java.*;

@Log
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

    public AuthScreenController(Stage primaryStage) {
        stage = primaryStage;
    }

    //    private Thread dataInit() {
//        return SpotyThreader.singleThreadCreator(
//                "data-tracker",
//                () -> {
//                    try {
//                        AdjustmentMasterViewModel.getAllAdjustmentMasters(null, null);
//                        BranchViewModel.getAllBranches(null, null);
//                        BrandViewModel.getAllBrands(null, null);
//                        BankViewModel.getAllBanks(null, null);
//                        CurrencyViewModel.getAllCurrencies(null, null);
//                        CustomerViewModel.getAllCustomers(null, null);
//                        DesignationViewModel.getAllDesignations(null, null);
//                        EmploymentStatusViewModel.getAllEmploymentStatuses(null, null);
//                        ExpenseCategoryViewModel.getAllCategories(null, null);
//                        ExpensesViewModel.getAllExpenses(null, null);
//                        ProductCategoryViewModel.getAllProductCategories(null, null);
//                        ProductViewModel.getAllProducts(null, null);
//                        PurchaseMasterViewModel.getAllPurchaseMasters(null, null);
//                        PurchaseReturnMasterViewModel.getPurchaseReturnMasters(null, null);
//                        QuotationMasterViewModel.getAllQuotationMasters(null, null);
//                        RequisitionMasterViewModel.getAllRequisitionMasters(null, null);
//                        SaleMasterViewModel.getAllSaleMasters(null, null);
//                        SaleReturnMasterViewModel.getSaleReturnMasters(null, null);
//                        StockInMasterViewModel.getAllStockInMasters(null, null);
//                        SupplierViewModel.getAllSuppliers(null, null);
//                        TransferMasterViewModel.getAllTransferMasters(null, null);
//                        UOMViewModel.getAllUOMs(null, null);
//                        UserViewModel.getAllUsers(null, null);
//                        RoleViewModel.getAllRoles(null, null);
//                        PermissionsViewModel.getAllPermissions(null, null);
//                    } catch (Exception e) {
//                        SpotyLogger.writeToFile(e, AuthScreenController.class);
//                    }
//                });
//    }
    private Service<Void> dataInit() {
        return new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() {
                        AdjustmentMasterViewModel.getAllAdjustmentMasters(null, null);
                        BranchViewModel.getAllBranches(null, null);
                        BrandViewModel.getAllBrands(null, null);
                        BankViewModel.getAllBanks(null, null);
                        CurrencyViewModel.getAllCurrencies(null, null);
                        CustomerViewModel.getAllCustomers(null, null);
                        DesignationViewModel.getAllDesignations(null, null);
                        DiscountViewModel.getDiscounts(null, null);
                        EmploymentStatusViewModel.getAllEmploymentStatuses(null, null);
                        ExpenseCategoryViewModel.getAllCategories(null, null);
                        ExpensesViewModel.getAllExpenses(null, null);
                        PermissionsViewModel.getAllPermissions(null, null);
                        ProductCategoryViewModel.getAllProductCategories(null, null);
                        ProductViewModel.getAllProducts(null, null);
                        PurchaseMasterViewModel.getAllPurchaseMasters(null, null);
                        PurchaseReturnMasterViewModel.getPurchaseReturnMasters(null, null);
                        QuotationMasterViewModel.getAllQuotationMasters(null, null);
                        RequisitionMasterViewModel.getAllRequisitionMasters(null, null);
                        RoleViewModel.getAllRoles(null, null);
                        SaleMasterViewModel.getAllSaleMasters(null, null);
                        SaleReturnMasterViewModel.getSaleReturnMasters(null, null);
                        StockInMasterViewModel.getAllStockInMasters(null, null);
                        SupplierViewModel.getAllSuppliers(null, null);
                        TaxViewModel.getTaxes(null, null);
                        TransferMasterViewModel.getAllTransferMasters(null, null);
                        UOMViewModel.getAllUOMs(null, null);
                        UserViewModel.getAllUsers(null, null);
                        return null;
                    }
                };
            }
        };
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
//            Platform.runLater(() ->
            LoginViewModel.login(this::onLoginSuccess, this::successMessage, this::errorMessage)/*)*/;
        }
    }

    private void onActivity() {
//        loginBtn.setDisable(true);
//        loginBtn.setManaged(false);
//        activityIndicator.setVisible(true);
//        activityIndicator.setManaged(true);
    }

    private void successMessage(String message) {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder(message)
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        AnchorPane.setRightAnchor(notification, 40.0);
        AnchorPane.setTopAnchor(notification, 10.0);

        var in = Animations.slideInDown(notification, Duration.millis(250));
        if (!actualContentPane.getChildren().contains(notification)) {
            actualContentPane.getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent -> delay(notification));
        }
    }

    private void errorMessage(String message) {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder(message)
                        .duration(MessageDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .build();
        notificationHolder.addMessage(notification);
        AnchorPane.setRightAnchor(notification, 40.0);
        AnchorPane.setTopAnchor(notification, 10.0);

        var in = Animations.slideInDown(notification, Duration.millis(250));
        if (!actualContentPane.getChildren().contains(notification)) {
            actualContentPane.getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent -> delay(notification));
        }
    }

    private void onLoginSuccess() {
        var dataInit = dataInit();
//        try {
//            dataInit.join();
//        } catch (InterruptedException e) {
//            SpotyLogger.writeToFile(e, AuthScreenController.class);
//        }
        dataInit.setOnSucceeded(workerStateEvent -> {
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
                    Scene scene = stage.getScene();
                    scene.setRoot(root);
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
            // var backgroundWorker = OnlineQueryWorker.fetchDataTask();
            // backgroundWorker.setPeriod(Duration.seconds(10));
            // backgroundWorker.setDelay(Duration.seconds(30));
            // backgroundWorker.start();
        });
        dataInit.start();
    }

    private void onSignupSuccess() {
        SignupViewModel.resetProperties();
        backToLogin();
    }

    public void closeIconClicked() {
        stage.hide();
        stage.close();
        SpotyThreader.disposeSpotyThreadPool();
        Platform.exit();
        System.exit(0);
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
            SignupViewModel.registerUser(this::onSignupSuccess, this::successMessage, this::errorMessage);
        }
    }
}
