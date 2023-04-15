package org.infinite.spoty.controller;

import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.utils.ToggleButtonsUtil;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoader;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoaderBean;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import io.github.palexdev.materialfx.utils.ScrollUtils;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.loadURL;
import static org.infinite.spoty.Utils.createToggle;

public class BaseController implements Initializable {

    public final Stage stage;

    @FXML
    public MFXFontIcon closeIcon;

    @FXML
    public MFXFontIcon maximizeIcon;

    @FXML
    public MFXFontIcon minimizeIcon;

    @FXML
    public StackPane contentPane;

    @FXML
    public VBox navBar;

    @FXML
    public HBox windowHeader;

    @FXML
    public MFXScrollPane scrollPane;

    @FXML
    public AnchorPane rootPane;

    @FXML
    public VBox settingsHolder;

    private double xOffset;

    private double yOffset;

    public BaseController(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void closeIconClicked(MouseEvent event) {
        Platform.exit();
    }

    @FXML
    void maximizeIconClicked(MouseEvent event) {
//        ((Stage) rootPane.getScene().getWindow()).setFullScreen(!((Stage) rootPane.getScene().getWindow()).isMaximized());
        if (!((Stage) rootPane.getScene().getWindow()).isMaximized()) {
            ((Stage) rootPane.getScene().getWindow()).setMaximized(true);
        } else {
            ((Stage) rootPane.getScene().getWindow()).setMaximized(false);
        }
    }

    @FXML
    void minimizeIconClicked(MouseEvent event) {
        ((Stage) rootPane.getScene().getWindow()).setIconified(true);
    }

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

        windowHeader.setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });
        windowHeader.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });

        initializeLoader();

        ScrollUtils.addSmoothScrolling(scrollPane);
    }

    public void initializeLoader() {
        MFXLoader loader = new MFXLoader();
        loader.addView(MFXLoaderBean.of("DASHBOARD", loadURL("fxml/dashboard/Dashboard.fxml")).setBeanToNodeMapper(() -> createToggle("fas-gauge-high", "Dashboard")).setDefaultRoot(true).get());
        loader.addView(MFXLoaderBean.of("INVENTORY", loadURL("fxml/inventory/Inventory.fxml")).setBeanToNodeMapper(() -> createToggle("fas-toggle-on", "Inventory")).get());
        loader.addView(MFXLoaderBean.of("PURCHASES", loadURL("fxml/purchases/Purchases.fxml")).setBeanToNodeMapper(() -> createToggle("fas-square-caret-down", "Purchases")).get());
        loader.addView(MFXLoaderBean.of("SALES", loadURL("fxml/sales/Sales.fxml")).setBeanToNodeMapper(() -> createToggle("fas-square-caret-down", "Sales")).get());
//        loader.addView(MFXLoaderBean.of("DIALOGS", loadURL("fxml/Dialogs.fxml")).setBeanToNodeMapper(() -> createToggle("fas-comments", "Dialogs")).setControllerFactory(c -> new DialogsController(stage)).get());
        loader.addView(MFXLoaderBean.of("RETURNS", loadURL("fxml/returns/Returns.fxml")).setBeanToNodeMapper(() -> createToggle("fas-italic", "Returns")).get());
        loader.addView(MFXLoaderBean.of("EXPENSES", loadURL("fxml/expenses/Expenses.fxml")).setBeanToNodeMapper(() -> createToggle("fas-rectangle-list", "Expenses")).get());
//        loader.addView(MFXLoaderBean.of("NOTIFICATIONS", loadURL("fxml/Notifications.fxml")).setBeanToNodeMapper(() -> createToggle("fas-bell", "Notifications")).setControllerFactory(c -> new NotificationsController(stage)).get());
        loader.addView(MFXLoaderBean.of("PEOPLE", loadURL("fxml/people/People.fxml")).setBeanToNodeMapper(() -> createToggle("fas-calendar", "People")).get());
//        loader.addView(MFXLoaderBean.of("PROGRESS", loadURL("fxml/Progress.fxml")).setBeanToNodeMapper(() -> createToggle("fas-bars-progress", "Progress")).get());
//        loader.addView(MFXLoaderBean.of("SCROLL-PANES", loadURL("fxml/ScrollPanes.fxml")).setBeanToNodeMapper(() -> createToggle("fas-bars-progress", "Scroll Panes", 90)).get());
//        loader.addView(MFXLoaderBean.of("SLIDERS", loadURL("fxml/Sliders.fxml")).setBeanToNodeMapper(() -> createToggle("fas-sliders", "Sliders")).get());
//        loader.addView(MFXLoaderBean.of("STEPPER", loadURL("fxml/Stepper.fxml")).setBeanToNodeMapper(() -> createToggle("fas-stairs", "Stepper")).get());
//        loader.addView(MFXLoaderBean.of("TABLES", loadURL("fxml/TableViews.fxml")).setBeanToNodeMapper(() -> createToggle("fas-table", "Tables")).get());
//        loader.addView(MFXLoaderBean.of("FONT-RESOURCES", loadURL("fxml/FontResources.fxml")).setBeanToNodeMapper(() -> createToggle("fas-icons", "Font Resources")).get());
        loader.setOnLoadedAction(beans -> {
            List<ToggleButton> nodes = beans.stream()
                    .map(bean -> {
                        ToggleButton toggle = (ToggleButton) bean.getBeanToNodeMapper().get();
                        toggle.setOnAction(event -> contentPane.getChildren().setAll(bean.getRoot()));
                        if (bean.isDefaultView()) {
                            contentPane.getChildren().setAll(bean.getRoot());
                            toggle.setSelected(true);
                        }
                        return toggle;
                    })
                    .toList();
            navBar.getChildren().setAll(nodes);
        });
        loader.start();

        MFXLoader settingsLoader = new MFXLoader();
        settingsLoader.addView(MFXLoaderBean.of("SETTINGS", loadURL("fxml/settings/Settings.fxml")).setBeanToNodeMapper(() -> createToggle("fas-gears", "Settings")).get());
        settingsLoader.setOnLoadedAction(beans -> {
            List<ToggleButton> node = beans.stream().map(bean -> {
                ToggleButton toggle = (ToggleButton) bean.getBeanToNodeMapper().get();
                toggle.setOnAction(event -> contentPane.getChildren().setAll(bean.getRoot()));
                return toggle;
            }).toList();
            settingsHolder.getChildren().setAll(node);
        });
        settingsLoader.start();
    }
}
