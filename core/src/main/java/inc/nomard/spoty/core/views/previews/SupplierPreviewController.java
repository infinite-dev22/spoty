package inc.nomard.spoty.core.views.previews;

import inc.nomard.spoty.core.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import io.github.palexdev.mfxcore.controls.*;
import java.net.*;
import java.util.*;
import javafx.fxml.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import lombok.extern.java.*;

@Log
public class SupplierPreviewController implements Initializable {
    @FXML
    public Circle imageHolder;
    @FXML
    public Label nameLbl;
    @FXML
    public Label emailLbl;
    @FXML
    public Label phoneLbl;
    @FXML
    public Label countryLbl;
    @FXML
    public Label cityLbl;
    @FXML
    public Label addressLbl;
    @FXML
    public Label tinLbl;
    @FXML
    public Label purchasesAmountLbl;
    @FXML
    public Label returnsAmountLbl;
    @FXML
    public Label dueAmountLbl;

    public void init(Supplier supplier) {
        if (Objects.nonNull(supplier.getImageUrl()) && !supplier.getImageUrl().isEmpty() && !supplier.getImageUrl().isBlank()) {
            var image = new Image(
                    supplier.getImageUrl(),
                    10000,
                    10000,
                    true,
                    true
            );
            imageHolder.setFill(new ImagePattern(image));
        } else {
            var image = new Image(
                    SpotyCoreResourceLoader.load("images/user-place-holder.png"),
                    10000,
                    10000,
                    true,
                    true
            );
            imageHolder.setFill(new ImagePattern(image));
        }

        nameLbl.setText(supplier.getName());
        emailLbl.setText(supplier.getEmail());
        phoneLbl.setText(supplier.getPhone());
        countryLbl.setText(supplier.getCountry());
        cityLbl.setText(supplier.getCity());
        addressLbl.setText(supplier.getAddress());
        tinLbl.setText(supplier.getTaxNumber());
//        purchasesAmountLbl.setText(supplier);
//        returnsAmountLbl.setText(supplier);
//        dueAmountLbl.setText(supplier);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
