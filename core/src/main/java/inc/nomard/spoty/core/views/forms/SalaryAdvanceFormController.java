package inc.nomard.spoty.core.views.forms;

import javafx.fxml.Initializable;
import lombok.extern.java.Log;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

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
