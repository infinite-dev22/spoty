package org.infinite.spoty.controller.people.users;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.ButtonType;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.infinite.spoty.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;
import static org.infinite.spoty.data.SampleData.userSampleData;

public class UsersController implements Initializable {
    @FXML
    public MFXTextField userSearchBar;
    @FXML
    public HBox userActionsPane;
    @FXML
    public MFXButton userImportBtn;
    @FXML
    public BorderPane userContentPane;
    @FXML
    private MFXTableView<User> usersTable;
    private Dialog<ButtonType> dialog;

    public UsersController(Stage stage) {
        Platform.runLater(() -> {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            try {
                userFormDialogPane(stage);
            } catch (IOException ex) {
                logger.error(ex.getLocalizedMessage());
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<User> firstName = new MFXTableColumn<>("First Name", true, Comparator.comparing(User::getFirstName));
        MFXTableColumn<User> lastName = new MFXTableColumn<>("Last Name", true, Comparator.comparing(User::getLastName));
        MFXTableColumn<User> userName = new MFXTableColumn<>("User Name", true, Comparator.comparing(User::getUserName));
        MFXTableColumn<User> userEmail = new MFXTableColumn<>("Email", true, Comparator.comparing(User::getUserEmail));
        MFXTableColumn<User> userPhone = new MFXTableColumn<>("Phone", true, Comparator.comparing(User::getUserPhoneNumber));
        MFXTableColumn<User> status = new MFXTableColumn<>("Status", true, Comparator.comparing(User::getUserStatus));

        firstName.setRowCellFactory(user -> new MFXTableRowCell<>(User::getFirstName));
        lastName.setRowCellFactory(user -> new MFXTableRowCell<>(User::getLastName));
        userName.setRowCellFactory(user -> new MFXTableRowCell<>(User::getUserName));
        userEmail.setRowCellFactory(user -> new MFXTableRowCell<>(User::getUserEmail));
        userPhone.setRowCellFactory(user -> new MFXTableRowCell<>(User::getUserPhoneNumber));
        status.setRowCellFactory(user -> new MFXTableRowCell<>(User::getUserStatus));

        usersTable.getTableColumns().addAll(firstName, lastName, userName, userEmail, userPhone, status);
        usersTable.getFilters().addAll(
                new StringFilter<>("First Name", User::getFirstName),
                new StringFilter<>("Last Name", User::getLastName),
                new StringFilter<>("User Name", User::getUserName),
                new StringFilter<>("Email", User::getUserEmail),
                new StringFilter<>("Phone", User::getUserPhoneNumber),
                new StringFilter<>("Status", User::getUserStatus)
        );
        styleUserTable();
        usersTable.setItems(userSampleData());
    }

    private void styleUserTable() {
        usersTable.setPrefSize(1000, 1000);
        usersTable.autosizeColumnsOnInitialization();
        usersTable.features().enableBounceEffect();
        usersTable.features().enableSmoothScrolling(0.5);
    }

    private void userFormDialogPane(Stage stage) throws IOException {
        DialogPane dialogPane = fxmlLoader("forms/UserForm.fxml").load();
        dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
    }

    public void userCreateBtnClicked() {
        dialog.showAndWait();
    }
}
