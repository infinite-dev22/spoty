package inc.nomard.spoty.core.views.previews;

import inc.nomard.spoty.network_bridge.dtos.purchases.PurchaseMaster;
import inc.nomard.spoty.network_bridge.dtos.transfers.TransferMaster;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class TransferPreviewController implements Initializable {
    private static TransferPreviewController instance;
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

    public static TransferPreviewController getInstance() {
        if (Objects.equals(instance, null))
            return instance = new TransferPreviewController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void init(TransferMaster transferMaster){}
}
