package inc.nomard.spoty.core.viewModels.returns.sales;

import com.google.gson.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.returns.sale_returns.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.utils.adapters.*;
import inc.nomard.spoty.utils.functional_paradigm.*;
import java.time.*;
import java.util.*;
import javafx.beans.property.*;
import javafx.collections.*;
import lombok.*;
import lombok.extern.java.*;

@Log
public class SaleReturnMasterViewModel {
    @Getter
    public static final ObservableList<SaleReturnMaster> saleReturnMasterList =
            FXCollections.observableArrayList();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    new UnixEpochDateTypeAdapter())
            .create();
    private static final ListProperty<SaleReturnMaster> saleReturns = new SimpleListProperty<>(saleReturnMasterList);
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final ObjectProperty<LocalDateTime> date = new SimpleObjectProperty<>();
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>(null);
    private static final StringProperty totalCost = new SimpleStringProperty("");
    private static final StringProperty status = new SimpleStringProperty("");
    private static final StringProperty note = new SimpleStringProperty("");
//    private static SaleReturnsRepositoryImpl saleReturnsRepository = new SaleReturnsRepositoryImpl();

    public static long getId() {
        return id.get();
    }

    public static void setId(long id) {
        SaleReturnMasterViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static LocalDateTime getDate() {
        return date.get();
    }

    public static void setDate(LocalDateTime date) {
        SaleReturnMasterViewModel.date.set(date);
    }

    public static ObjectProperty<LocalDateTime> dateProperty() {
        return date;
    }

    public static Branch getBranch() {
        return branch.get();
    }

    public static void setBranch(Branch branch) {
        SaleReturnMasterViewModel.branch.set(branch);
    }

    public static ObjectProperty<Branch> branchProperty() {
        return branch;
    }

    public static String getNote() {
        return note.get();
    }

    public static void setNote(String note) {
        SaleReturnMasterViewModel.note.set(note);
    }

    public static StringProperty noteProperty() {
        return note;
    }

    public static String getStatus() {
        return status.get();
    }

    public static void setStatus(String status) {
        SaleReturnMasterViewModel.status.set(status);
    }

    public static StringProperty statusProperty() {
        return status;
    }

    public static double getTotalCost() {
        return Double.parseDouble(!totalCost.get().isEmpty() ? totalCost.get() : "0");
    }

    public static void setTotalCost(String totalCost) {
        SaleReturnMasterViewModel.totalCost.set(totalCost);
    }

    public static StringProperty totalCostProperty() {
        return totalCost;
    }

    public static ObservableList<SaleReturnMaster> getSaleReturns() {
        return saleReturns.get();
    }

    public static void setSaleReturns(ObservableList<SaleReturnMaster> saleReturns) {
        SaleReturnMasterViewModel.saleReturns.set(saleReturns);
    }

    public static ListProperty<SaleReturnMaster> saleReturnsProperty() {
        return saleReturns;
    }

    public static void resetProperties() {
        setId(0);
        setDate(null);
        setBranch(null);
        setNote("");
        setStatus("");
        setTotalCost("");
    }

    public static void saveSaleReturnMaster(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                            SpotyGotFunctional.MessageConsumer successMessage,
                                            SpotyGotFunctional.MessageConsumer errorMessage) {
//        var saleReturnsMaster = SaleReturnMaster.builder()
//                .customer(getCustomer())
//                .total(getTotal())
//                .amountPaid(getPaid())
//                .SaleReturnsStatus(getSaleReturnStatus())
//                .paymentStatus(getPayStatus())
//                .notes(getNote())
//                .createdAt(getCreatedAt())
//                .build();
//
//        if (!SaleReturnDetailViewModel.SaleReturnsDetailsList.isEmpty()) {
//            SaleReturnDetailViewModel.SaleReturnsDetailsList.forEach(
//                    SaleReturnsDetail -> SaleReturnsDetail.setSaleReturn(SaleReturnsMaster));
//
//            SaleReturnsMaster.setSaleReturnDetails(SaleReturnDetailViewModel.SaleReturnsDetailsList);
//        }
//
//        var task = SaleReturnsRepository.post(SaleReturnsMaster);
//        task.setOnRunning(workerStateEvent -> onActivity.run());
//        task.setOnSucceeded(workerStateEvent -> {
//            SaleReturnDetailViewModel.saveSaleReturnDetails(onActivity, null, onFailed);
//
//        if (Objects.nonNull(onSuccess)) {
//            Platform.runLater(onSuccess::run);
//        }
//        });
//        task.setOnFailed(workerStateEvent -> onFailed.run());
//        SpotyThreader.spotyThreadPool(task);
    }

    public static void getSaleReturnMasters(SpotyGotFunctional.ParameterlessConsumer onSuccess,
                                            SpotyGotFunctional.MessageConsumer errorMessage) {
//        Dao<SaleReturnMaster, Long> saleReturnMasterDao =
//                DaoManager.createDao(connectionSource, SaleReturnMaster.class);
//
//        Platform.runLater(
//                () -> {
//                    saleReturnMasterList.clear();
//
//                    try {
//                        saleReturnMasterList.addAll(saleReturnMasterDao.queryForAll());
//                    } catch (Exception e) {
//                        SpotyLogger.writeToFile(e, SaleReturnMasterViewModel.class);
//                    }
//                });
    }

    public static void getItem(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer errorMessage) {
//        var findModel = FindModel.builder().id(index).build();
//        var task = SaleReturnsRepository.fetch(findModel);
//
//        if (Objects.nonNull(onActivity)) {
//            task.setOnRunning(workerStateEvent -> onActivity.run());
//        }
//        if (Objects.nonNull(onFailed)) {
//            task.setOnFailed(workerStateEvent -> onFailed.run());
//        }
//        task.setOnSucceeded(workerStateEvent -> {
//            SaleReturnMaster SaleReturnsMaster = new SaleReturnMaster();
//            try {
//                SaleReturnsMaster = gson.fromJson(task.get().body(), SaleReturnMaster.class);
//            } catch (InterruptedException | ExecutionException e) {
//                SpotyLogger.writeToFile(e, SaleReturnMasterViewModel.class);
//            }
//
//            setId(SaleReturnsMaster.getId());
//            setCreatedAt(SaleReturnsMaster.getLocaleDate());
//            setCustomer(SaleReturnsMaster.getCustomer());
//            setNote(SaleReturnsMaster.getNotes());
//            setSaleReturnStatus(SaleReturnsMaster.getSaleReturnStatus());
//            setPayStatus(SaleReturnsMaster.getPaymentStatus());
//            SaleReturnDetailViewModel.SaleReturnsDetailsList.clear();
//            SaleReturnDetailViewModel.SaleReturnsDetailsList.addAll(SaleReturnsMaster.getSaleReturnDetails());
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
//        var task = SaleReturnsRepository.search(searchModel);
//        if (Objects.nonNull(onActivity)) {
//            task.setOnRunning(workerStateEvent -> onActivity.run());
//        }
//        if (Objects.nonNull(onFailed)) {
//            task.setOnFailed(workerStateEvent -> onFailed.run());
//        }
//        task.setOnSucceeded(workerStateEvent -> {
//            Type listType = new TypeToken<ArrayList<SaleReturnMaster>>() {
//            }.getType();
//            ArrayList<SaleReturnMaster> SaleReturnsMasterList = new ArrayList<>();
//            try {
//                SaleReturnsMasterList = gson.fromJson(
//                        task.get().body(), listType);
//            } catch (InterruptedException | ExecutionException e) {
//                SpotyLogger.writeToFile(e, SaleReturnMasterViewModel.class);
//            }
//
//            SaleReturnsMastersList.clear();
//            SaleReturnsMastersList.addAll(SaleReturnsMasterList);
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
//        var saleReturnsMaster = SaleReturnMaster.builder()
//                .id(getId())
//                .customer(getCustomer())
//                .total(getTotal())
//                .amountPaid(getPaid())
//                .SaleReturnsStatus(getSaleReturnStatus())
//                .paymentStatus(getPayStatus())
//                .notes(getNote())
//                .createdAt(getCreatedAt())
//                .build();
//
//        if (!PENDING_DELETES.isEmpty()) {
//            SaleReturnDetailViewModel.deleteSaleReturnDetails(PENDING_DELETES, onActivity, null, onFailed);
//        }
//
//        if (!SaleReturnDetailViewModel.getSaleReturnDetailsList().isEmpty()) {
//            SaleReturnDetailViewModel.getSaleReturnDetailsList()
//                    .forEach(SaleReturnsDetail -> SaleReturnsDetail.setSaleReturn(SaleReturnsMaster));
//
//            SaleReturnsMaster.setSaleReturnDetails(SaleReturnDetailViewModel.getSaleReturnDetailsList());
//        }
//
//        var task = SaleReturnsRepository.put(SaleReturnsMaster);
//        task.setOnRunning(workerStateEvent -> onActivity.run());
//        task.setOnSucceeded(workerStateEvent -> SaleReturnDetailViewModel.updateSaleReturnDetails(onActivity, onSuccess, onFailed));
//        task.setOnFailed(workerStateEvent -> onFailed.run());
//        SpotyThreader.spotyThreadPool(task);
    }

    public static void deleteItem(
            Long index, SpotyGotFunctional.ParameterlessConsumer onSuccess,
            SpotyGotFunctional.MessageConsumer successMessage,
            SpotyGotFunctional.MessageConsumer errorMessage) {
        var findModel = FindModel.builder().id(index).build();

//        var task = SaleReturnsRepository.delete(findModel);
//        task.setOnRunning(workerStateEvent -> onActivity.run());
//        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
//        task.setOnFailed(workerStateEvent -> onFailed.run());
//        SpotyThreader.spotyThreadPool(task);
    }
}
