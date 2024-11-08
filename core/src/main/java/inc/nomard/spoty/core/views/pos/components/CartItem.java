package inc.nomard.spoty.core.views.pos.components;

import inc.nomard.spoty.core.SpotyCoreResourceLoader;
import inc.nomard.spoty.core.views.layout.navigation.Spacer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Objects;

@Setter
@Slf4j
public class CartItem extends HBox {
    private static String logo;
    private static String productName;
    private static String productPrice;
    private static int productQuantity;

    public CartItem() {
        getChildren().addAll(drawRect(), drawBody());
    }

    public CartItem(String logo, String productName, String productPrice, int productQuantity) {
        CartItem.logo = logo;
        CartItem.productName = productName;
        CartItem.productPrice = productPrice;
        CartItem.productQuantity = productQuantity;
        getChildren().addAll(drawRect(), drawBody());
        getStylesheets().add(SpotyCoreResourceLoader.load("styles/base.css"));
        getStylesheets().add(SpotyCoreResourceLoader.load("styles/Common.css"));
        getStyleClass().add("card-raised");
    }

    // Called in constructor.
    private static void setProductImage(Rectangle imageHolder) {
        if (Objects.nonNull(logo) && !logo.isEmpty() && !logo.isBlank()) {
            var image = new Image(
                    logo,
                    10000,
                    10000,
                    true,
                    true
            );
            imageHolder.setFill(new ImagePattern(image));
        } else {
            var image = new Image(
                    SpotyCoreResourceLoader.load("images/upload_image_placeholder.png"),
                    10000,
                    10000,
                    true,
                    true
            );
            imageHolder.setFill(new ImagePattern(image));
        }
    }

    private Rectangle drawRect() {
        var rect = new Rectangle();
        rect.setArcHeight(25);
        rect.setArcWidth(25);
        rect.setCache(true);
        rect.setCacheHint(CacheHint.SPEED);
        rect.setHeight(110);
        rect.setWidth(124);
        setProductImage(rect);
        return rect;
    }

    private VBox drawBody() {
        // Components one.
        var hBox1 = new HBox();
        var label1 = new Label();
        var icon = new FontIcon();
        label1.setText(productName);
        label1.getStyleClass().add("product-name");
        hBox1.getChildren().setAll(label1, new Spacer(), icon);
        hBox1.setAlignment(Pos.CENTER);
        // Components two.
        var hBox2 = new HBox();
        var label2 = new Label();
        var spinner = new Spinner<Integer>();
        label2.setText(productPrice);
        label2.getStyleClass().add("product-price");
        spinner.setEditable(true);
//        spinner.setGraphicTextGap(0);
//        spinner.setSpinnerModel(new IntegerSpinnerModel(1));
        hBox2.getChildren().setAll(label2, new Spacer(), spinner);
        hBox2.setAlignment(Pos.CENTER);
        // Body component.
        var vBox = new VBox(hBox1, hBox2);
        vBox.setPrefHeight(110);
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(10));
        HBox.setHgrow(vBox, Priority.ALWAYS);
        return vBox;
    }
}
