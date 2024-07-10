package inc.nomard.spoty.core.views.pages;

import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.utils.navigation.*;
import io.github.palexdev.mfxcomponents.controls.buttons.*;
import io.github.palexdev.mfxcore.controls.*;
import io.github.palexdev.mfxresources.fonts.*;
import javafx.geometry.*;
import javafx.scene.layout.*;
import lombok.extern.java.*;

@Log
public class PaySlipItem extends HBox {
    public Label payRunPeriod;
    public Label employeeCount;
    public Label payRunGenerationDetails;
    public Label payRunStatus;
    public Label issues;
    public MFXButton viewSalariesBtn,
            exportBtn,
            sendBtn;
    public MFXIconButton editBtn,
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
        viewSalariesBtn = new MFXButton("View Salaries");
        viewSalariesBtn.getStyleClass().add("outlined");
        exportBtn = new MFXButton("Export");
        exportBtn.getStyleClass().add("outlined");
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

    private HBox buildIcon(String description, String value) {
        var icon = new MFXFontIcon(description);
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
                buildIcon("fas-calendar", "10th/Mar/2024"),
                buildIcon("fas-check", "Sent"),
                buildIcon("fas-flag", "No Issues"));
        return vbox;
    }

    private HBox buildRight() {
        editBtn = new MFXIconButton();
        deleteBtn = new MFXIconButton();
        sendBtn = new MFXButton("Send PaySlips");
        sendBtn.getStyleClass().add("filled");

        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(10d);
        HBox.setHgrow(hbox, Priority.ALWAYS);
        hbox.getChildren().addAll(editBtn, deleteBtn, sendBtn);
        return hbox;
    }

    private void buildIcons() {
        MFXFontIcon downLoadIcon = new MFXFontIcon();
        MFXFontIcon editIcon = new MFXFontIcon();
        MFXFontIcon deleteIcon = new MFXFontIcon();

        editIcon.setIconsProvider(IconsProviders.FONTAWESOME_REGULAR);
        deleteIcon.setIconsProvider(IconsProviders.FONTAWESOME_REGULAR);

        downLoadIcon.setDescription("fas-arrow-up-from-bracket");
        editIcon.setDescription("far-pen-to-square");
        deleteIcon.setDescription("far-trash-can");

        deleteBtn.setIcon(deleteIcon);
        editBtn.setIcon(editIcon);
        exportBtn.setGraphic(downLoadIcon);
    }
}
