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

import inc.nomard.spoty.core.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import io.github.palexdev.mfxcore.controls.*;
import java.net.*;
import java.util.*;
import javafx.fxml.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import lombok.extern.java.*;

@Log
public class CustomerPreviewController implements Initializable {
    @FXML
    public Circle imageHolder;
    @FXML
    public Label tinLbl;
    @FXML
    public Label addressLbl;
    @FXML
    public Label cityLbl;
    @FXML
    public Label countryLbl;
    @FXML
    public Label phoneLbl;
    @FXML
    public Label emailLbl;
    @FXML
    public Label nameLbl;
    @FXML
    public Label salesAmountLbl;
    @FXML
    public Label returnsAmountLbl;
    @FXML
    public Label dueAmountLbl;

    public void init(Customer customer) {
        if (Objects.nonNull(customer.getImageUrl()) && !customer.getImageUrl().isEmpty() && !customer.getImageUrl().isBlank()) {
            var image = new Image(
                    customer.getImageUrl(),
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

        nameLbl.setText(customer.getName());
        emailLbl.setText(customer.getEmail());
        phoneLbl.setText(customer.getPhone());
        countryLbl.setText(customer.getCountry());
        cityLbl.setText(customer.getCity());
        addressLbl.setText(customer.getAddress());
        tinLbl.setText(customer.getTaxNumber());
//        purchasesAmountLbl.setText(customer);
//        returnsAmountLbl.setText(customer);
//        dueAmountLbl.setText(customer);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
