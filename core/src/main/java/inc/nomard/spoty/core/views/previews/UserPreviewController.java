package inc.nomard.spoty.core.views.previews;

import inc.nomard.spoty.core.SpotyCoreResourceLoader;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.UserProfile;
import io.github.palexdev.mfxcore.controls.Label;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import lombok.extern.java.Log;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

@Log
public class UserPreviewController implements Initializable {
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
