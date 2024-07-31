package inc.nomard.spoty.core.views;

import atlantafx.base.util.*;
import static inc.nomard.spoty.core.SpotyCoreResourceLoader.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.*;
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
import javafx.fxml.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class AccountTransactionController implements Initializable {
    private static AccountTransactionController instance;
    private final Stage stage;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXTableView<AccountTransaction> masterTable;
    @FXML
    public HBox leftHeaderPane;
    @FXML
    public MFXProgressSpinner progress;
    private MFXStageDialog dialog;

    private AccountTransactionController(Stage stage) {
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

    public static AccountTransactionController getInstance(Stage stage) {
        if (instance == null) instance = new AccountTransactionController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setIcons();
        setSearchBar();
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<AccountTransaction> accountName =
                new MFXTableColumn<>("Account Name", false, Comparator.comparing(AccountTransaction::getAccountName));
        MFXTableColumn<AccountTransaction> transactionType =
                new MFXTableColumn<>("Type", false, Comparator.comparing(AccountTransaction::getTransactionType));
        MFXTableColumn<AccountTransaction> credit =
                new MFXTableColumn<>("Credit", false, Comparator.comparing(AccountTransaction::getCredit));
        MFXTableColumn<AccountTransaction> debit =
                new MFXTableColumn<>("Debit", false, Comparator.comparing(AccountTransaction::getDebit));
        MFXTableColumn<AccountTransaction> amount =
                new MFXTableColumn<>("Amount", false, Comparator.comparing(AccountTransaction::getAmount));
        MFXTableColumn<AccountTransaction> note =
                new MFXTableColumn<>("Note", false, Comparator.comparing(AccountTransaction::getNote));
        MFXTableColumn<AccountTransaction> transactionDate =
                new MFXTableColumn<>("Date", false, Comparator.comparing(AccountTransaction::getTransactionDate));

        accountName.setRowCellFactory(customer -> new MFXTableRowCell<>(AccountTransaction::getAccountName));
        transactionType.setRowCellFactory(customer -> new MFXTableRowCell<>(AccountTransaction::getTransactionType));
        credit.setRowCellFactory(customer -> new MFXTableRowCell<>(AccountTransaction::getCredit));
        debit.setRowCellFactory(customer -> new MFXTableRowCell<>(AccountTransaction::getDebit));
        amount.setRowCellFactory(customer -> new MFXTableRowCell<>(AccountTransaction::getAmount));
        note.setRowCellFactory(customer -> new MFXTableRowCell<>(AccountTransaction::getNote));
        transactionDate.setRowCellFactory(customer -> new MFXTableRowCell<>(AccountTransaction::getTransactionDate));

        accountName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        transactionType.prefWidthProperty().bind(masterTable.widthProperty().multiply(.25));
        credit.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        debit.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        amount.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        note.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));
        transactionDate.prefWidthProperty().bind(masterTable.widthProperty().multiply(.2));

        masterTable
                .getTableColumns()
                .addAll(accountName, transactionDate, transactionType, credit, debit, amount, note);
        masterTable
                .getFilters()
                .addAll(new StringFilter<>("Account Name", AccountTransaction::getAccountName),
                        new StringFilter<>("Type", AccountTransaction::getTransactionType));
        styleBankTable();

        if (AccountTransactionViewModel.getTransactions().isEmpty()) {
            AccountTransactionViewModel.getTransactions().addListener(
                    (ListChangeListener<AccountTransaction>)
                            c -> masterTable.setItems(AccountTransactionViewModel.transactionsList));
        } else {
            masterTable.itemsProperty().bindBidirectional(AccountTransactionViewModel.transactionsProperty());
        }
    }

    private void styleBankTable() {
        masterTable.setPrefSize(1000, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);
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

    private void setIcons() {
        searchBar.setTrailingIcon(new MFXFontIcon("fas-magnifying-glass"));
    }

    public void setSearchBar() {
        searchBar.textProperty().addListener((observableValue, ov, nv) -> {
            if (Objects.equals(ov, nv)) {
                return;
            }
            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                AccountTransactionViewModel.getAllTransactions(null, this::errorMessage);
            }
            AccountTransactionViewModel.transactionsList.addAll(AccountTransactionViewModel.getTransactions().stream()
                    .filter(accountTransaction -> Objects.equals(accountTransaction.getAccountName(), nv))
                    .filter(accountTransaction -> Objects.equals(accountTransaction.getTransactionType(), nv))
                    .toList());
        });
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
