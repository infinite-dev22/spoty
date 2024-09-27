package inc.nomard.spoty.core.views.pages;

import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.utils.navigation.Spacer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.extern.java.Log;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

@Log
public class PaySlipItem extends HBox {
    public Label payRunPeriod;
    public Label employeeCount;
    public Label payRunGenerationDetails;
    public Label payRunStatus;
    public Label issues;
    public Button viewSalariesBtn,
            exportBtn,
            sendBtn;
    public Button editBtn,
            deleteBtn;

    public PaySlipItem() {
        init();
    }

    public void init() {
        this.setPrefHeight(200d);
        this.setSpacing(10d);
        this.getStyleClass().add("card-flat");
        this.setPadding(new Insets(8d));
        this.getChildren().addAll(buildLeft(), new Spacer(), buildCenter(), new Spacer(), buildRight());
        this.setOnMouseClicked(event -> AppManager.getNavigation().navigate(SalaryPage.class));
        buildIcons();
    }

    private VBox buildLeft() {
        payRunPeriod = new Label();
        HBox.setHgrow(payRunPeriod, Priority.ALWAYS);
        employeeCount = new Label();
        var hbox1 = new HBox();
        hbox1.setAlignment(Pos.CENTER_LEFT);
        hbox1.setSpacing(10d);
        hbox1.getChildren().addAll(payRunPeriod, employeeCount);
        payRunGenerationDetails = new Label();
        viewSalariesBtn = new Button("View Salaries");
        viewSalariesBtn.getStyleClass().add(Styles.BUTTON_OUTLINED);
        exportBtn = new Button("Export");
        exportBtn.getStyleClass().add(Styles.BUTTON_OUTLINED);
        var hbox2 = new HBox();
        hbox2.setAlignment(Pos.CENTER_LEFT);
        hbox2.setSpacing(10d);
        hbox2.getChildren().addAll(viewSalariesBtn, exportBtn);

        var vbox = new VBox();
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setSpacing(10d);
        HBox.setHgrow(vbox, Priority.ALWAYS);
        vbox.getChildren().addAll(hbox1, payRunGenerationDetails, hbox2);
        return vbox;
    }

    private HBox buildIcon(Ikon description, String value) {
        var icon = new FontIcon(description);
        var label = new Label(value);
        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(10d);
        hbox.getChildren().addAll(icon, label);
        return hbox;
    }

    private VBox buildCenter() {
        payRunStatus = new Label();

        var vbox = new VBox();
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setSpacing(10d);
        HBox.setHgrow(vbox, Priority.ALWAYS);
        vbox.getChildren().addAll(payRunStatus,
                buildIcon(FontAwesomeSolid.CALENDAR, "10th/Mar/2024"),
                buildIcon(FontAwesomeSolid.CHECK, "Sent"),
                buildIcon(FontAwesomeSolid.FLAG, "No Issues"));
        return vbox;
    }

    private HBox buildRight() {
        editBtn = new Button();
        deleteBtn = new Button();
        sendBtn = new Button("Send PaySlips");
        sendBtn.setDefaultButton(true);

        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(10d);
        HBox.setHgrow(hbox, Priority.ALWAYS);
        hbox.getChildren().addAll(editBtn, deleteBtn, sendBtn);
        return hbox;
    }

    private void buildIcons() {
        exportBtn.setGraphic(new FontIcon(FontAwesomeRegular.CARET_SQUARE_DOWN));
        editBtn.setGraphic(new FontIcon(FontAwesomeRegular.EDIT));
        deleteBtn.setGraphic(new FontIcon(FontAwesomeRegular.TRASH_ALT));
    }
}
