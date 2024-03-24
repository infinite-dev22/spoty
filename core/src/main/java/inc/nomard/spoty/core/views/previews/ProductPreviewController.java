package inc.nomard.spoty.core.views.previews;

import inc.nomard.spoty.core.viewModels.ProductViewModel;
import inc.nomard.spoty.network_bridge.dtos.Product;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

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
        productType.setText(product.getProductType());
        productCost.setText(String.valueOf(product.getCost()));
        productPrice.setText(String.valueOf(product.getPrice()));
        productUnit.setText(product.getUnit().getName());
        productTax.setText(String.valueOf(product.getNetTax()));
        stockQuantity.setText(String.valueOf(product.getQuantity()));
        alertQuantity.setText(String.valueOf(product.getStockAlert()));
//        productDescription.setText(product.getDescription());
    }
}
