package inc.nomard.spoty.core.views.previews.returns;

import inc.nomard.spoty.network_bridge.dtos.returns.sale_returns.SaleReturnMaster;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SaleReturnsPreviewController implements Initializable {
    private static SaleReturnsPreviewController instance;
    public Label dateLabel;
    public Label refLabel;
    public Label nameLabel;
    public Label numberLabel;
    public Label emailLabel;
    public TableView itemsTable;
    public TableColumn itemNumber;
    public TableColumn itemName;
    public TableColumn itemQuantity;
    public TableColumn itemCost;
    public Label totalCost;
    public Label totalDiscount;
    public Label netCost;
    public Label signerNameLabel;
    public Label signerIdLabel;

    public static SaleReturnsPreviewController getInstance() {
        if (Objects.equals(instance, null))
            return instance = new SaleReturnsPreviewController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void init(SaleReturnMaster saleReturnsMaster) {
    }
}
