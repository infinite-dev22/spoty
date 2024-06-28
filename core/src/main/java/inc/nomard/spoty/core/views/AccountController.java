package inc.nomard.spoty.core.views;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.views.components.*;
import inc.nomard.spoty.core.views.forms.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.mfxresources.fonts.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class AccountController implements Initializable {
    private static AccountController instance;
    private final Stage stage;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXTableView<Account> masterTable;
    @FXML
    public HBox leftHeaderPane;
    @FXML
    public MFXProgressSpinner progress;
    private MFXStageDialog dialog;

    private AccountController(Stage stage) {
        this.stage = stage;
        Platform.runLater(
                () -> {
                    try {
                        customerFormDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public static AccountController getInstance(Stage stage) {
        if (instance == null) instance = new AccountController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setIcons();
        setSearchBar();
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<Account> accountName =
                new MFXTableColumn<>("Account Name", false, Comparator.comparing(Account::getAccountName));
        MFXTableColumn<Account> accountNumber =
                new MFXTableColumn<>("Account Number", false, Comparator.comparing(Account::getAccountNumber));
        MFXTableColumn<Account> credit =
                new MFXTableColumn<>("Credit", false, Comparator.comparing(Account::getCredit));
        MFXTableColumn<Account> debit =
                new MFXTableColumn<>("Debit", false, Comparator.comparing(Account::getDebit));
        MFXTableColumn<Account> balance =
                new MFXTableColumn<>("Balance", false, Comparator.comparing(Account::getBalance));
        MFXTableColumn<Account> description =
                new MFXTableColumn<>("Description", false, Comparator.comparing(Account::getDescription));

        accountName.setRowCellFactory(customer -> new MFXTableRowCell<>(Account::getAccountName));
        accountNumber.setRowCellFactory(customer -> new MFXTableRowCell<>(Account::getAccountNumber));
        credit.setRowCellFactory(customer -> new MFXTableRowCell<>(Account::getCredit));
        debit.setRowCellFactory(customer -> new MFXTableRowCell<>(Account::getDebit));
        balance.setRowCellFactory(customer -> new MFXTableRowCell<>(Account::getBalance));
        description.setRowCellFactory(customer -> new MFXTableRowCell<>(Account::getDescription));

        accountName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        accountNumber.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        credit.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        debit.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        balance.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        description.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));

        masterTable
                .getTableColumns()
                .addAll(accountName, accountNumber, credit, debit, balance, description);
        masterTable
                .getFilters()
                .addAll(new StringFilter<>("Account Name", Account::getAccountName),
                        new StringFilter<>("Account Number", Account::getAccountNumber),
                        new StringFilter<>("Balance", Account::getBalance));
        styleAccountTable();

        if (AccountViewModel.getAccounts().isEmpty()) {
            AccountViewModel.getAccounts().addListener(
                    (ListChangeListener<Account>)
                            c -> masterTable.setItems(AccountViewModel.accountsList));
        } else {
            masterTable.itemsProperty().bindBidirectional(AccountViewModel.accountsProperty());
        }
    }

    private void styleAccountTable() {
        masterTable.setPrefSize(1000, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<Account> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<Account>) event.getSource())
                                        .show(
                                                masterTable.getScene().getWindow(),
                                                event.getScreenX(),
                                                event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<Account> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");
        MFXContextMenuItem deposit = new MFXContextMenuItem("Deposit");

        // Actions
        // Delete
        delete.setOnAction(event -> new DeleteConfirmationDialog(() -> {
            AccountViewModel.deleteItem(obj.getData().getId(), this::onSuccess, this::successMessage, this::errorMessage);
            event.consume();
        }, obj.getData().getAccountName(), stage, contentPane));
        // Edit
        edit.setOnAction(
                event -> {
                    AccountViewModel.getItem(obj.getData().getId(), () -> dialog.showAndWait(), this::errorMessage);
                    event.consume();
                });
        // Deposit
        deposit.setOnAction(
                event -> {
                    AccountViewModel.getItem(obj.getData().getId(), () -> dialog.showAndWait(), this::errorMessage);
                    event.consume();
                });

        contextMenu.addItems(deposit, edit, delete);

        if (contextMenu.isShowing()) contextMenu.hide();
        return contextMenu;
    }

    private void customerFormDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/AccountForm.fxml");
        fxmlLoader.setControllerFactory(c -> AccountFormController.getInstance(stage));

        MFXGenericDialog dialogContent = fxmlLoader.load();

        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);
        dialogContent.setShowClose(false);

        dialog =
                MFXGenericDialogBuilder.build(dialogContent)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(contentPane)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .get();
        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }

    public void createBtnClicked() {
        dialog.showAndWait();
    }

    private void onSuccess() {
        AccountViewModel.getAllAccounts(null, null);
    }

    private void setIcons() {
        searchBar.setTrailingIcon(new MFXFontIcon("fas-magnifying-glass"));
    }

    public void setSearchBar() {
        searchBar.textProperty().addListener((observableValue, ov, nv) -> {
            if (Objects.equals(ov, nv)) {
                return;
            }
            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                AccountViewModel.getAllAccounts(null, null);
            }
            progress.setVisible(true);
            AccountViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
            }, this::errorMessage);
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
