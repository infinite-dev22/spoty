package inc.nomard.spoty.core.views.components;

import inc.nomard.spoty.core.SpotyCoreResourceLoader;
import inc.nomard.spoty.core.wrappers.DashboardKPIModelWrapper;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class KPICard extends AnchorPane {
    private final StringProperty name = new SimpleStringProperty();
    private final ObjectProperty<Number> value = new SimpleObjectProperty<>();
    private final String STYLE_SHEET = SpotyCoreResourceLoader.load("styles/Common.css");

    private final Label title;
    private final Label subTitle;
    private final MFXFontIcon icon;

    public KPICard() {
        this.title = buildTitle();
        this.subTitle = buildSubTitle();
        this.icon = buildIcon();

        this.getChildren().addAll(title, subTitle, icon);
        this.setMinSize(229.0, 88.0);
        this.setPrefSize(329.0, 88.0);
        this.setMaxSize(429.0, 88.0);
        this.getStyleClass().add("card-flat");
        this.getStylesheets().add(STYLE_SHEET);
    }

    private Label buildTitle() {
        Label label = new Label();
        label.getStyleClass().add("card-title");
        label.setPrefSize(85.0, 19.0);
        label.textProperty().bind(name);
        AnchorPane.setTopAnchor(label, 10.0);
        AnchorPane.setBottomAnchor(label, 55.0);
        AnchorPane.setRightAnchor(label, 130.0);
        AnchorPane.setLeftAnchor(label, 10.0);
        return label;
    }

    private Label buildSubTitle() {
        Label label = new Label();
        label.getStyleClass().add("card-sub-title");
        label.setPrefSize(85.0, 19.0);
        label.textProperty().bind(value.asString());
        AnchorPane.setTopAnchor(label, 52.0);
        AnchorPane.setBottomAnchor(label, 10.0);
        AnchorPane.setRightAnchor(label, 130.0);
        AnchorPane.setLeftAnchor(label, 10.0);
        return label;
    }

    private MFXFontIcon buildIcon() {
        MFXFontIcon icon = new MFXFontIcon();
        AnchorPane.setTopAnchor(icon, 36.0);
        AnchorPane.setBottomAnchor(icon, 36.0);
        AnchorPane.setRightAnchor(icon, 10.0);
        AnchorPane.setLeftAnchor(icon, 143.0);
        return icon;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public ObjectProperty<Number> valueProperty() {
        return value;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setValue(Number value) {
        this.value.set(value);
    }

    public void bindToModel(DashboardKPIModelWrapper model) {
        this.name.bind(model.nameProperty());
        this.value.bind(model.valueProperty());
    }
}
