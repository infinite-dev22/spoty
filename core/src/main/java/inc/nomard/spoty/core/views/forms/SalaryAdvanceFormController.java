package inc.nomard.spoty.core.views.forms;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

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
