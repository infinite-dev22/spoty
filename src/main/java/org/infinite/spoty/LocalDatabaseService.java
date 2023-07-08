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

package org.infinite.spoty;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.infinite.spoty.viewModels.*;

public class LocalDatabaseService extends Service<Void> {
  @Override
  protected Task<Void> createTask() {
    return new Task<>() {
      @Override
      protected Void call() {
        AdjustmentMasterViewModel.getAdjustmentMasters();
        BranchViewModel.getBranches();
        BrandViewModel.getItems();
        CurrencyViewModel.getCurrencies();
        CustomerViewModel.getCustomers();
        ExpenseCategoryViewModel.getCategories();
        ExpenseViewModel.getExpenses();
        ProductCategoryViewModel.getItems();
        ProductMasterViewModel.getProductMasters();
        PurchaseMasterViewModel.getPurchaseMasters();
        PurchaseReturnMasterViewModel.getPurchaseReturnMasters();
        QuotationMasterViewModel.getQuotationMasters();
        RequisitionMasterViewModel.getRequisitionMasters();
        SaleMasterViewModel.getSaleMasters();
        SaleReturnMasterViewModel.getSaleReturnMasters();
        StockInMasterViewModel.getStockInMasters();
        SupplierViewModel.getSuppliers();
        TransferMasterViewModel.getTransferMasters();
        UOMViewModel.getItems();
        UserViewModel.getUsers();
        return null;
      }
    };
  }
}
