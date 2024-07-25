package inc.nomard.spoty.core.viewModels.returns.purchases;

import com.google.gson.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.returns.purchase_returns.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.utils.adapters.*;
import inc.nomard.spoty.utils.functional_paradigm.*;
import java.time.*;
import java.util.*;
import javafx.beans.property.*;
import javafx.collections.*;
import lombok.extern.java.*;

@Log
public class PurchaseReturnMasterViewModel {
    public static final ObservableList<PurchaseReturnMaster> purchaseReturnMasterList =
            FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    new UnixEpochDateTypeAdapter())
            .registerTypeAdapter(LocalDate.class,
                    new LocalDateTypeAdapter())
            .registerTypeAdapter(LocalTime.class,
                    new LocalTimeTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class,
                    new LocalDateTimeTypeAdapter())
            .create();
    private static final ListProperty<PurchaseReturnMaster> purchaseReturns =
            new SimpleListProperty<>(purchaseReturnMasterList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<LocalDateTime> date = new SimpleObjectProperty<>();
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
    private static final StringProperty totalCost = new SimpleStringProperty("");
    private static final StringProperty status = new SimpleStringProperty("");
    private static final StringProperty note = new SimpleStringProperty("");
//    private static PurchaseReturnsRepositoryImpl purchaseReturnsRepository = new PurchaseReturnsRepositoryImpl();

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        PurchaseReturnMasterViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static LocalDateTime getDate() {
        return date.get();
    }

    public static void setDate(LocalDateTime date) {
        PurchaseReturnMasterViewModel.date.set(date);
    }

    public static ObjectProperty<LocalDateTime> dateProperty() {
        return date;
    }

    public static Branch getBranch() {
        return branch.get();
    }

    public static void setBranch(Branch branch) {
        PurchaseReturnMasterViewModel.branch.set(branch);
    }

    public static ObjectProperty<Branch> branchProperty() {
        return branch;
    }

    public static String getNote() {
        return note.get();
    }

    public static void setNote(String note) {
        PurchaseReturnMasterViewModel.note.set(note);
    }

    public static StringProperty noteProperty() {
        return note;
    }

    public static String getStatus() {
        return status.get();
    }

    public static void setStatus(String status) {
        PurchaseReturnMasterViewModel.status.set(status);
    }

    public static StringProperty statusProperty() {
        return status;
    }

    public static double getTotalCost() {
        return Double.parseDouble(!totalCost.get().isEmpty() ? totalCost.get() : "0");
    }

    public static void setTotalCost(String totalCost) {
        PurchaseReturnMasterViewModel.totalCost.set(totalCost);
    }

    public static StringProperty totalCostProperty() {
        return totalCost;
    }

    public static ObservableList<PurchaseReturnMaster> getPurchaseReturns() {
        return purchaseReturns.get();
    }

    public static void setPurchaseReturns(ObservableList<PurchaseReturnMaster> purchaseReturns) {
        PurchaseReturnMasterViewModel.purchaseReturns.set(purchaseReturns);
    }

    public static ListProperty<PurchaseReturnMaster> purchaseReturnsProperty() {
        return purchaseReturns;
    }

    public static void resetProperties() {
        setId(0);
        setDate(null);
        setBranch(null);
        setNote("");
        setStatus("");
        setTotalCost("");
    }

    public static void savePurchaseReturnMaster(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                                SpotyGotFunctional.MessageConsumer successMessage,
                                                SpotyGotFunctional.MessageConsumer errorMessage) {
//        var PurchaseReturnsMaster = PurchaseReturnMaster.builder()
//                .customer(getCustomer())
//                .total(getTotal())
//                .amountPaid(getPaid())
//                .PurchaseReturnsStatus(getPurchaseReturnStatus())
//                .paymentStatus(getPayStatus())
//                .notes(getNote())
//                .createdAt(getCreatedAt())
//                .build();
//
//        if (!PurchaseReturnDetailViewModel.PurchaseReturnsDetailsList.isEmpty()) {
//            PurchaseReturnDetailViewModel.PurchaseReturnsDetailsList.forEach(
//                    PurchaseReturnsDetail -> PurchaseReturnsDetail.setPurchaseReturn(PurchaseReturnsMaster));
//
//            PurchaseReturnsMaster.setPurchaseReturnDetails(PurchaseReturnDetailViewModel.PurchaseReturnsDetailsList);
//        }
//
//        var task = PurchaseReturnsRepository.post(PurchaseReturnsMaster);
//        task.setOnRunning(workerStateEvent -> onActivity.run());
//        task.setOnSucceeded(workerStateEvent -> {
//            PurchaseReturnDetailViewModel.savePurchaseReturnDetails(onActivity, null, onFailed);
//
//        if (Objects.nonNull(onSuccess)) {
//            Platform.runLater(onSuccess::run);
//        }
//        });
//        task.setOnFailed(workerStateEvent -> onFailed.run());
//        SpotyThreader.spotyThreadPool(task);
    }

    public static void getPurchaseReturnMasters(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                                SpotyGotFunctional.MessageConsumer errorMessage) {
//        Dao<PurchaseReturnMaster, Long> purchaseReturnMasterDao =
//                DaoManager.createDao(connectionSource, PurchaseReturnMaster.class);
//
//        Platform.runLater(
//                () -> {
//                    purchaseReturnMasterList.clear();
//
//                    try {
//                        purchaseReturnMasterList.addAll(purchaseReturnMasterDao.queryForAll());
//                    } catch (Exception e) {
//                        SpotyLogger.writeToFile(e, PurchaseReturnMasterViewModel.class);
//                    }
//                });
    }

    public static ObservableList<PurchaseReturnMaster> getPurchaseReturnMasterList() {
        return purchaseReturnMasterList;
    }

    public static void getItem(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer errorMessage) {
//        var findModel = FindModel.builder().id(index).build();
//        var task = PurchaseReturnsRepository.fetch(findModel);
//
//        if (Objects.nonNull(onActivity)) {
//            task.setOnRunning(workerStateEvent -> onActivity.run());
//        }
//        if (Objects.nonNull(onFailed)) {
//            task.setOnFailed(workerStateEvent -> onFailed.run());
//        }
//        task.setOnSucceeded(workerStateEvent -> {
//            PurchaseReturnMaster PurchaseReturnsMaster = new PurchaseReturnMaster();
//            try {
//                PurchaseReturnsMaster = gson.fromJson(task.get().body(), PurchaseReturnMaster.class);
//            } catch (InterruptedException | ExecutionException e) {
//                SpotyLogger.writeToFile(e, PurchaseReturnMasterViewModel.class);
//            }
//
//            setId(PurchaseReturnsMaster.getId());
//            setCreatedAt(PurchaseReturnsMaster.getLocaleDate());
//            setCustomer(PurchaseReturnsMaster.getCustomer());
//            setNote(PurchaseReturnsMaster.getNotes());
//            setPurchaseReturnStatus(PurchaseReturnsMaster.getPurchaseReturnStatus());
//            setPayStatus(PurchaseReturnsMaster.getPaymentStatus());
//            PurchaseReturnDetailViewModel.PurchaseReturnsDetailsList.clear();
//            PurchaseReturnDetailViewModel.PurchaseReturnsDetailsList.addAll(PurchaseReturnsMaster.getPurchaseReturnDetails());
//
//            if (Objects.nonNull(onSuccess)) {
//
//        if (Objects.nonNull(onSuccess)) {
//            Platform.runLater(onSuccess::run);
//        }
//            }
//        });
//        SpotyThreader.spotyThreadPool(task);
    }

    public static void searchItem(
            String search, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer errorMessage) {
//        var searchModel = SearchModel.builder().search(search).build();
//        var task = PurchaseReturnsRepository.search(searchModel);
//        if (Objects.nonNull(onActivity)) {
//            task.setOnRunning(workerStateEvent -> onActivity.run());
//        }
//        if (Objects.nonNull(onFailed)) {
//            task.setOnFailed(workerStateEvent -> onFailed.run());
//        }
//        task.setOnSucceeded(workerStateEvent -> {
//            Type listType = new TypeToken<ArrayList<PurchaseReturnMaster>>() {
//            }.getType();
//            ArrayList<PurchaseReturnMaster> PurchaseReturnsMasterList = new ArrayList<>();
//            try {
//                PurchaseReturnsMasterList = gson.fromJson(
//                        task.get().body(), listType);
//            } catch (InterruptedException | ExecutionException e) {
//                SpotyLogger.writeToFile(e, PurchaseReturnMasterViewModel.class);
//            }
//
//            PurchaseReturnsMastersList.clear();
//            PurchaseReturnsMastersList.addAll(PurchaseReturnsMasterList);
//
//            if (Objects.nonNull(onSuccess)) {
//
//        if (Objects.nonNull(onSuccess)) {
//            Platform.runLater(onSuccess::run);
//        }
//            }
//        });
//        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateItem(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                  SpotyGotFunctional.MessageConsumer successMessage,
                                  SpotyGotFunctional.MessageConsumer errorMessage) {
//        var PurchaseReturnsMaster = PurchaseReturnMaster.builder()
//                .id(getId())
//                .customer(getCustomer())
//                .total(getTotal())
//                .amountPaid(getPaid())
//                .PurchaseReturnsStatus(getPurchaseReturnStatus())
//                .paymentStatus(getPayStatus())
//                .notes(getNote())
//                .createdAt(getCreatedAt())
//                .build();
//
//        if (!PENDING_DELETES.isEmpty()) {
//            PurchaseReturnDetailViewModel.deletePurchaseReturnDetails(PENDING_DELETES, onActivity, null, onFailed);
//        }
//
//        if (!PurchaseReturnDetailViewModel.getPurchaseReturnDetailsList().isEmpty()) {
//            PurchaseReturnDetailViewModel.getPurchaseReturnDetailsList()
//                    .forEach(PurchaseReturnsDetail -> PurchaseReturnsDetail.setPurchaseReturn(PurchaseReturnsMaster));
//
//            PurchaseReturnsMaster.setPurchaseReturnDetails(PurchaseReturnDetailViewModel.getPurchaseReturnDetailsList());
//        }
//
//        var task = PurchaseReturnsRepository.put(PurchaseReturnsMaster);
//        task.setOnRunning(workerStateEvent -> onActivity.run());
//        task.setOnSucceeded(workerStateEvent -> PurchaseReturnDetailViewModel.updatePurchaseReturnDetails(onActivity, onSuccess, onFailed));
//        task.setOnFailed(workerStateEvent -> onFailed.run());
//        SpotyThreader.spotyThreadPool(task);
    }

    public static void deleteItem(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer successMessage,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();

//        var task = PurchaseReturnsRepository.delete(findModel);
//        task.setOnRunning(workerStateEvent -> onActivity.run());
//        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
//        task.setOnFailed(workerStateEvent -> onFailed.run());
//        SpotyThreader.spotyThreadPool(task);
    }
}
