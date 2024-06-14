package inc.nomard.spoty.core.views.forms;

import java.net.*;
import java.util.*;
import javafx.fxml.*;
import javafx.stage.*;
import lombok.extern.java.*;

@Log
public class SalaryAdvanceFormController implements Initializable {
    private static SalaryAdvanceFormController instance;
    private final Stage stage;

    public SalaryAdvanceFormController(Stage stage) {
        this.stage = stage;
    }

    public static SalaryAdvanceFormController getInstance(Stage stage) {
        if (Objects.equals(instance, null)) {
            synchronized (SalaryAdvanceFormController.class) {
                instance = new SalaryAdvanceFormController(stage);
            }
        }
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
