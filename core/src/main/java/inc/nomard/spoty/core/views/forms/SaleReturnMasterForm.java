package inc.nomard.spoty.core.views.forms;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.viewModels.CustomerViewModel;
import inc.nomard.spoty.core.viewModels.returns.sales.SaleReturnDetailViewModel;
import inc.nomard.spoty.core.viewModels.returns.sales.SaleReturnMasterViewModel;
import inc.nomard.spoty.core.viewModels.sales.SaleDetailViewModel;
import inc.nomard.spoty.core.viewModels.sales.SaleMasterViewModel;
import inc.nomard.spoty.core.views.components.CustomButton;
import inc.nomard.spoty.core.views.components.validatables.ValidatableComboBox;
import inc.nomard.spoty.core.views.components.validatables.ValidatableTextArea;
import inc.nomard.spoty.core.views.util.FunctionalStringConverter;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.network_bridge.dtos.Customer;
import inc.nomard.spoty.network_bridge.dtos.sales.SaleDetail;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import lombok.extern.log4j.Log4j2;

import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Stream;

@Log4j2
public class SaleReturnMasterForm extends VBox {
    private final ModalPane modalPane;
    public CustomButton saveBtn;
    public Button cancelBtn, addBtn;
    private ValidatableComboBox<Customer> customer;
    private TableView<SaleDetail> tableView;
    private ValidatableTextArea note;

    public SaleReturnMasterForm(ModalPane modalPane) {
        this.modalPane = modalPane;
        init();
        initializeComponentProperties();
    }

    public void initializeComponentProperties() {
        bindProperties();
        configureCustomerComboBox();
    }

    private void init() {
        this.getStyleClass().add("card-flat");
        this.setPadding(new Insets(10d));
        this.setSpacing(10d);
        VBox.setVgrow(this, Priority.ALWAYS);
        this.getChildren().addAll(buildTitle(),
                buildCustomer(),
                buildAddButton(),
                buildTable(),
                buildNote(),
                createButtonBox());
    }

    private Separator buildSeparator() {
        var separator = new Separator();
        separator.setPrefWidth(200.0);
        HBox.setHgrow(separator, Priority.ALWAYS);
        return separator;
    }

    private VBox buildTitle() {
        var title = new Text("Create");
        title.getStyleClass().add(Styles.TITLE_3);
        var subTitle = new Text("Sale return");
        subTitle.getStyleClass().add(Styles.TITLE_4);
        return buildFieldHolder(title, subTitle, buildSeparator());
    }

    private Button buildAddButton() {
        addBtn = new Button("Add");
        addBtn.setDefaultButton(true);
        addBtn.setDisable(true);
        addBtn.setPrefWidth(10000d);
        HBox.setHgrow(addBtn, Priority.ALWAYS);
        return addBtn;
    }

    private TableView<SaleDetail> buildTable() {
        tableView = new TableView<>();
        HBox.setHgrow(tableView, Priority.ALWAYS);
        setupTable();
        return tableView;
    }

    private VBox buildFieldHolder(Node... nodes) {
        VBox vbox = new VBox();
        vbox.setSpacing(5d);
        vbox.setPadding(new Insets(5d, 0d, 0d, 0d));
        vbox.getChildren().addAll(nodes);
        HBox.setHgrow(vbox, Priority.ALWAYS);
        return vbox;
    }

    private VBox buildCustomer() {
        var label = new Label("Customer");
        customer = new ValidatableComboBox<>();
        customer.setPrefWidth(10000d);
        customer.setDisable(true);
        return buildFieldHolder(label, customer);
    }

    private VBox buildNote() {
        var label = new Label("Note");
        note = new ValidatableTextArea();
        note.setMinHeight(100d);
        note.setWrapText(true);
        return buildFieldHolder(label, note);
    }

    private CustomButton buildSaveButton() {
        saveBtn = new CustomButton("Save");
        saveBtn.getStyleClass().add(Styles.ACCENT);
        saveBtn.setOnAction(event -> {
            processProducts();
            if (!tableView.isDisabled() && SaleReturnDetailViewModel.getSaleReturnDetails().isEmpty()) {
                errorMessage("No products selected");
            }

            if (isValidForm()) {
                saveBtn.startLoading();
                SaleReturnMasterViewModel.saveSaleReturnMaster(this::onSuccess, SpotyUtils::successMessage, this::errorMessage);
            }
        });
        return saveBtn;
    }

    private Button buildCancelButton() {
        cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add(Styles.BUTTON_OUTLINED);
        cancelBtn.setOnAction(event -> {
            this.dispose();
        });
        return cancelBtn;
    }

    private HBox createButtonBox() {
        var buttonBox = new HBox(20.0);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10.0));

        buttonBox.getChildren().addAll(buildSaveButton(), buildCancelButton());
        HBox.setHgrow(buttonBox, Priority.ALWAYS);
        return buttonBox;
    }

    private void bindProperties() {
        customer.valueProperty().bindBidirectional(SaleMasterViewModel.customerProperty());
        note.textProperty().bindBidirectional(SaleMasterViewModel.notesProperty());
    }

    private void configureCustomerComboBox() {
        StringConverter<Customer> customerConverter = FunctionalStringConverter.to(customer -> (customer == null) ? "" : customer.getName());

        customer.setConverter(customerConverter);

        if (CustomerViewModel.getCustomers().isEmpty()) {
            CustomerViewModel.getCustomers().addListener((ListChangeListener<Customer>) c -> customer.setItems(CustomerViewModel.getCustomers()));
        } else {
            customer.itemsProperty().bindBidirectional(CustomerViewModel.customersProperty());
        }
    }

    private boolean isValidForm() {
        return !tableView.isDisabled() && !SaleReturnDetailViewModel.getSaleReturnDetails().isEmpty();
    }

    private void processProducts() {
        SaleDetailViewModel.getSaleDetails().forEach(saleDetail -> {
            if (saleDetail.getSelected().get()) {
                SaleReturnDetailViewModel.getSaleReturnDetails().add(saleDetail);
            }
        });
    }

    private void setupTable() {
        setupTableColumns();
        styleTable();
        bindTableItems();
    }

    private void setupTableColumns() {
        var selectAll = new CheckBox();

        var select = new TableColumn<SaleDetail, Boolean>();
        select.setGraphic(selectAll);
        select.setSortable(false);
        select.setCellValueFactory(c -> c.getValue().getSelected());
        select.setCellFactory(CheckBoxTableCell.forTableColumn(select));
        select.setEditable(true);
        selectAll.setOnAction(evt -> {
            tableView.getItems().forEach(
                    item -> item.getSelected().set(selectAll.isSelected())
            );
            evt.consume();
        });
        var product = new TableColumn<SaleDetail, SaleDetail>("Product");
        var quantity = new TableColumn<SaleDetail, SaleDetail>("Quantity");

        product.prefWidthProperty().bind(tableView.widthProperty().multiply(.7));
        quantity.prefWidthProperty().bind(tableView.widthProperty().multiply(.3));

        product.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        product.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(SaleDetail item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || Objects.isNull(item) ? null : item.getProductName());
            }
        });
        quantity.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        quantity.setCellFactory(tableColumn -> new TableCell<>() {
            @Override
            public void updateItem(SaleDetail item, boolean empty) {
                super.updateItem(item, empty);
                this.setAlignment(Pos.CENTER);
                if (!empty || !Objects.isNull(item)) {
                    Spinner<Integer> quantitySpinner = new Spinner<>(0, 999999999, 1);
                    quantitySpinner.getStyleClass().addAll(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
                    quantitySpinner.getValueFactory().setValue(item.getQuantity());
                    quantitySpinner.valueProperty().addListener(
                            (obs, oldValue, newValue) -> {
                                SaleDetailViewModel.getSaleDetail(item);
                                SaleDetailViewModel.setQuantity(Long.valueOf(newValue));
                            }
                    );
                    setGraphic(quantitySpinner);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText(null);
                }
            }
        });

        var columnList = new LinkedList<>(Stream.of(select, product, quantity).toList());
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tableView.getColumns().addAll(columnList);
        tableView.setEditable(true);
    }

    private void styleTable() {
        tableView.setPrefSize(10000, 10000);
        tableView.setRowFactory(t -> {
            TableRow<SaleDetail> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
            });
            return row;
        });
    }

    private void bindTableItems() {
        tableView.setItems(SaleDetailViewModel.getSaleDetails());
    }

    private void onSuccess() {
        this.dispose();
        SaleMasterViewModel.getAllSaleMasters(null, null, null, null);
    }

    private void errorMessage(String message) {
        SpotyUtils.errorMessage(message);
        saveBtn.stopLoading();
    }

    public void dispose() {
        modalPane.hide(true);
        modalPane.setPersistent(false);
        SaleMasterViewModel.resetProperties();
        customer = null;
        tableView = null;
        note = null;
        saveBtn = null;
        cancelBtn = null;
        addBtn = null;
    }
}
