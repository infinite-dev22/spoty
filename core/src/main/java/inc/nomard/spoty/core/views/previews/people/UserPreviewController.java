/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

package inc.nomard.spoty.core.views.previews.people;

import inc.nomard.spoty.core.SpotyCoreResourceLoader;
import inc.nomard.spoty.network_bridge.dtos.UserProfile;
import io.github.palexdev.mfxcore.controls.Label;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

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
