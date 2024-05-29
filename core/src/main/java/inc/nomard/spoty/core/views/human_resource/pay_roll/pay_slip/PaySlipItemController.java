package inc.nomard.spoty.core.views.human_resource.pay_roll.pay_slip;

import inc.nomard.spoty.core.components.navigation.*;
import inc.nomard.spoty.core.views.*;
import io.github.palexdev.mfxcomponents.controls.buttons.*;
import io.github.palexdev.mfxcore.controls.*;
import io.github.palexdev.mfxresources.fonts.*;
import java.net.*;
import java.util.*;
import javafx.fxml.*;
import javafx.scene.layout.*;

import lombok.extern.slf4j.*;

@Slf4j
public class PaySlipItemController implements Initializable {
    @FXML
    public HBox contentPane;
    @FXML
    public Label payRunPeriod;
    @FXML
    public Label employeeCount;
    @FXML
    public Label payRunGenerationDetails;
    @FXML
    public MFXButton vieSalariesBtn;
    @FXML
    public MFXButton exportBtn;
    @FXML
    public Label payRunStatus;
    @FXML
    public MFXFontIcon calendarIcon;
    @FXML
    public Label payRunCreatedTime;
    @FXML
    public Label sentStatus;
    @FXML
    public Label issues;
    @FXML
    public MFXIconButton editBtn;
    @FXML
    public MFXIconButton deleteBtn;
    @FXML
    public MFXButton sendBtn;
    @FXML
    public MFXFontIcon userTickIcon;
    @FXML
    public MFXFontIcon redFlagIcon;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buildIcons();
    }

    private void buildIcons() {
        MFXFontIcon downLoadIcon = new MFXFontIcon();
        MFXFontIcon editIcon = new MFXFontIcon();
        MFXFontIcon deleteIcon = new MFXFontIcon();

        editIcon.setIconsProvider(IconsProviders.FONTAWESOME_REGULAR);
        deleteIcon.setIconsProvider(IconsProviders.FONTAWESOME_REGULAR);

        downLoadIcon.setDescription("fas-arrow-up-from-bracket");
        editIcon.setDescription("far-pen-to-square");
        deleteIcon.setDescription("far-trash-can");

        deleteBtn.setIcon(deleteIcon);
        editBtn.setIcon(editIcon);
        exportBtn.setGraphic(downLoadIcon);
    }

    public void viewSalariesBtnClicked() {
        BaseController.navigation.navigate(Pages.getSalariesPane());
    }
}
