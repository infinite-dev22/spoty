package inc.nomard.spoty.core.views.previews;

import inc.nomard.spoty.network_bridge.dtos.stock_ins.StockInMaster;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class StockInPreviewController implements Initializable {
    private static StockInPreviewController instance;
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

    public static StockInPreviewController getInstance() {
        if (Objects.equals(instance, null))
            return instance = new StockInPreviewController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void init(StockInMaster stockInMaster) {

    }
}
