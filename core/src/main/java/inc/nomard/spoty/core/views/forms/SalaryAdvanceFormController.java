package inc.nomard.spoty.core.views.forms;

import java.net.*;
import java.util.*;
import javafx.fxml.*;
import lombok.extern.java.*;

@Log
public class SalaryAdvanceFormController implements Initializable {
    private static SalaryAdvanceFormController instance;

    public static SalaryAdvanceFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new SalaryAdvanceFormController();
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
