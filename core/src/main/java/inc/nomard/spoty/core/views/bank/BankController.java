package inc.nomard.spoty.core.views.bank;

import inc.nomard.spoty.core.components.animations.SpotyAnimations;
import inc.nomard.spoty.core.viewModels.BankViewModel;
import inc.nomard.spoty.core.views.forms.BankFormController;
import inc.nomard.spoty.network_bridge.dtos.Bank;
import inc.nomard.spoty.utils.SpotyThreader;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static inc.nomard.spoty.core.SpotyCoreResourceLoader.fxmlLoader;

public class BankController implements Initializable {
    private static BankController instance;
    @FXML
    public BorderPane contentPane;
    @FXML
    public MFXTextField searchBar;
    @FXML
    public HBox actionsPane;
    @FXML
    public MFXButton importBtn;
    @FXML
    public MFXButton createBtn;
    @FXML
    public MFXTableView<Bank> masterTable;
    @FXML
    public HBox refresh;
    private MFXStageDialog dialog;
    private RotateTransition transition;

    private BankController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        customerFormDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public static BankController getInstance(Stage stage) {
        if (instance == null) instance = new BankController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setIcons();
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<Bank> bankName =
                new MFXTableColumn<>("Bank Name", false, Comparator.comparing(Bank::getBankName));
        MFXTableColumn<Bank> accountName =
                new MFXTableColumn<>("A/C Name", false, Comparator.comparing(Bank::getAccountName));
        MFXTableColumn<Bank> accountNumber =
                new MFXTableColumn<>("A/C Number", false, Comparator.comparing(Bank::getAccountNumber));
        MFXTableColumn<Bank> balance =
                new MFXTableColumn<>("Balance", false, Comparator.comparing(Bank::getBalance));

        bankName.setRowCellFactory(customer -> new MFXTableRowCell<>(Bank::getBankName));
        accountName.setRowCellFactory(customer -> new MFXTableRowCell<>(Bank::getAccountName));
        accountNumber.setRowCellFactory(customer -> new MFXTableRowCell<>(Bank::getAccountNumber));
        balance.setRowCellFactory(customer -> new MFXTableRowCell<>(Bank::getBalance));

        bankName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.1));
        accountName.prefWidthProperty().bind(masterTable.widthProperty().multiply(.3));
        accountNumber.prefWidthProperty().bind(masterTable.widthProperty().multiply(.3));
        balance.prefWidthProperty().bind(masterTable.widthProperty().multiply(.3));

        masterTable
                .getTableColumns()
                .addAll(bankName, accountName, accountNumber, balance);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Bank Name", Bank::getBankName),
                        new StringFilter<>("A/C Name", Bank::getAccountName),
                        new StringFilter<>("A/C Number", Bank::getAccountNumber),
                        new StringFilter<>("Balance", Bank::getBalance));
        styleBankTable();

        if (BankViewModel.getBanks().isEmpty()) {
            BankViewModel.getBanks().addListener(
                    (ListChangeListener<Bank>)
                            c -> masterTable.setItems(BankViewModel.banksList));
        } else {
            masterTable.itemsProperty().bindBidirectional(BankViewModel.banksProperty());
        }
    }

    private void styleBankTable() {
        masterTable.setPrefSize(1000, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);

        masterTable.setTableRowFactory(
                t -> {
                    MFXTableRow<Bank> row = new MFXTableRow<>(masterTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<Bank>) event.getSource())
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

    private MFXContextMenu showContextMenu(MFXTableRow<Bank> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(masterTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            BankViewModel.deleteItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    e.consume();
                });
        // Edit
        edit.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            BankViewModel.getItem(obj.getData().getId(), this::onAction, this::onSuccess, this::onFailed);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    dialog.showAndWait();
                    e.consume();
                });

        contextMenu.addItems(edit, delete);

        if (contextMenu.isShowing()) contextMenu.hide();
        return contextMenu;
    }

    private void customerFormDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("views/forms/BankForm.fxml");
        fxmlLoader.setControllerFactory(c -> BankFormController.getInstance());

        MFXGenericDialog dialogContent = fxmlLoader.load();

        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);

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

    private void onAction() {
        transition.playFromStart();
        transition.setOnFinished((ActionEvent event) -> transition.playFromStart());
    }

    private void onSuccess() {
        transition.setOnFinished(null);
    }

    private void onFailed() {
        transition.setOnFinished(null);
    }

    private void setIcons() {
        var refreshIcon = new MFXFontIcon("fas-arrows-rotate", 24);
        refresh.getChildren().addFirst(refreshIcon);

        transition = SpotyAnimations.rotateTransition(refreshIcon, Duration.millis(1000), 360);

        refreshIcon.setOnMouseClicked(mouseEvent -> BankViewModel.getAllBanks(this::onAction, this::onSuccess, this::onFailed));
    }
}