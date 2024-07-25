package inc.nomard.spoty.core.views.pages;

import atlantafx.base.util.*;
import inc.nomard.spoty.core.viewModels.hrm.pay_roll.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.core.views.util.*;
import io.github.palexdev.materialfx.controls.*;
import java.util.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class PaySlipPage extends OutlinePage {
    public VBox paySlipItemHolder;
    public ScrollPane scrollPane;
    private TextField searchBar;
    private MFXProgressSpinner progress;

    public PaySlipPage() {
        addNode(init());
        progress.setManaged(true);
        progress.setVisible(true);
        PaySlipViewModel.getAllPaySlips(this::onDataInitializationSuccess, this::errorMessage);
    }

    private void onDataInitializationSuccess() {
        progress.setManaged(false);
        progress.setVisible(false);
    }

    private void setPayslipItems() {
        PaySlipItem paySlipItem = new PaySlipItem();
        paySlipItemHolder.getChildren().addAll(paySlipItem);
    }

    public BorderPane init() {
        var pane = new BorderPane();
        pane.setTop(buildTop());
        pane.setCenter(buildCenter());
        setSearchBar();
        setPayslipItems();
        createBtnAction();
        return pane;
    }

    private HBox buildLeftTop() {
        progress = new MFXProgressSpinner();
        progress.setMinSize(30d, 30d);
        progress.setPrefSize(30d, 30d);
        progress.setMaxSize(30d, 30d);
        progress.setVisible(false);
        progress.setManaged(false);
        var hbox = new HBox(progress);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
        HBox.setHgrow(hbox, Priority.ALWAYS);
        return hbox;
    }

    private HBox buildCenterTop() {
        searchBar = new TextField();
        searchBar.setPromptText("Search payslips");
        searchBar.setMinWidth(300d);
        searchBar.setPrefWidth(500d);
        searchBar.setMaxWidth(700d);
        var hbox = new HBox(searchBar);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
        HBox.setHgrow(hbox, Priority.ALWAYS);
        return hbox;
    }

    private HBox buildRightTop() {
        var createBtn = new Button("Create");
        createBtn.setDefaultButton(true);
        var hbox = new HBox(createBtn);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
        HBox.setHgrow(hbox, Priority.ALWAYS);
        return hbox;
    }

    private HBox buildTop() {
        var hbox = new HBox();
        hbox.getStyleClass().add("card-flat-bottom");
        BorderPane.setAlignment(hbox, Pos.CENTER);
        hbox.setPadding(new Insets(5d));
        hbox.getChildren().addAll(buildLeftTop(), buildCenterTop(), buildRightTop());
        return hbox;
    }

    private AnchorPane buildCenter() {
        return new AnchorPane(buildScrollPane());
    }

    private VBox buildPaySlipItemHolder() {
        paySlipItemHolder = new VBox();
        paySlipItemHolder.setSpacing(10d);
        paySlipItemHolder.getStyleClass().add("card");
        BorderPane.setMargin(paySlipItemHolder, new Insets(20d, 0d, 0d, 0d));
        paySlipItemHolder.setPadding(new Insets(8d));
        return paySlipItemHolder;
    }

    private ScrollPane buildScrollPane() {
        scrollPane = new ScrollPane(buildPaySlipItemHolder());
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.getStyleClass().add("card-flat");
        scrollPane.setNodeOrientation(NodeOrientation.INHERIT);
        NodeUtils.setAnchors(scrollPane, new Insets(0d));
        BorderPane.setAlignment(scrollPane, Pos.CENTER);
        BorderPane.setMargin(scrollPane, new Insets(20d, 0d, 0d, 0d));
        return scrollPane;
    }

    public void createBtnAction() {
    }

    public void setSearchBar() {
        searchBar.textProperty().addListener((observableValue, ov, nv) -> {
            if (Objects.equals(ov, nv)) {
                return;
            }
            if (ov.isBlank() && ov.isEmpty() && nv.isBlank() && nv.isEmpty()) {
                PaySlipViewModel.getAllPaySlips(null, null);
            }
            progress.setManaged(true);
            progress.setVisible(true);
            PaySlipViewModel.searchItem(nv, () -> {
                progress.setVisible(false);
                progress.setManaged(false);
            }, this::errorMessage);
        });
    }

    private void errorMessage(String message) {
        displayNotification(message, MessageVariants.ERROR, "fas-triangle-exclamation");
        progress.setManaged(false);
        progress.setVisible(false);
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
        if (!AppManager.getMorphPane().getChildren().contains(notification)) {
            AppManager.getMorphPane().getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent -> SpotyMessage.delay(notification));
        }
    }
}
