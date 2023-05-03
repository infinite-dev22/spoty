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
import org.infinite.spoty.views.people.customers.CustomersController;
import org.infinite.spoty.views.people.suppliers.SuppliersController;
import org.infinite.spoty.views.people.users.UsersController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotResourceLoader.loadURL;
import static org.infinite.spoty.Utils.createToggle;

public class PeopleController implements Initializable {
    private final Stage stage;
    @FXML
    public VBox peopleNavbar;

    @FXML
    public StackPane contentPane;

    public PeopleController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MFXLoader loader = new MFXLoader();
        loader.addView(MFXLoaderBean.of("CUSTOMERS", loadURL("fxml/people/customers/Customers.fxml")).setBeanToNodeMapper(() -> createToggle("fas-user-tie", "Customers")).setControllerFactory(c -> new CustomersController(stage)).setDefaultRoot(true).get());
        loader.addView(MFXLoaderBean.of("SUPPLIERS", loadURL("fxml/people/suppliers/Suppliers.fxml")).setBeanToNodeMapper(() -> createToggle("fas-truck", "Suppliers")).setControllerFactory(c -> new SuppliersController(stage)).get());
        loader.addView(MFXLoaderBean.of("USERS", loadURL("fxml/people/users/Users.fxml")).setBeanToNodeMapper(() -> createToggle("fas-user", "Users")).setControllerFactory(c -> new UsersController(stage)).get());
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
