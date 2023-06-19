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

package org.infinite.spoty.views.people;

import io.github.palexdev.materialfx.utils.others.loader.MFXLoader;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoaderBean;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.infinite.spoty.views.people.customers.CustomerController;
import org.infinite.spoty.views.people.suppliers.SupplierController;
import org.infinite.spoty.views.people.users.UsersController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.loadURL;
import static org.infinite.spoty.Utils.createToggle;

public class PeopleController implements Initializable {
    private static PeopleController instance;
    private final Stage stage;
    @FXML
    public VBox peopleNavbar;
    @FXML
    public StackPane contentPane;

    private PeopleController(Stage stage) {
        this.stage = stage;
    }

    public static PeopleController getInstance(Stage stage) {
        if (instance == null)
            instance = new PeopleController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MFXLoader loader = new MFXLoader();
        loader.addView(MFXLoaderBean.of("CUSTOMERS", loadURL("fxml/people/customers/Customers.fxml")).setBeanToNodeMapper(() -> createToggle("fas-user-tie", "Customers")).setControllerFactory(c -> CustomerController.getInstance(stage)).setDefaultRoot(true).get());
        loader.addView(MFXLoaderBean.of("SUPPLIERS", loadURL("fxml/people/suppliers/Suppliers.fxml")).setBeanToNodeMapper(() -> createToggle("fas-truck", "Suppliers")).setControllerFactory(c -> SupplierController.getInstance(stage)).get());
        loader.addView(MFXLoaderBean.of("USERS", loadURL("fxml/people/users/Users.fxml")).setBeanToNodeMapper(() -> createToggle("fas-user", "Users")).setControllerFactory(c -> UsersController.getInstance(stage)).get());
        loader.setOnLoadedAction(beans -> {
            List<ToggleButton> nodes = beans.stream()
                    .map(bean -> {
                        ToggleButton toggle = (ToggleButton) bean.getBeanToNodeMapper().get();
                        toggle.setOnAction(event -> contentPane.getChildren().setAll(bean.getRoot()));
                        if (bean.isDefaultView()) {
                            contentPane.getChildren().setAll(bean.getRoot());
                            toggle.setSelected(true);
                        }
                        return toggle;
                    })
                    .toList();
            peopleNavbar.getChildren().setAll(nodes);
        });
        Platform.runLater(loader::start);
    }
}
