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

package inc.nomard.spoty.core.viewModels;

import com.google.gson.*;
import com.google.gson.reflect.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.network_bridge.repositories.implementations.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.adapters.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import javafx.beans.property.*;
import javafx.collections.*;


public class BankViewModel {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class,
                    UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
            .create();
    private static final LongProperty id = new SimpleLongProperty(0);
    private static final StringProperty bankName = new SimpleStringProperty("");
    private static final StringProperty accountName = new SimpleStringProperty("");
    private static final StringProperty accountNumber = new SimpleStringProperty("");
    private static final StringProperty balance = new SimpleStringProperty("");
    private static final StringProperty logo = new SimpleStringProperty("");
    private static final ObjectProperty<Bank> bank = new SimpleObjectProperty<>();
    private static final ObjectProperty<Branch> branch = new SimpleObjectProperty<>();
    public static ObservableList<Bank> banksList = FXCollections.observableArrayList();
    private static final ListProperty<Bank> banks = new SimpleListProperty<>(banksList);
    public static ObservableList<Branch> branchesList = FXCollections.observableArrayList();
    private static final ListProperty<Branch> branches = new SimpleListProperty<>(branchesList);
    public static ObservableList<Bank> banksComboBoxList = FXCollections.observableArrayList();
    public static BanksRepositoryImpl banksRepository = new BanksRepositoryImpl();

    public static Long getId() {
        return id.get();
    }

    public static void setId(Long id) {
        BankViewModel.id.set(id);
    }

    public static LongProperty idProperty() {
        return id;
    }

    public static String getBankName() {
        return bankName.get();
    }

    public static void setBankName(String bankName) {
        BankViewModel.bankName.set(bankName);
    }

    public static StringProperty nameProperty() {
        return bankName;
    }

    public static String getAccountName() {
        return accountName.get();
    }

    public static void setAccountName(String accountName) {
        BankViewModel.accountName.set(accountName);
    }

    public static StringProperty emailProperty() {
        return accountName;
    }

    public static String getAccountNumber() {
        return accountNumber.get();
    }

    public static void setAccountNumber(String accountNumber) {
        BankViewModel.accountNumber.set(accountNumber);
    }

    public static StringProperty phoneProperty() {
        return accountNumber;
    }

    public static String getBalance() {
        return balance.get();
    }

    public static void setBalance(String balance) {
        BankViewModel.balance.set(balance);
    }

    public static StringProperty townProperty() {
        return balance;
    }

    public static String getLogo() {
        return logo.get();
    }

    public static void setLogo(String logo) {
        BankViewModel.logo.set(logo);
    }

    public static StringProperty cityProperty() {
        return logo;
    }

    public static ObservableList<Bank> getBanks() {
        return banks.get();
    }

    public static void setBanks(ObservableList<Bank> banks) {
        BankViewModel.banks.set(banks);
    }

    public static ListProperty<Bank> banksProperty() {
        return banks;
    }

    public static Branch getBranch() {
        return branch.get();
    }

    public static void setBranch(Branch branch) {
        BankViewModel.branch.set(branch);
    }

    public static void saveBank(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var bank =
                Bank.builder()
                        .bankName(getBankName())
                        .accountName(getAccountName())
                        .accountNumber(getAccountNumber())
                        .balance(getBalance())
                        .logo(getLogo())
                        .build();

        var task = banksRepository.post(bank);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> {
            onFailed.run();
            System.err.println("The task failed with the following exception:");
            task.getException().printStackTrace(System.err);
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void clearBankData() {
        setId(0L);
        setBankName("");
        setLogo("");
        setAccountNumber("");
        setAccountName("");
        setBalance("");
    }

    public static void getAllBanks(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var task = banksRepository.fetchAll();
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> {
                onFailed.run();
                System.err.println("The task failed with the following exception:");
                task.getException().printStackTrace(System.err);
            });
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<Bank>>() {
                }.getType();
                ArrayList<Bank> bankList = gson.fromJson(task.get().body(), listType);

                banksList.clear();
                banksList.addAll(bankList);

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, BankViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void getItem(
            Long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

        var task = banksRepository.fetch(findModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> {
                onFailed.run();
                System.err.println("The task failed with the following exception:");
                task.getException().printStackTrace(System.err);
            });
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                var bank = gson.fromJson(task.get().body(), Bank.class);

                setId(bank.getId());
                setBankName(bank.getBankName());
                setAccountName(bank.getAccountName());
                setAccountNumber(bank.getAccountNumber());
                setLogo(bank.getLogo());
                setBalance(bank.getBalance());

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, BankViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void searchItem(
            String search,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var searchModel = SearchModel.builder().search(search).build();
        var task = banksRepository.search(searchModel);
        if (Objects.nonNull(onActivity)) {
            task.setOnRunning(workerStateEvent -> onActivity.run());
        }
        if (Objects.nonNull(onFailed)) {
            task.setOnFailed(workerStateEvent -> {
                onFailed.run();
                System.err.println("The task failed with the following exception:");
                task.getException().printStackTrace(System.err);
            });
        }
        task.setOnSucceeded(workerStateEvent -> {
            try {
                Type listType = new TypeToken<ArrayList<Bank>>() {
                }.getType();
                ArrayList<Bank> bankList = gson.fromJson(task.get().body(), listType);

                banksList.clear();
                banksList.addAll(bankList);

                if (Objects.nonNull(onSuccess)) {
                    onSuccess.run();
                }
            } catch (InterruptedException | ExecutionException e) {
                SpotyLogger.writeToFile(e, BankViewModel.class);
            }
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void updateItem(
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var bank = Bank.builder()
                .id(getId())
                .bankName(getBankName())
                .accountName(getAccountName())
                .accountNumber(getAccountNumber())
                .balance(getBalance())
                .logo(getLogo())
                .build();

        var task = banksRepository.put(bank);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> {
            onFailed.run();
            System.err.println("The task failed with the following exception:");
            task.getException().printStackTrace(System.err);
        });
        SpotyThreader.spotyThreadPool(task);
    }

    public static void deleteItem(
            Long index,
            ParameterlessConsumer onActivity,
            ParameterlessConsumer onSuccess,
            ParameterlessConsumer onFailed) {
        var findModel = FindModel.builder().id(index).build();

        var task = banksRepository.delete(findModel);
        task.setOnRunning(workerStateEvent -> onActivity.run());
        task.setOnSucceeded(workerStateEvent -> onSuccess.run());
        task.setOnFailed(workerStateEvent -> {
            onFailed.run();
            System.err.println("The task failed with the following exception:");
            task.getException().printStackTrace(System.err);
        });
        SpotyThreader.spotyThreadPool(task);
    }
}
