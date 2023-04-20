package org.infinite.spoty.controller.people.users;

import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.infinite.spoty.model.User;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.data.SampleData.userSampleData;

public class UsersController implements Initializable {
    private MFXTableView<User> usersTable;

    @FXML
    public BorderPane userContentPane;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(() -> Platform.runLater(() -> {
            userContentPane.setCenter(getUserTable());
            setupTable();
        })).start();
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

        usersTable.setItems(userSampleData());
    }

    private MFXTableView<User> getUserTable() {
        usersTable = new MFXTableView<>();
        usersTable.setPrefSize(1000, 1000);
        usersTable.autosizeColumnsOnInitialization();
        usersTable.features().enableBounceEffect();
        usersTable.features().enableSmoothScrolling(0.5);
        return usersTable;
    }
}
