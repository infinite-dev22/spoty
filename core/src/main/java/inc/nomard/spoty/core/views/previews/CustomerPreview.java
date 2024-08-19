package inc.nomard.spoty.core.views.previews;

import atlantafx.base.controls.*;
import atlantafx.base.theme.*;
import inc.nomard.spoty.core.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.functional_paradigm.*;
import inc.nomard.spoty.utils.navigation.Spacer;
import java.util.*;
import javafx.beans.property.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.util.converter.*;
import lombok.extern.java.*;
import org.kordamp.ikonli.fontawesome5.*;
import org.kordamp.ikonli.javafx.*;

@Log
public class CustomerPreview extends BorderPane {
    private static final StringProperty nameProperty = new SimpleStringProperty();
    private static final StringProperty emailProperty = new SimpleStringProperty();
    private static final StringProperty phoneProperty = new SimpleStringProperty();
    private static final StringProperty countryProperty = new SimpleStringProperty();
    private static final StringProperty cityProperty = new SimpleStringProperty();
    private static final StringProperty addressProperty = new SimpleStringProperty();
    private static final StringProperty taxNumberProperty = new SimpleStringProperty();
    private static final DoubleProperty ordersProperty = new SimpleDoubleProperty();
    private static final DoubleProperty returnsProperty = new SimpleDoubleProperty();
    private static final DoubleProperty dueAmountProperty = new SimpleDoubleProperty();
    private static final ObjectProperty<ImagePattern> imageProperty = new SimpleObjectProperty<>();
    private final ModalPane modalPane;

    public CustomerPreview(Customer customer, ModalPane modalPane) {
        this.modalPane = modalPane;
        initUI();
        initData(customer);
    }

    private javafx.scene.text.Text buildHeaderText(String txt) {
        var text = new javafx.scene.text.Text(txt);
        text.getStyleClass().addAll(Styles.TITLE_2, Styles.TEXT_SUBTLE, Styles.TEXT_ITALIC);
        return text;
    }

    private HBox buildSpacedTitledText(String title, StringProperty property) {
        var text1 = new javafx.scene.text.Text(title + ":");
        text1.getStyleClass().add(Styles.TEXT_SUBTLE);
        var text2 = new Text();
        text2.getStyleClass().addAll(Styles.TEXT_BOLD);
        text2.textProperty().bind(property);
        return new HBox(20d, text1, new Spacer(), text2);
    }

    private Circle buildUserProfileHolder(ObjectProperty<ImagePattern> property) {
        var circle = new Circle();
        circle.setCache(true);
        circle.setCacheHint(CacheHint.SPEED);
        circle.fillProperty().bind(property);
        circle.setStroke(Color.web("#ffffff00"));
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setStrokeWidth(0d);
        return circle;
    }

    private HBox buildKpi(String title, DoubleProperty property, Color color) {
        var region = new Region();
        region.prefWidth(5d);
        region.setBackground(new Background(new BackgroundFill(color, null, null)));
        region.getStyleClass().add("card-color-decor");

        var text1 = new Text(title);
        var text2 = new Text("$0.00");
        text2.textProperty().bindBidirectional(property, new NumberStringConverter(AppUtils.decimalFormatter()));
        text2.getStyleClass().addAll(Styles.TEXT_BOLDER, Styles.TEXT_SUBTLE);

        var vbox = new VBox(10d, text1, text2);
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setPrefWidth(200d);
        vbox.setPrefHeight(100d);
        vbox.setSpacing(20d);
        HBox.setHgrow(vbox, Priority.ALWAYS);

        var hbox = new HBox(10d, region, vbox);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getStyleClass().add("card-raised");
        return hbox;
    }

    private VBox buildUserDetails() {
        var vbox = new VBox(20d,
                buildSpacedTitledText("Name", nameProperty),
                buildSpacedTitledText("Email", emailProperty),
                buildSpacedTitledText("Phone Number", phoneProperty),
                buildSpacedTitledText("Country", countryProperty),
                buildSpacedTitledText("City", cityProperty),
                buildSpacedTitledText("Address", addressProperty),
                buildSpacedTitledText("Tax Number(TIN)", taxNumberProperty));
        VBox.setVgrow(vbox, Priority.ALWAYS);
        return vbox;
    }

    private VBox buildLeft() {
        var vbox = new VBox(30d,
                buildUserProfileHolder(imageProperty),
                buildUserDetails());
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10d));
        vbox.getStyleClass().add("card-raised");
        HBox.setHgrow(vbox, Priority.ALWAYS);
        return vbox;
    }

    private VBox buildRight() {
        var vbox = new VBox(10d,
                buildKpi("Orders", ordersProperty, Color.GREEN),
                buildKpi("Returns", returnsProperty, Color.RED),
                buildKpi("Due amount", dueAmountProperty, Color.ORANGE));
        HBox.setHgrow(vbox, Priority.ALWAYS);
        return vbox;
    }

    private VBox buildBody() {
        var hbox = new HBox(20d, buildLeft(), buildRight());
        hbox.setMaxSize(1000d, 900d);
        hbox.setPrefSize(700d, 600d);
        hbox.setMinSize(600d, 500d);
        hbox.setPadding(new Insets(10d));
        var vbox = new VBox(10d,
                buildHeaderText("Customer details"),
                hbox);
        vbox.setAlignment(Pos.CENTER);
        return vbox;
    }

    private ScrollPane buildBodyScroll() {
        var scroll = new ScrollPane((buildBody()));
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        return scroll;
    }

    public void initUI() {
        this.setTop(buildTop());
        this.setTop(buildTop());
        this.setCenter(buildBodyScroll());
        this.setPadding(new Insets(10d));
        this.setMaxWidth(1000d);
        this.setPrefWidth(700d);
        this.setMinWidth(400d);

        this.getStylesheets().addAll(
                SpotyCoreResourceLoader.load("styles/base.css"),
                SpotyCoreResourceLoader.load("styles/Common.css"),
                SpotyCoreResourceLoader.load("styles/TextFields.css"),
                SpotyCoreResourceLoader.load("styles/theming/Default.css")
        );
    }

    public void initData(Customer customer) {
        if (Objects.nonNull(customer.getImageUrl()) && !customer.getImageUrl().isEmpty() && !customer.getImageUrl().isBlank()) {
            var image = new Image(
                    customer.getImageUrl(),
                    10000,
                    10000,
                    true,
                    true
            );
            imageProperty.set(new ImagePattern(image));
        } else {
            var image = new Image(
                    SpotyCoreResourceLoader.load("images/user-place-holder.png"),
                    10000,
                    10000,
                    true,
                    true
            );
            imageProperty.set(new ImagePattern(image));
        }

        nameProperty.set(customer.getName());
        emailProperty.set(customer.getEmail());
        phoneProperty.set(customer.getPhone());
        countryProperty.set(customer.getCountry());
        cityProperty.set(customer.getCity());
        addressProperty.set(customer.getAddress());
        taxNumberProperty.set(customer.getTaxNumber());
        // ordersProperty.set(customer.getTotalOrders());
        // returnsProperty.set(customer.getTotalReturns());
        // dueAmountProperty.set(customer.getTotalDueAmount());
    }

    private FontIcon buildFontIcon(SpotyGotFunctional.ParameterlessConsumer onAction, String styleClass) {
        var icon = new FontIcon(FontAwesomeSolid.CIRCLE);
        icon.setOnMouseClicked(event -> onAction.run());
        icon.getStyleClass().addAll(styleClass, Styles.DANGER);
        return icon;
    }

    private HBox buildTop() {
        var hbox = new HBox(buildFontIcon(this::dispose, "close-icon"));
        hbox.setMaxHeight(20d);
        hbox.setMinHeight(20d);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    public void dispose() {
        modalPane.hide(true);
        modalPane.setPersistent(false);
    }
}
