package inc.nomard.spoty.core.views.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class FilterComboBox<T> extends ComboBox<T> {

    public FilterComboBox() {
        initialize();
    }

    private void initialize() {
        FilteredList<T> originalItems = new FilteredList<>(getItems(), p -> true);
        setEditable(true);

        TextField editor = getEditor();
        setCellFactory(new Callback<>() {
            @Override
            public ListCell<T> call(ListView<T> p) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(T item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.toString());
                        }
                    }
                };
            }
        });

        editor.textProperty().addListener((obs, oldText, newText) -> {
            if (!isShowing()) {
                show();
            }
            if (newText.isEmpty()) {
                setItems(originalItems);
            } else {
                ObservableList<T> filteredItems = FXCollections.observableArrayList();
                for (T item : originalItems) {
                    if (item.toString().toLowerCase().contains(newText.toLowerCase())) {
                        filteredItems.add(item);
                    }
                }
                setItems(filteredItems);
            }
        });

        showingProperty().addListener((obs, wasShowing, isShowing) -> {
            if (isShowing) {
                setItems(originalItems);
            }
        });
    }
}
