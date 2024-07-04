package inc.nomard.spoty.core.views;

import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.*;
import io.github.palexdev.materialfx.enums.*;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.mfxresources.fonts.*;
import java.util.*;
import javafx.geometry.*;
import javafx.scene.layout.*;
import lombok.extern.java.*;

@Log
public class SaleTermPage extends OutlinePage {
    private MFXTextField searchBar;
    private MFXTableView<SaleTermAndCondition> masterTable;

    public SaleTermPage() {
        addNode(init());
    }

    public BorderPane init() {
        var pane = new BorderPane();
        pane.setTop(buildTop());
        pane.setCenter(buildCenter());
        setIcons();
        setupTable();
        return pane;
    }

    private HBox buildLeftTop() {
        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
        HBox.setHgrow(hbox, Priority.ALWAYS);
        return hbox;
    }

    private HBox buildCenterTop() {
        searchBar = new MFXTextField();
        searchBar.setPromptText("Search sale terms");
        searchBar.setFloatMode(FloatMode.DISABLED);
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
        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setPadding(new Insets(0d, 10d, 0d, 10d));
        HBox.setHgrow(hbox, Priority.ALWAYS);
        return hbox;
    }

    private HBox buildTop() {
        var hbox = new HBox();
        hbox.getStyleClass().add("card-flat");
        BorderPane.setAlignment(hbox, Pos.CENTER);
        hbox.setPadding(new Insets(5d));
        hbox.getChildren().addAll(buildLeftTop(), buildCenterTop(), buildRightTop());
        return hbox;
    }

    private AnchorPane buildCenter() {
        masterTable = new MFXTableView<>();
        AnchorPane.setBottomAnchor(masterTable, 0d);
        AnchorPane.setLeftAnchor(masterTable, 0d);
        AnchorPane.setRightAnchor(masterTable, 0d);
        AnchorPane.setTopAnchor(masterTable, 10d);
        return new AnchorPane(masterTable);
    }

    private void setupTable() {
        MFXTableColumn<SaleTermAndCondition> name =
                new MFXTableColumn<>("Name", true, Comparator.comparing(SaleTermAndCondition::getName));
        MFXTableColumn<SaleTermAndCondition> status =
                new MFXTableColumn<>(
                        "Status", true, Comparator.comparing(SaleTermAndCondition::isActive));
        name.setRowCellFactory(category -> new MFXTableRowCell<>(SaleTermAndCondition::getName));
        status.setRowCellFactory(
                category -> new MFXTableRowCell<>(SaleTermAndCondition::isActive));
        name.prefWidthProperty().bind(masterTable.widthProperty().multiply(.5));
        status.prefWidthProperty().bind(masterTable.widthProperty().multiply(.5));
        name.setColumnResizable(false);
        status.setColumnResizable(false);
        masterTable.getTableColumns().addAll(name, status);
        masterTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", SaleTermAndCondition::getName),
                        new BooleanFilter<>("Status", SaleTermAndCondition::isActive));
        styleSaleTermAndConditionTable();
    }

    private void styleSaleTermAndConditionTable() {
        masterTable.setPrefSize(1200, 1000);
        masterTable.features().enableBounceEffect();
        masterTable.features().enableSmoothScrolling(0.5);
    }

    private void setIcons() {
        searchBar.setTrailingIcon(new MFXFontIcon("fas-magnifying-glass"));
    }
}
