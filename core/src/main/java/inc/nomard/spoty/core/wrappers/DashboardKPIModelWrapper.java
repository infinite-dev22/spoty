package inc.nomard.spoty.core.wrappers;

import inc.nomard.spoty.network_bridge.dtos.dashboard.DashboardKPIModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DashboardKPIModelWrapper {

    private final StringProperty name = new SimpleStringProperty();
    private final ObjectProperty<Number> value = new SimpleObjectProperty<>();

    public DashboardKPIModelWrapper(DashboardKPIModel model) {
        this.name.set(model.getName());
        this.value.set(model.getValue());
    }

    public StringProperty nameProperty() {
        return name;
    }

    public ObjectProperty<Number> valueProperty() {
        return value;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public Number getValue() {
        return value.get();
    }

    public void setValue(Number value) {
        this.value.set(value);
    }
}
