package org.infinite.spotmarkpos.controller;

import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.utils.ToggleButtonsUtil;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoader;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoaderBean;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.application.Application;
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
import java.util.ResourceBundle;

import static org.infinite.spotmarkpos.loader.SpotMarkResourceLoader.loadURL;

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

    private double xOffset;

    private double yOffset;

    private final ToggleGroup toggleGroup;

    public BaseController(Stage stage) {
        this.stage = stage;
        this.toggleGroup = new ToggleGroup();
        ToggleButtonsUtil.addAlwaysOneSelectedSupport(toggleGroup);
    }

    @FXML
    void closeIconClicked(MouseEvent event) {
        Platform.exit();
    }

    @FXML
    void maximizeIconClicked(MouseEvent event) {
//        ((Stage) rootPane.getScene().getWindow()).setFullScreen(!((Stage) rootPane.getScene().getWindow()).isMaximized());
        if (!((Stage) rootPane.getScene().getWindow()).isMaximized()){
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

//        initializeLoader();

        ScrollUtils.addSmoothScrolling(scrollPane);
    }

//    public void initializeLoader() {
//        MFXLoader loader = new MFXLoader();
//        loader.addView(MFXLoaderBean.of("BUTTONS", loadURL("fxml/Buttons.fxml")).setBeanToNodeMapper(() -> createToggle("fas-circle-dot", "Buttons")).setDefaultRoot(true).get());
//        loader.addView(MFXLoaderBean.of("CHECKS_RADIOS_TOGGLES", loadURL("fxml/ChecksRadiosToggles.fxml")).setBeanToNodeMapper(() -> createToggle("fas-toggle-on", "Checks, Radios, Toggles")).get());
//        loader.addView(MFXLoaderBean.of("COMBOS", loadURL("fxml/ComboBoxes.fxml")).setBeanToNodeMapper(() -> createToggle("fas-square-caret-down", "ComboBoxes")).get());
////        loader.addView(MFXLoaderBean.of("DIALOGS", loadURL("fxml/Dialogs.fxml")).setBeanToNodeMapper(() -> createToggle("fas-comments", "Dialogs")).setControllerFactory(c -> new DialogsController(stage)).get());
//        loader.addView(MFXLoaderBean.of("TEXT-FIELDS", loadURL("fxml/TextFields.fxml")).setBeanToNodeMapper(() -> createToggle("fas-italic", "Fields")).get());
//        loader.addView(MFXLoaderBean.of("LISTS", loadURL("fxml/ListViews.fxml")).setBeanToNodeMapper(() -> createToggle("fas-rectangle-list", "Lists")).get());
////        loader.addView(MFXLoaderBean.of("NOTIFICATIONS", loadURL("fxml/Notifications.fxml")).setBeanToNodeMapper(() -> createToggle("fas-bell", "Notifications")).setControllerFactory(c -> new NotificationsController(stage)).get());
//        loader.addView(MFXLoaderBean.of("PICKERS", loadURL("fxml/Pickers.fxml")).setBeanToNodeMapper(() -> createToggle("fas-calendar", "Pickers")).get());
//        loader.addView(MFXLoaderBean.of("PROGRESS", loadURL("fxml/Progress.fxml")).setBeanToNodeMapper(() -> createToggle("fas-bars-progress", "Progress")).get());
//        loader.addView(MFXLoaderBean.of("SCROLL-PANES", loadURL("fxml/ScrollPanes.fxml")).setBeanToNodeMapper(() -> createToggle("fas-bars-progress", "Scroll Panes", 90)).get());
//        loader.addView(MFXLoaderBean.of("SLIDERS", loadURL("fxml/Sliders.fxml")).setBeanToNodeMapper(() -> createToggle("fas-sliders", "Sliders")).get());
//        loader.addView(MFXLoaderBean.of("STEPPER", loadURL("fxml/Stepper.fxml")).setBeanToNodeMapper(() -> createToggle("fas-stairs", "Stepper")).get());
//        loader.addView(MFXLoaderBean.of("TABLES", loadURL("fxml/TableViews.fxml")).setBeanToNodeMapper(() -> createToggle("fas-table", "Tables")).get());
//        loader.addView(MFXLoaderBean.of("FONT-RESOURCES", loadURL("fxml/FontResources.fxml")).setBeanToNodeMapper(() -> createToggle("fas-icons", "Font Resources")).get());
//    }
//
//    private ToggleButton createToggle(String icon, String text) {
//        return createToggle(icon, text, 0);
//    }
//
//    private ToggleButton createToggle(String icon, String text, double rotate) {
//        MFXIconWrapper wrapper = new MFXIconWrapper(icon, 24, 32);
//        MFXRectangleToggleNode toggleNode = new MFXRectangleToggleNode(text, wrapper);
//        toggleNode.setAlignment(Pos.CENTER_LEFT);
//        toggleNode.setMaxWidth(Double.MAX_VALUE);
//        toggleNode.setToggleGroup(toggleGroup);
//        if (rotate != 0) wrapper.getIcon().setRotate(rotate);
//        return toggleNode;
//    }
}
