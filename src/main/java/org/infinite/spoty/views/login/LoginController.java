package org.infinite.spoty.views.login;

import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.infinite.spoty.SpotyResourceLoader;
import org.infinite.spoty.components.notification.SimpleNotificationHolder;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.utils.SpotyThreader;
import org.infinite.spoty.values.strings.Labels;
import org.infinite.spoty.viewModels.BranchViewModel;
import org.infinite.spoty.viewModels.LoginViewModel;
import org.infinite.spoty.views.BaseController;
import org.infinite.spoty.views.splash.SplashScreenController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotyResourceLoader.fxmlLoader;
import static org.infinite.spoty.Validators.requiredValidator;

public class LoginController implements Initializable {
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
    private Stage stage;

    public LoginController(Stage primaryStage) {
        stage = primaryStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image(SpotyResourceLoader.load("images/login-img-1.png"));

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
    }

    public void onLoginPressed(ActionEvent actionEvent) {

        System.out.println("Login pressed");
        try {
            LoginViewModel.login();

//        UserAgentBuilder.builder()
//                .themes(JavaFXThemes.MODENA)
//                .themes(MaterialFXStylesheets.forAssemble(true))
//                .setDeploy(true)
//                .setResolveAssets(true)
//                .build()
//                .setGlobal();

            Platform.runLater(
                    () -> {
                        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                        try {
                            // Set base view.
                            FXMLLoader loader = fxmlLoader("fxml/Base.fxml");
                            loader.setControllerFactory(c -> BaseController.getInstance(stage));
                            // Base view parent.
                            Parent root = loader.load();
                            Scene scene = new Scene(root);
                            // Fixes black edges showing in main app scene.
                            scene.setFill(null);
                            stage.setScene(scene);
                            stage.initStyle(StageStyle.TRANSPARENT);
                            // Set initial window size.
                            stage.setHeight(primScreenBounds.getHeight());
                            stage.setWidth(primScreenBounds.getWidth());
                            // Set window title name, this name will only be seen when cursor hovers over app icon in
                            // taskbar. Not necessary too but added since other apps also do this.
                            stage.setTitle(Labels.APP_NAME);
                            stage.getIcons().add(new Image(SpotyResourceLoader.load("icon.png")));
                            stage.show();
                            // Set window position to center of screen.
                            // This isn't necessary, just felt like adding it here.
                            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
                            // Initialize app notification handler.
                            SimpleNotificationHolder.setNotificationOwner(stage);
                        } catch (IOException e) {
                            SpotyLogger.writeToFile(e, SplashScreenController.class);
                        }
                    });

        } catch (IOException | InterruptedException e) {
            SpotyLogger.writeToFile(e, BranchViewModel.class);
        }
    }

    public void closeIconClicked(MouseEvent mouseEvent) {
        stage.hide();
        stage.close();
        SpotyThreader.disposeSpotyThreadPool();
        Platform.exit();
        System.exit(0);
    }
}
