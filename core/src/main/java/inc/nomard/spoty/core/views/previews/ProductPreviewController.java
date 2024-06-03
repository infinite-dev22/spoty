package inc.nomard.spoty.core.views.previews;

import inc.nomard.spoty.network_bridge.dtos.*;
import io.github.palexdev.materialfx.dialogs.*;
import java.net.*;
import java.util.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import lombok.extern.java.*;

@Log
public class ProductPreviewController implements Initializable {
    @FXML
    public Label productName,
            productCategory,
            productBrand,
            productType,
            productCost,
            productPrice,
            productUnit,
            productTax,
            stockQuantity,
            alertQuantity,
            productDescription;
    @FXML
    public ImageView barCode,
            productImage;
    @FXML
    public MFXGenericDialog dialog;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void init(Product product) {
        productName.setText(product.getName());
        productCategory.setText(product.getCategory().getName());
        productBrand.setText(product.getBrand().getName());
        productCost.setText(String.valueOf(product.getCostPrice()));
        productPrice.setText(String.valueOf(product.getSalePrice()));
        productUnit.setText(product.getUnit().getName());
        productTax.setText(String.valueOf(product.getTax()));
        stockQuantity.setText(String.valueOf(product.getQuantity()));
        alertQuantity.setText(String.valueOf(product.getStockAlert()));
//        productDescription.setText(product.getDescription());
    }
}
