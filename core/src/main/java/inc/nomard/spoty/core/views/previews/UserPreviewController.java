package inc.nomard.spoty.core.views.previews;

import inc.nomard.spoty.core.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import io.github.palexdev.mfxcore.controls.*;
import java.net.*;
import java.util.*;
import javafx.fxml.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.*;
import lombok.extern.java.*;

@Log
public class UserPreviewController implements Initializable {
    private static UserPreviewController instance;
    @FXML
    public Circle imageHolder;
    @FXML
    public Label nameLbl;
    @FXML
    public Label emailLbl;
    @FXML
    public Label phoneLbl;
    @FXML
    public Label genderLbl;
    @FXML
    public Label dobLbl;

    public UserPreviewController(Stage stage) {
    }

    public static UserPreviewController getInstance(Stage stage) {
        if (instance == null) instance = new UserPreviewController(stage);
        return instance;
    }

    public void init(UserProfile supplier) {
        if (Objects.nonNull(supplier.getAvatar()) && !supplier.getAvatar().isEmpty() && !supplier.getAvatar().isBlank()) {
            var image = new Image(
                    supplier.getAvatar(),
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
        genderLbl.setText(supplier.getGender());
        dobLbl.setText(supplier.getDob());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
