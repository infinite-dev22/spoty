package inc.nomard.spoty.core.views.pages;

import atlantafx.base.util.*;
import inc.nomard.spoty.core.*;
import inc.nomard.spoty.core.values.*;
import inc.nomard.spoty.core.values.strings.*;
import static inc.nomard.spoty.core.values.strings.Values.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.adjustments.*;
import inc.nomard.spoty.core.viewModels.dashboard.*;
import inc.nomard.spoty.core.viewModels.hrm.employee.*;
import inc.nomard.spoty.core.viewModels.purchases.*;
import inc.nomard.spoty.core.viewModels.quotations.*;
import inc.nomard.spoty.core.viewModels.requisitions.*;
import inc.nomard.spoty.core.viewModels.returns.purchases.*;
import inc.nomard.spoty.core.viewModels.returns.sales.*;
import inc.nomard.spoty.core.viewModels.sales.*;
import inc.nomard.spoty.core.viewModels.stock_ins.*;
import inc.nomard.spoty.core.viewModels.transfers.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.enums.*;
import static io.github.palexdev.materialfx.utils.StringUtils.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxresources.fonts.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import javafx.animation.*;
import javafx.application.*;
import javafx.beans.binding.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class AuthScreen extends BorderPane {
    private final Stage stage;
    public MFXTextField email,
            signUpEmail,
            phoneNumber,
            otherName,
            lastName,
            firstName;
    public MFXPasswordField password,
            confirmPassword,
            signUpPassword;
    public Label forgotPassword,
            emailValidationLabel,
            passwordValidationLabel,
            loginAppNameLbl,
            loginPoweredByLbl,
            signUpEmailValidationLabel,
            phoneNumberValidationLabel,
            lastNameValidationLabel,
            firstNameValidationLabel,
            loginLink,
            confirmPasswordValidationLabel,
            signUpPasswordValidationLabel,
            signUpLink;
    public MFXButton loginBtn,
            nextBtn,
            backBtn,
            registerBtn,
            signUpBack;
    public MFXFontIcon closeLoginIcon,
            closeSignupIcon;
    public Pane contentPane;
    public AnchorPane actualContentPane;
    public VBox loginScreen,
            kycScreen,
            authCreateScreen;
    public HBox registerScreen;

    public AuthScreen(Stage primaryStage) {
        stage = primaryStage;
        init();
    }

    // Validation label.
    private Label buildValidationLabel() {
        var label = new Label();
        label.setManaged(false);
        label.setVisible(false);
        label.setWrapText(true);
        label.getStyleClass().add("input-validation-error");
        label.setId("validationLabel");
        return label;
    }

    // App branding.
    private VBox buildAppBranding() {
        loginAppNameLbl = new Label();
        loginAppNameLbl.setAlignment(Pos.CENTER);
        loginAppNameLbl.setContentDisplay(ContentDisplay.CENTER);
        loginAppNameLbl.getStyleClass().add("app-label");
        loginAppNameLbl.setText(Labels.APP_NAME);
        VBox.setVgrow(loginAppNameLbl, Priority.ALWAYS);
        loginPoweredByLbl = new Label();
        loginPoweredByLbl.setAlignment(Pos.CENTER);
        loginPoweredByLbl.setContentDisplay(ContentDisplay.CENTER);
        loginPoweredByLbl.getStyleClass().add("company-label");
        loginPoweredByLbl.setText("Powered by " + Labels.COMPANY_NAME);
        VBox.setVgrow(loginPoweredByLbl, Priority.ALWAYS);

        var vbox = new VBox();
        vbox.setAlignment(Pos.BOTTOM_CENTER);
        vbox.setSpacing(5d);
        vbox.getChildren().addAll(loginAppNameLbl, loginPoweredByLbl);
        return vbox;
    }

    // Title.
    private Label buildLoginTitle() {
        var label = new Label("Login");
        label.getStyleClass().add("login-title");
        return label;
    }

    // Email Input.
    private VBox buildLoginEmail() {
        // Input.
        email = new MFXTextField();
        email.setFloatMode(FloatMode.BORDER);
        email.setFloatingText("Email");
        email.setPrefWidth(350d);
        email.textProperty().bindBidirectional(LoginViewModel.emailProperty());
        // Validation.
        emailValidationLabel = buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 0d, 0d));
        vbox.getChildren().addAll(email, emailValidationLabel);
        return vbox;
    }

    // Password input.
    private VBox buildPassword() {
        // Input.
        password = new MFXPasswordField();
        password.setFloatMode(FloatMode.BORDER);
        password.setFloatingText("Password");
        password.setPrefWidth(350d);
        password.textProperty().bindBidirectional(LoginViewModel.passwordProperty());
        // Validation.
        passwordValidationLabel = buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 0d, 0d));
        vbox.getChildren().addAll(password, passwordValidationLabel);
        return vbox;
    }

    // Forgot password.
    private HBox buildForgotPassword() {
        forgotPassword = new Label("Forgot Password?");
        forgotPassword.getStyleClass().add("link");
        forgotPassword.setUnderline(true);
        forgotPassword.setCursor(Cursor.HAND);
        var hbox = new HBox(forgotPassword);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        return hbox;
    }

    // Login button.
    private HBox buildLoginButton() {
        loginBtn = new MFXButton("Login");
        loginBtn.setPrefWidth(352d);
        loginBtn.getStyleClass().add("filled");
        loginBtn.setOnAction(actionEvent -> onLoginPressed());
        var hbox = new HBox(loginBtn);
        hbox.setAlignment(Pos.CENTER);
        return hbox;
    }

    // Don't have an account link.
    private HBox buildRegistrationLink() {
        var label = new Label("Don't have an account? ");
        label.setAlignment(Pos.CENTER);
        label.setContentDisplay(ContentDisplay.CENTER);
        signUpLink = new Label("Create one");
        signUpLink.getStyleClass().add("link");
        signUpLink.setUnderline(true);
        signUpLink.setCursor(Cursor.HAND);
        signUpLink.setOnMouseClicked(event -> signUpLinkAction());
        var hbox = new HBox();
        hbox.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(hbox, Priority.ALWAYS);
        hbox.getChildren().addAll(label, signUpLink);
        return hbox;
    }

    // Login screen UI.
    private VBox buildLoginScreenUI() {
        var region = new Region();
        VBox.setVgrow(region, Priority.ALWAYS);

        loginScreen = new VBox();
        loginScreen.setAlignment(Pos.CENTER);
        loginScreen.setPrefHeight(500d);
        loginScreen.setPrefWidth(540d);
        loginScreen.setSpacing(20d);
        loginScreen.getStyleClass().addAll("card-raised");
        loginScreen.setStyle("-fx-background-color: white;");
        UIUtils.anchor(loginScreen, 0d, 0d, 0d, 599d);
        loginScreen.setPadding(new Insets(16d));
        loginScreen.getChildren().addAll(region,
                buildLoginTitle(),
                buildLoginEmail(),
                buildPassword(),
                buildForgotPassword(),
                buildLoginButton(),
                buildRegistrationLink(),
                buildAppBranding());
        return loginScreen;
    }

    // Title.
    private Label buildKYCTitle() {
        var label = new Label("Register (User Details)");
        label.getStyleClass().add("login-title");
        return label;
    }

    // First name input.
    private VBox buildFirstName() {
        // Input.
        firstName = new MFXTextField();
        firstName.setFloatMode(FloatMode.BORDER);
        firstName.setFloatingText("First name");
        firstName.setPrefWidth(350d);
        firstName.textProperty().bindBidirectional(SignupViewModel.firstNameProperty());
        // Validation.
        firstNameValidationLabel = buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 0d, 0d));
        vbox.getChildren().addAll(firstName, firstNameValidationLabel);
        return vbox;
    }

    // Last name input.
    private VBox buildLastName() {
        // Input.
        lastName = new MFXTextField();
        lastName.setFloatMode(FloatMode.BORDER);
        lastName.setFloatingText("Last name");
        lastName.setPrefWidth(350d);
        lastName.textProperty().bindBidirectional(SignupViewModel.lastNameProperty());
        // Validation.
        lastNameValidationLabel = buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 0d, 0d));
        vbox.getChildren().addAll(lastName, lastNameValidationLabel);
        return vbox;
    }

    private HBox buildNames() {
        var hbox = new HBox();
        hbox.setSpacing(10d);
        hbox.getChildren().addAll(buildFirstName(), buildLastName());
        return hbox;
    }

    // Other name input.
    private VBox buildOtherName() {
        // Input.
        otherName = new MFXTextField();
        otherName.setFloatMode(FloatMode.BORDER);
        otherName.setFloatingText("Other name (Optional)");
        otherName.setPrefWidth(350d);
        otherName.textProperty().bindBidirectional(SignupViewModel.otherNameProperty());
        var vbox = new VBox(otherName);
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 0d, 0d));
        return vbox;
    }

    // Phone number input.
    private VBox buildPhoneName() {
        // Input.
        phoneNumber = new MFXTextField();
        phoneNumber.setFloatMode(FloatMode.BORDER);
        phoneNumber.setFloatingText("Phone number");
        phoneNumber.setPrefWidth(350d);
        phoneNumber.textProperty().bindBidirectional(SignupViewModel.phoneProperty());
        // Validation.
        phoneNumberValidationLabel = buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 0d, 0d));
        vbox.getChildren().addAll(phoneNumber, phoneNumberValidationLabel);
        return vbox;
    }

    // Email input.
    private VBox buildKYCEmail() {
        // Input.
        signUpEmail = new MFXTextField();
        signUpEmail.setFloatMode(FloatMode.BORDER);
        signUpEmail.setFloatingText("Email");
        signUpEmail.setPrefWidth(350d);
        signUpEmail.textProperty().bindBidirectional(SignupViewModel.emailProperty());
        // Validation.
        signUpEmailValidationLabel = buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 0d, 0d));
        vbox.getChildren().addAll(signUpEmail, signUpEmailValidationLabel);
        return vbox;
    }

    // Back button.
    private HBox buildKYCBackButton() {
        backBtn = new MFXButton("Back to Login");
        backBtn.setPrefWidth(150d);
        backBtn.getStyleClass().add("outlined");
        backBtn.setOnAction(event -> backToLogin());
        var hbox = new HBox(backBtn);
        HBox.setHgrow(hbox, Priority.ALWAYS);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    // Next button.
    private HBox buildKYCNextButton() {
        nextBtn = new MFXButton("Proceed");
        nextBtn.setPrefWidth(120d);
        nextBtn.getStyleClass().add("filled");
        nextBtn.setOnAction(event -> goNext());
        var hbox = new HBox(nextBtn);
        HBox.setHgrow(hbox, Priority.ALWAYS);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        return hbox;
    }

    private HBox buildKYCButtons() {
        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(buildKYCBackButton(), buildKYCNextButton());
        return hbox;
    }

    // KYC Screen UI.
    private VBox buildKYCScreenUI() {
        var region = new Region();
        VBox.setVgrow(region, Priority.ALWAYS);

        kycScreen = new VBox();
        kycScreen.setAlignment(Pos.CENTER);
        kycScreen.setPrefWidth(390d);
        kycScreen.setSpacing(20d);
        kycScreen.getStyleClass().addAll("card-raised");
        kycScreen.setStyle("-fx-background-color: white;");
        kycScreen.setPadding(new Insets(16d));
        kycScreen.getChildren().addAll(region,
                buildKYCTitle(),
                buildNames(),
                buildOtherName(),
                buildPhoneName(),
                buildKYCEmail(),
                buildKYCButtons(),
                buildAppBranding());
        return kycScreen;
    }

    // Title.
    private Label buildRegisterTitle() {
        var label = new Label("Register (Authentication)");
        label.getStyleClass().add("login-title");
        return label;
    }

    // Password input.
    private VBox buildRegisterPassword() {
        // Input.
        signUpPassword = new MFXPasswordField();
        signUpPassword.setFloatMode(FloatMode.BORDER);
        signUpPassword.setFloatingText("Password");
        signUpPassword.setPrefWidth(350d);
        signUpPassword.textProperty().bindBidirectional(SignupViewModel.passwordProperty());
        // Validation.
        signUpPasswordValidationLabel = buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 0d, 0d));
        vbox.getChildren().addAll(signUpPassword, signUpPasswordValidationLabel);
        return vbox;
    }

    // Confirm password input.
    private VBox buildRegisterConfirmPassword() {
        // Input.
        confirmPassword = new MFXPasswordField();
        confirmPassword.setFloatMode(FloatMode.BORDER);
        confirmPassword.setFloatingText("Confirm Password");
        confirmPassword.setPrefWidth(350d);
        confirmPassword.textProperty().bindBidirectional(SignupViewModel.confirmPasswordProperty());
        // Validation.
        confirmPasswordValidationLabel = buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 0d, 0d));
        vbox.getChildren().addAll(confirmPassword, confirmPasswordValidationLabel);
        return vbox;
    }

    // Back button.
    private HBox buildRegisterBackButton() {
        signUpBack = new MFXButton("Back");
        signUpBack.setPrefWidth(120d);
        signUpBack.getStyleClass().add("outlined");
        signUpBack.setOnAction(event -> signUpBack());
        var hbox = new HBox(signUpBack);
        HBox.setHgrow(hbox, Priority.ALWAYS);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    // Register button.
    private HBox buildRegisterButton() {
        registerBtn = new MFXButton("Register");
        registerBtn.setPrefWidth(150d);
        registerBtn.getStyleClass().add("filled");
        registerBtn.setOnAction(event -> registerUser());
        var hbox = new HBox(registerBtn);
        HBox.setHgrow(hbox, Priority.ALWAYS);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        return hbox;
    }

    private HBox buildRegisterButtons() {
        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(buildRegisterBackButton(), buildRegisterButton());
        return hbox;
    }

    // Already have an account link.
    private HBox buildLoginLink() {
        var label = new Label("Already have an account? ");
        label.setAlignment(Pos.CENTER);
        label.setContentDisplay(ContentDisplay.CENTER);
        loginLink = new Label("Login");
        loginLink.getStyleClass().add("link");
        loginLink.setUnderline(true);
        loginLink.setCursor(Cursor.HAND);
        loginLink.setOnMouseClicked(event -> backToLogin());
        var hbox = new HBox();
        hbox.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(hbox, Priority.ALWAYS);
        hbox.getChildren().addAll(label, loginLink);
        return hbox;
    }

    // Auth Create screen UI.
    private VBox buildAuthCreateScreenUI() {
        var region = new Region();
        VBox.setVgrow(region, Priority.ALWAYS);

        authCreateScreen = new VBox();
        authCreateScreen.setManaged(false);
        authCreateScreen.setVisible(false);
        authCreateScreen.setAlignment(Pos.CENTER);
        authCreateScreen.setPrefWidth(390d);
        authCreateScreen.setSpacing(20d);
        authCreateScreen.getStyleClass().addAll("card-raised");
        authCreateScreen.setStyle("-fx-background-color: white;");
        authCreateScreen.setPadding(new Insets(16d));
        authCreateScreen.getChildren().addAll(region,
                buildRegisterTitle(),
                buildRegisterPassword(),
                buildRegisterConfirmPassword(),
                buildRegisterButtons(),
                buildLoginLink(),
                buildAppBranding());
        return authCreateScreen;
    }

    // Register screen UI.
    private HBox buildRegisterScreenUI() {
        registerScreen = new HBox();
        registerScreen.setManaged(false);
        registerScreen.setVisible(false);
        UIUtils.anchor(registerScreen, 0d, 599d, 0d, 0d);
        registerScreen.getChildren().addAll(buildKYCScreenUI(),
                buildAuthCreateScreenUI());
        return registerScreen;
    }

    // Close button.
    private MFXFontIcon buildLoginCloseButton() {
        closeLoginIcon = new MFXFontIcon("fas-circle");
        closeLoginIcon.setSize(15d);
        closeLoginIcon.getStyleClass().add("close-icon");
        closeLoginIcon.setOnMouseClicked(event -> closeIconClicked());
        UIUtils.anchor(closeLoginIcon, 10d, 10d, null, null);
        return closeLoginIcon;
    }

    private MFXFontIcon buildSignupCloseButton() {
        closeSignupIcon = new MFXFontIcon("fas-circle");
        closeSignupIcon.setSize(15d);
        closeSignupIcon.getStyleClass().add("close-icon");
        closeSignupIcon.setVisible(false);
        closeSignupIcon.setManaged(false);
        closeSignupIcon.setOnMouseClicked(event -> closeIconClicked());
        UIUtils.anchor(closeSignupIcon, 10d, null, null, 10d);
        return closeSignupIcon;
    }

    // Screen background design.
    private Rectangle createRectangle(double layoutX, double layoutY, double rotate, double width, double height, double arcWidth, double arcHeight, Stop[] stops) {
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setLayoutX(layoutX);
        rectangle.setLayoutY(layoutY);
        rectangle.setRotate(rotate);
        rectangle.setArcWidth(arcWidth);
        rectangle.setArcHeight(arcHeight);
        rectangle.setStroke(Color.TRANSPARENT);
        rectangle.setStrokeLineCap(StrokeLineCap.ROUND);
        rectangle.setStrokeLineJoin(StrokeLineJoin.ROUND);
        rectangle.setStrokeType(StrokeType.INSIDE);
        rectangle.setStrokeWidth(0.0);
        rectangle.getStyleClass().add("card");
        rectangle.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops));
        return rectangle;
    }

    private List<Rectangle> buildBackground() {
        Rectangle rectangle1 = createRectangle(35, -186, -59, 306, 400, 50, 50, new Stop[]{
                new Stop(0, Color.color(0.125490203499794, 0, 0.2235294133424759)),
                new Stop(1, Color.color(0.33725491166114807, 0, 0.6000000238418579))
        });

        Rectangle rectangle2 = createRectangle(234, 249, 121, 370, 400, 50, 50, new Stop[]{
                new Stop(0, Color.color(0.33725491166114807, 0, 0.6000000238418579)),
                new Stop(1, Color.color(1, 0, 0.9019607901573181))
        });
        rectangle2.setY(30);

        Rectangle rectangle3 = createRectangle(-161, 118, 59, 339, 400, 50, 50, new Stop[]{
                new Stop(0, Color.color(0.125490203499794, 0, 0.2235294133424759)),
                new Stop(1, Color.color(1, 0, 0.9019607901573181))
        });
        rectangle3.setRotationAxis(new Point3D(0, 0, -37));

        Rectangle rectangle4 = createRectangle(454, -42, -59, 306, 400, 50, 50, new Stop[]{
                new Stop(0, Color.color(0.125490203499794, 0, 0.2235294133424759)),
                new Stop(1, Color.color(1, 0, 0.9019607901573181))
        });

        Rectangle rectangle5 = createRectangle(625, -343, -60, 306, 400, 50, 50, new Stop[]{
                new Stop(0, Color.color(0.125490203499794, 0, 0.2235294133424759)),
                new Stop(1, Color.color(0.33725491166114807, 0, 0.6000000238418579))
        });
        rectangle5.setY(17);

        Rectangle rectangle6 = createRectangle(938, -27, -60, 339, 400, 50, 50, new Stop[]{
                new Stop(0, Color.color(0.125490203499794, 0, 0.2235294133424759)),
                new Stop(1, Color.color(1, 0, 0.9019607901573181))
        });

        Rectangle rectangle7 = createRectangle(770, 254, -60, 306, 400, 50, 50, new Stop[]{
                new Stop(0, Color.color(0.33725491166114807, 0, 0.6000000238418579)),
                new Stop(1, Color.color(1, 0, 0.9019607901573181))
        });
        rectangle7.setY(30);

        return Stream.of(rectangle1, rectangle2, rectangle3, rectangle4, rectangle5, rectangle6, rectangle7).toList();
    }

    // Actual content pane.
    private AnchorPane buildActualContentPane() {
        actualContentPane = new AnchorPane();
        actualContentPane.setCache(true);
        actualContentPane.setCacheHint(CacheHint.SPEED);
        actualContentPane.setMaxSize(983, 500);
        actualContentPane.setMinSize(983, 500);
        actualContentPane.setPrefSize(983, 500);
        actualContentPane.getChildren().addAll(buildBackground());
        actualContentPane.getChildren().addAll(
                buildLoginScreenUI(),
                buildLoginCloseButton(),
                buildRegisterScreenUI(),
                buildSignupCloseButton());
        return actualContentPane;
    }

    // Content pane.
    private Pane buildContentPane() {
        contentPane = new Pane(buildActualContentPane());
        contentPane.setMinHeight(500d);
        contentPane.setPrefHeight(500d);
        contentPane.setMaxHeight(500d);
        contentPane.setMinWidth(983d);
        contentPane.setPrefWidth(983d);
        contentPane.setMaxWidth(983d);
        contentPane.getStyleClass().add("card-raised");
        BorderPane.setAlignment(contentPane, Pos.TOP_LEFT);
        Rectangle clipRect = new Rectangle(contentPane.getPrefWidth(), contentPane.getPrefHeight());
        clipRect.setArcWidth(20);
        clipRect.setArcHeight(20);
        contentPane.setClip(clipRect);
        return contentPane;
    }

    // Border pane.
    private void init() {
        this.setMinHeight(500d);
        this.setPrefHeight(500d);
        this.setMaxHeight(500d);
        this.setMinWidth(983d);
        this.setPrefWidth(983d);
        this.setMaxWidth(983d);
        this.getStyleClass().add("card-raised");
        this.getChildren().add(buildContentPane());
        this.getStylesheets().addAll(
                SpotyCoreResourceLoader.load("styles/base.css"),
                SpotyCoreResourceLoader.load("styles/Splash.css"),
                SpotyCoreResourceLoader.load("styles/Common.css"),
                SpotyCoreResourceLoader.load("styles/theming/Default.css"),
                SpotyCoreResourceLoader.load("styles/toolitip.css"),
                SpotyCoreResourceLoader.load("styles/TextFields.css")
        );
        // Input listeners.
        requiredValidator();
        focusControl();
    }

    private void initData() {
        CompletableFuture<Void> allDataInitialization = CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> AdjustmentMasterViewModel.getAllAdjustmentMasters(null, null)),
                CompletableFuture.runAsync(() -> BranchViewModel.getAllBranches(null, null)),
                CompletableFuture.runAsync(() -> BrandViewModel.getAllBrands(null, null)),
                CompletableFuture.runAsync(() -> AccountViewModel.getAllAccounts(null, null)),
                CompletableFuture.runAsync(() -> AccountTransactionViewModel.getAllTransactions(null, null)),
                CompletableFuture.runAsync(() -> CurrencyViewModel.getAllCurrencies(null, null)),
                CompletableFuture.runAsync(() -> CustomerViewModel.getAllCustomers(null, null)),
                CompletableFuture.runAsync(() -> DesignationViewModel.getAllDesignations(null, null)),
                CompletableFuture.runAsync(() -> DiscountViewModel.getDiscounts(null, null)),
                CompletableFuture.runAsync(() -> EmploymentStatusViewModel.getAllEmploymentStatuses(null, null)),
                CompletableFuture.runAsync(() -> ExpenseCategoryViewModel.getAllCategories(null, null)),
                CompletableFuture.runAsync(() -> ExpensesViewModel.getAllExpenses(null, null)),
                CompletableFuture.runAsync(() -> PermissionsViewModel.getAllPermissions(null, null)),
                CompletableFuture.runAsync(() -> ProductCategoryViewModel.getAllProductCategories(null, null)),
                CompletableFuture.runAsync(() -> ProductViewModel.getAllProducts(null, null)),
                CompletableFuture.runAsync(() -> PurchaseMasterViewModel.getAllPurchaseMasters(null, null)),
                CompletableFuture.runAsync(() -> PurchaseReturnMasterViewModel.getPurchaseReturnMasters(null, null)),
                CompletableFuture.runAsync(() -> QuotationMasterViewModel.getAllQuotationMasters(null, null)),
                CompletableFuture.runAsync(() -> RequisitionMasterViewModel.getAllRequisitionMasters(null, null)),
                CompletableFuture.runAsync(() -> RoleViewModel.getAllRoles(null, null)),
                CompletableFuture.runAsync(() -> SaleMasterViewModel.getAllSaleMasters(null, null)),
                CompletableFuture.runAsync(() -> SaleReturnMasterViewModel.getSaleReturnMasters(null, null)),
                CompletableFuture.runAsync(() -> StockInMasterViewModel.getAllStockInMasters(null, null)),
                CompletableFuture.runAsync(() -> SupplierViewModel.getAllSuppliers(null, null)),
                CompletableFuture.runAsync(() -> TaxViewModel.getTaxes(null, null)),
                CompletableFuture.runAsync(() -> TransferMasterViewModel.getAllTransferMasters(null, null)),
                CompletableFuture.runAsync(() -> UOMViewModel.getAllUOMs(null, null)),
                CompletableFuture.runAsync(() -> UserViewModel.getAllUsers(null, null)),
                CompletableFuture.runAsync(() -> DashboardViewModel.getTotalEarnings(null, null)),
                CompletableFuture.runAsync(() -> DashboardViewModel.getTotalPurchases(null, null)),
                CompletableFuture.runAsync(() -> DashboardViewModel.getCountProducts(null, null)),
                CompletableFuture.runAsync(() -> DashboardViewModel.getCountCustomers(null, null)),
                CompletableFuture.runAsync(() -> DashboardViewModel.getCountSuppliers(null, null)),
                // CompletableFuture.runAsync(() -> DashboardViewModel.getYearlyExpenses(null, null)),
                CompletableFuture.runAsync(() -> DashboardViewModel.getMonthlyExpenses(null, null)),
                // CompletableFuture.runAsync(() -> DashboardViewModel.getWeeklyExpenses(null, null)),
                // CompletableFuture.runAsync(() -> DashboardViewModel.getYearlyIncomes(null, null)),
                CompletableFuture.runAsync(() -> DashboardViewModel.getMonthlyIncomes(null, null)),
                // CompletableFuture.runAsync(() -> DashboardViewModel.getWeeklyIncomes(null, null)),
                CompletableFuture.runAsync(() -> DashboardViewModel.getWeeklyRevenue(null, null)),
                CompletableFuture.runAsync(() -> DashboardViewModel.getMonthlyRevenue(null, null)),
                CompletableFuture.runAsync(() -> DashboardViewModel.getTopProducts(null, null)),
                CompletableFuture.runAsync(() -> DashboardViewModel.getRecentOrders(null, null)),
                CompletableFuture.runAsync(() -> DashboardViewModel.getStockAlerts(null, null))
        );

        allDataInitialization.thenRun(this::onDataInitializationSuccess)
                .exceptionally(this::onDataInitializationFailure);
    }

    private Void onDataInitializationFailure(Throwable throwable) {
        SpotyLogger.writeToFile(throwable, AuthScreen.class);
        return null;
    }

    private void onDataInitializationSuccess() {
        Platform.runLater(() -> {
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(3500), event -> loadMainView()));
            timeline.play();
        });
    }

    private void loadMainView() {
        stage.hide();
        Parent root = new WindowRunner();
        Scene scene = new Scene(root);
        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(scene);
        scene.setFill(null);
        stage.setScene(scene);
        stage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
        stage.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
        stage.setTitle(Labels.APP_NAME);
        stage.getIcons().addAll(
                PreloadedData.icon16,
                PreloadedData.icon32,
                PreloadedData.icon64,
                PreloadedData.icon128,
                PreloadedData.icon256,
                PreloadedData.icon512
        );
        stage.show();
        stage.centerOnScreen();

        var service = OnlineQueryWorker.fetchDataTask();
        service.start();
        service.setPeriod(Duration.seconds(10));
        service.setDelay(Duration.seconds(0));
    }

    public void onLoginSuccess() {
        initData();
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
            email.setDisable(true);
            password.setDisable(true);
            loginBtn.setDisable(true);
            LoginViewModel.login(this::onLoginSuccess, this::successMessage, this::errorMessage);
        }
    }

    private void successMessage(String message) {
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder(message)
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .height(60)
                        .build();
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
        email.setDisable(false);
        password.setDisable(false);
        loginBtn.setDisable(false);
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder(message)
                        .duration(MessageDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .height(60)
                        .build();
        AnchorPane.setRightAnchor(notification, 40.0);
        AnchorPane.setTopAnchor(notification, 10.0);

        var in = Animations.slideInDown(notification, Duration.millis(250));
        if (!actualContentPane.getChildren().contains(notification)) {
            actualContentPane.getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent -> delay(notification));
        }
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
            authCreateScreen.setVisible(true);
            authCreateScreen.setManaged(true);
        }
    }

    public void signUpBack() {
        authCreateScreen.setVisible(false);
        authCreateScreen.setManaged(false);
        kycScreen.setVisible(true);
        kycScreen.setManaged(true);
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
