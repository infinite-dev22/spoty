package inc.nomard.spoty.core.views.previews;

import inc.nomard.spoty.core.SpotyCoreResourceLoader;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.Employee;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

@Slf4j
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

    public void init(Employee employee) {
        if (Objects.nonNull(employee.getAvatar()) && !employee.getAvatar().isEmpty() && !employee.getAvatar().isBlank()) {
            var image = new Image(
                    employee.getAvatar(),
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

        nameLbl.setText(employee.getName());
        emailLbl.setText(employee.getEmail());
        phoneLbl.setText(employee.getPhone());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
