package inc.nomard.spoty.core;

import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.adjustments.*;
import inc.nomard.spoty.core.viewModels.hrm.employee.*;
import inc.nomard.spoty.core.viewModels.purchases.*;
import inc.nomard.spoty.core.viewModels.quotations.*;
import inc.nomard.spoty.core.viewModels.requisitions.*;
import inc.nomard.spoty.core.viewModels.returns.purchases.*;
import inc.nomard.spoty.core.viewModels.returns.sales.*;
import inc.nomard.spoty.core.viewModels.sales.*;
import inc.nomard.spoty.core.viewModels.stock_ins.*;
import inc.nomard.spoty.core.viewModels.transfers.*;
import javafx.concurrent.*;

public class OnlineQueryWorker {
    public static ScheduledService<Void> fetchDataTask() {
        return new ScheduledService<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() {
                        AdjustmentMasterViewModel.getAllAdjustmentMasters(null, null);
                        BranchViewModel.getAllBranches(null, null);
                        BrandViewModel.getAllBrands(null, null);
                        BankViewModel.getAllBanks(null, null);
                        CurrencyViewModel.getAllCurrencies(null, null);
                        CustomerViewModel.getAllCustomers(null, null);
                        DesignationViewModel.getAllDesignations(null, null);
                        DiscountViewModel.getDiscounts(null, null);
                        EmploymentStatusViewModel.getAllEmploymentStatuses(null, null);
                        ExpenseCategoryViewModel.getAllCategories(null, null);
                        ExpensesViewModel.getAllExpenses(null, null);
                        PermissionsViewModel.getAllPermissions(null, null);
                        ProductCategoryViewModel.getAllProductCategories(null, null);
                        ProductViewModel.getAllProducts(null, null);
                        PurchaseMasterViewModel.getAllPurchaseMasters(null, null);
                        PurchaseReturnMasterViewModel.getPurchaseReturnMasters(null, null);
                        QuotationMasterViewModel.getAllQuotationMasters(null, null);
                        RequisitionMasterViewModel.getAllRequisitionMasters(null, null);
                        RoleViewModel.getAllRoles(null, null);
                        SaleMasterViewModel.getAllSaleMasters(null, null);
                        SaleReturnMasterViewModel.getSaleReturnMasters(null, null);
                        StockInMasterViewModel.getAllStockInMasters(null, null);
                        SupplierViewModel.getAllSuppliers(null, null);
                        TaxViewModel.getTaxes(null, null);
                        TransferMasterViewModel.getAllTransferMasters(null, null);
                        UOMViewModel.getAllUOMs(null, null);
                        UserViewModel.getAllUsers(null, null);
                        return null;
                    }
                };
            }
        };
    }
}