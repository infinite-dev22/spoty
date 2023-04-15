package org.infinite.spoty.controller.people;

import io.github.palexdev.materialfx.utils.others.loader.MFXLoader;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoaderBean;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.loadURL;
import static org.infinite.spoty.Utils.createToggle;

public class PeopleController implements Initializable {

    @FXML
    public VBox peopleNavbar;

    @FXML
    public StackPane contentPane;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MFXLoader loader = new MFXLoader();
        loader.addView(MFXLoaderBean.of("CUSTOMERS", loadURL("fxml/people/customers/Customers.fxml")).setBeanToNodeMapper(() -> createToggle("fas-toggle-on", "Customers")).setDefaultRoot(true).get());
        loader.addView(MFXLoaderBean.of("SUPPLIERS", loadURL("fxml/people/suppliers/Suppliers.fxml")).setBeanToNodeMapper(() -> createToggle("fas-gauge-high", "Suppliers")).get());
        loader.addView(MFXLoaderBean.of("USERS", loadURL("fxml/people/users/Users.fxml")).setBeanToNodeMapper(() -> createToggle("fas-gauge-high", "Users")).get());
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
        loader.start();
    }
}
