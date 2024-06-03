package inc.nomard.spoty.core.views.printable.general;

import java.net.*;
import java.util.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import lombok.extern.java.*;

@Log
public class GeneralViewController implements Initializable {
    private static GeneralViewController instance;
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

    public static GeneralViewController getInstance() {
        if (Objects.equals(instance, null))
            return instance = new GeneralViewController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
