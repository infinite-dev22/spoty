package inc.nomard.spoty.network_bridge.end_points;

import lombok.extern.slf4j.*;

@Slf4j
public class EndPoints {
    public static final String appDNS = "http://localhost:8080";
    public static final String apiUrl = appDNS + "/api/v1";

    public static final class Adjustments {
        public static final String adjustmentsUrl = apiUrl + "/adjustments";
        // Masters
        public static final String allAdjustmentMasters = adjustmentsUrl + "/masters";
        public static final String adjustmentMasterById = adjustmentsUrl + "/master";
        public static final String searchAdjustmentMasters = adjustmentsUrl + "/masters/search";
        public static final String addAdjustmentMaster = adjustmentsUrl + "/master/add";
        public static final String updateAdjustmentMaster = adjustmentsUrl + "/master/update";
        public static final String deleteAdjustmentMaster = adjustmentsUrl + "/master/delete";
        public static final String deleteAdjustmentMasters = adjustmentsUrl + "/masters/delete";
        // Details
        public static final String allAdjustmentDetails = adjustmentsUrl + "/details";
        public static final String adjustmentDetailById = adjustmentsUrl + "/detail";
        public static final String searchAdjustmentDetails = adjustmentsUrl + "/details/search";
        public static final String addAdjustmentDetail = adjustmentsUrl + "/detail/add";
        public static final String updateAdjustmentDetail = adjustmentsUrl + "/detail/update";
        public static final String deleteAdjustmentDetail = adjustmentsUrl + "/detail/delete";
        public static final String deleteAdjustmentDetails = adjustmentsUrl + "/details/delete";
    }

    public static final class Attendances {
        public static final String attendancesUrl = apiUrl + "/attendances";
        public static final String allAttendances = attendancesUrl + "/all";
        public static final String attendanceById = attendancesUrl + "/single";
        public static final String searchAttendances = attendancesUrl + "/search";
        public static final String addAttendance = attendancesUrl + "/add";
        public static final String updateAttendance = attendancesUrl + "/update";
        public static final String deleteAttendance = attendancesUrl + "/single/delete";
        public static final String deleteAttendances = attendancesUrl + "/multiple/delete";
    }

    public static final class Auth {
        public static final String authUrl = apiUrl + "/auth";
        public static final String login = authUrl + "/login";
        public static final String signup = authUrl + "/register";
    }

    public static final class Banks {
        public static final String banksUrl = apiUrl + "/banks";
        public static final String allBanks = banksUrl + "/all";
        public static final String bankById = banksUrl + "/single";
        public static final String searchBanks = banksUrl + "/search";
        public static final String addBank = banksUrl + "/add";
        public static final String updateBank = banksUrl + "/update";
        public static final String deleteBank = banksUrl + "/single/delete";
        public static final String deleteBanks = banksUrl + "/multiple/delete";
    }

    public static final class Branches {
        public static final String branchesUrl = apiUrl + "/branches";
        public static final String allBranches = branchesUrl + "/all";
        public static final String branchById = branchesUrl + "/single";
        public static final String searchBranches = branchesUrl + "/search";
        public static final String addBranch = branchesUrl + "/add";
        public static final String updateBranch = branchesUrl + "/update";
        public static final String deleteBranch = branchesUrl + "/single/delete";
        public static final String deleteBranches = branchesUrl + "/multiple/delete";
    }

    public static final class Brands {
        public static final String brandsUrl = apiUrl + "/brands";
        public static final String allBrands = brandsUrl + "/all";
        public static final String brandById = brandsUrl + "/single";
        public static final String searchBrands = brandsUrl + "/search";
        public static final String addBrand = brandsUrl + "/add";
        public static final String updateBrand = brandsUrl + "/update";
        public static final String deleteBrand = brandsUrl + "/single/delete";
        public static final String deleteBrands = brandsUrl + "/multiple/delete";
    }

    public static final class Currencies {
        public static final String currenciesUrl = apiUrl + "/currencies";
        public static final String allCurrencies = currenciesUrl + "/all";
        public static final String currencyById = currenciesUrl + "/single";
        public static final String searchCurrencies = currenciesUrl + "/search";
        public static final String addCurrency = currenciesUrl + "/add";
        public static final String updateCurrency = currenciesUrl + "/update";
        public static final String deleteCurrency = currenciesUrl + "/single/delete";
        public static final String deleteCurrencies = currenciesUrl + "/multiple/delete";
    }

    public static final class Customers {
        public static final String customersUrl = apiUrl + "/customers";
        public static final String allCustomers = customersUrl + "/all";
        public static final String customerById = customersUrl + "/single";
        public static final String searchCustomers = customersUrl + "/search";
        public static final String addCustomer = customersUrl + "/add";
        public static final String updateCustomer = customersUrl + "/update";
        public static final String deleteCustomer = customersUrl + "/single/delete";
        public static final String deleteCustomers = customersUrl + "/multiple/delete";
    }

    public static final class Departments {
        public static final String departmentsUrl = apiUrl + "/departments";
        public static final String allDepartments = departmentsUrl + "/all";
        public static final String departmentById = departmentsUrl + "/single";
        public static final String searchDepartments = departmentsUrl + "/search";
        public static final String addDepartment = departmentsUrl + "/add";
        public static final String updateDepartment = departmentsUrl + "/update";
        public static final String deleteDepartment = departmentsUrl + "/single/delete";
        public static final String deleteDepartments = departmentsUrl + "/multiple/delete";
    }

    public static final class Designations {
        public static final String designationsUrl = apiUrl + "/designations";
        public static final String allDesignations = designationsUrl + "/all";
        public static final String designationById = designationsUrl + "/single";
        public static final String searchDesignations = designationsUrl + "/search";
        public static final String addDesignation = designationsUrl + "/add";
        public static final String updateDesignation = designationsUrl + "/update";
        public static final String deleteDesignation = designationsUrl + "/single/delete";
        public static final String deleteDesignations = designationsUrl + "/multiple/delete";
    }

    public static final class Expense {
        public static final String expenseUrl = apiUrl + "/expenses";
        public static final String allExpense = expenseUrl + "/all";
        public static final String designationById = expenseUrl + "/single";
        public static final String searchExpense = expenseUrl + "/search";
        public static final String addExpense = expenseUrl + "/add";
        public static final String updateExpense = expenseUrl + "/update";
        public static final String deleteExpense = expenseUrl + "/single/delete";
        public static final String deleteExpenses = expenseUrl + "/multiple/delete";
    }

    public static final class ExpenseCategories {
        public static final String expenseCategoriesUrl = apiUrl + "/expense/categories";
        public static final String allExpenseCategories = expenseCategoriesUrl + "/all";
        public static final String designationById = expenseCategoriesUrl + "/single";
        public static final String searchExpenseCategories = expenseCategoriesUrl + "/search";
        public static final String addExpenseCategory = expenseCategoriesUrl + "/add";
        public static final String updateExpenseCategory = expenseCategoriesUrl + "/update";
        public static final String deleteExpenseCategory = expenseCategoriesUrl + "/single/delete";
        public static final String deleteExpenseCategories = expenseCategoriesUrl + "/multiple/delete";
    }

    public static final class Organisations {
        public static final String organisationsUrl = apiUrl + "/organisations";
        public static final String allOrganisations = organisationsUrl + "/all";
        public static final String designationById = organisationsUrl + "/single";
        public static final String searchOrganisations = organisationsUrl + "/search";
        public static final String addOrganisation = organisationsUrl + "/add";
        public static final String updateOrganisation = organisationsUrl + "/update";
        public static final String deleteOrganisation = organisationsUrl + "/single/delete";
        public static final String deleteOrganisations = organisationsUrl + "/multiple/delete";
    }

    public static final class ProductCategories {
        public static final String productCategoryUrl = apiUrl + "/product/categories";
        public static final String allProductCategories = productCategoryUrl + "/all";
        public static final String productCategoriesById = productCategoryUrl + "/single";
        public static final String searchProductCategories = productCategoryUrl + "/search";
        public static final String addProductCategory = productCategoryUrl + "/add";
        public static final String updateProductCategory = productCategoryUrl + "/update";
        public static final String deleteProductCategory = productCategoryUrl + "/single/delete";
        public static final String deleteProductCategories = productCategoryUrl + "/multiple/delete";
    }

    public static final class Products {
        public static final String productsUrl = apiUrl + "/products";
        public static final String allProducts = productsUrl + "/all";
        public static final String productById = productsUrl + "/single";
        public static final String searchProducts = productsUrl + "/search";
        public static final String productsStockAlert = productsUrl + "/stock_alert";
        public static final String addProduct = productsUrl + "/add";
        public static final String updateProduct = productsUrl + "/update";
        public static final String deleteProduct = productsUrl + "/single/delete";
        public static final String deleteProducts = productsUrl + "/multiple/delete";
    }

    public static final class Purchases {
        public static final String purchasesUrl = apiUrl + "/purchases";
        // Masters
        public static final String allPurchaseMasters = purchasesUrl + "/masters";
        public static final String purchaseMasterById = purchasesUrl + "/master";
        public static final String searchPurchaseMasters = purchasesUrl + "/masters/search";
        public static final String addPurchaseMaster = purchasesUrl + "/master/add";
        public static final String updatePurchaseMaster = purchasesUrl + "/master/update";
        public static final String deletePurchaseMaster = purchasesUrl + "/master/delete";
        public static final String deletePurchaseMasters = purchasesUrl + "/masters/delete";
        // Details
        public static final String allPurchaseDetails = purchasesUrl + "/details";
        public static final String purchaseDetailById = purchasesUrl + "/detail";
        public static final String searchPurchaseDetails = purchasesUrl + "/details/search";
        public static final String addPurchaseDetail = purchasesUrl + "/detail/add";
        public static final String updatePurchaseDetail = purchasesUrl + "/detail/update";
        public static final String deletePurchaseDetail = purchasesUrl + "/detail/delete";
        public static final String deletePurchaseDetails = purchasesUrl + "/details/delete";
    }

    public static final class Quotations {
        public static final String quotationsUrl = apiUrl + "/quotations";
        // Masters
        public static final String allQuotationMasters = quotationsUrl + "/masters";
        public static final String quotationMasterById = quotationsUrl + "/master";
        public static final String searchQuotationMasters = quotationsUrl + "/masters/search";
        public static final String addQuotationMaster = quotationsUrl + "/master/add";
        public static final String updateQuotationMaster = quotationsUrl + "/master/update";
        public static final String deleteQuotationMaster = quotationsUrl + "/master/delete";
        public static final String deleteQuotationMasters = quotationsUrl + "/masters/delete";
        // Details
        public static final String allQuotationDetails = quotationsUrl + "/details";
        public static final String quotationDetailById = quotationsUrl + "/detail";
        public static final String searchQuotationDetails = quotationsUrl + "/details/search";
        public static final String addQuotationDetail = quotationsUrl + "/detail/add";
        public static final String updateQuotationDetail = quotationsUrl + "/detail/update";
        public static final String deleteQuotationDetail = quotationsUrl + "/detail/delete";
        public static final String deleteQuotationDetails = quotationsUrl + "/details/delete";
    }

    public static final class Requisitions {
        public static final String requisitionsUrl = apiUrl + "/requisitions";
        // Masters
        public static final String allRequisitionMasters = requisitionsUrl + "/masters";
        public static final String requisitionMasterById = requisitionsUrl + "/master";
        public static final String searchRequisitionMasters = requisitionsUrl + "/masters/search";
        public static final String addRequisitionMaster = requisitionsUrl + "/master/add";
        public static final String updateRequisitionMaster = requisitionsUrl + "/master/update";
        public static final String deleteRequisitionMaster = requisitionsUrl + "/master/delete";
        public static final String deleteRequisitionMasters = requisitionsUrl + "/masters/delete";
        // Details
        public static final String allRequisitionDetails = requisitionsUrl + "/details";
        public static final String requisitionDetailById = requisitionsUrl + "/detail";
        public static final String searchRequisitionDetails = requisitionsUrl + "/details/search";
        public static final String addRequisitionDetail = requisitionsUrl + "/detail/add";
        public static final String updateRequisitionDetail = requisitionsUrl + "/detail/update";
        public static final String deleteRequisitionDetail = requisitionsUrl + "/detail/delete";
        public static final String deleteRequisitionDetails = requisitionsUrl + "/details/delete";
    }

    public static final class SalaryAdvances {
        public static final String salaryAdvancesUrl = apiUrl + "/salary/advances";
        public static final String allSalaryAdvances = salaryAdvancesUrl + "/all";
        public static final String salaryAdvanceById = salaryAdvancesUrl + "/single";
        public static final String searchSalaryAdvances = salaryAdvancesUrl + "/search";
        public static final String addSalaryAdvance = salaryAdvancesUrl + "/add";
        public static final String updateSalaryAdvance = salaryAdvancesUrl + "/update";
        public static final String deleteSalaryAdvance = salaryAdvancesUrl + "/single/delete";
        public static final String deleteSalaryAdvances = salaryAdvancesUrl + "/multiple/delete";
    }

    public static final class Salaries {
        public static final String salariesUrl = apiUrl + "/salaries";
        public static final String allSalaries = salariesUrl + "/all";
        public static final String salaryById = salariesUrl + "/single";
        public static final String searchSalaries = salariesUrl + "/search";
        public static final String addSalary = salariesUrl + "/add";
        public static final String updateSalary = salariesUrl + "/update";
        public static final String deleteSalary = salariesUrl + "/single/delete";
        public static final String deleteSalaries = salariesUrl + "/multiple/delete";
    }

    public static final class Sales {
        public static final String salesUrl = apiUrl + "/sales";
        // Masters
        public static final String allSaleMasters = salesUrl + "/masters";
        public static final String saleMasterById = salesUrl + "/master";
        public static final String searchSaleMasters = salesUrl + "/masters/search";
        public static final String addSaleMaster = salesUrl + "/master/add";
        public static final String updateSaleMaster = salesUrl + "/master/update";
        public static final String deleteSaleMaster = salesUrl + "/master/delete";
        public static final String deleteSaleMasters = salesUrl + "/masters/delete";
        // Details
        public static final String allSaleDetails = salesUrl + "/details";
        public static final String saleDetailById = salesUrl + "/detail";
        public static final String searchSaleDetails = salesUrl + "/details/search";
        public static final String addSaleDetail = salesUrl + "/detail/add";
        public static final String updateSaleDetail = salesUrl + "/detail/update";
        public static final String deleteSaleDetail = salesUrl + "/detail/delete";
        public static final String deleteSaleDetails = salesUrl + "/details/delete";
    }

    public static final class SaleTermsAndConditions {
        public static final String saleTermsAndConditionsUrl = apiUrl + "/sale_terms_and_conditions";
        public static final String allSaleTermsAndConditions = saleTermsAndConditionsUrl + "/all";
        public static final String saleTermAndConditionById = saleTermsAndConditionsUrl + "/single";
        public static final String searchSaleTermsAndConditions = saleTermsAndConditionsUrl + "/search";
        public static final String addSaleTermAndCondition = saleTermsAndConditionsUrl + "/add";
        public static final String updateSaleTermAndCondition = saleTermsAndConditionsUrl + "/update";
        public static final String deleteSaleTermAndCondition = saleTermsAndConditionsUrl + "/single/delete";
        public static final String deleteSaleTermsAndConditions = saleTermsAndConditionsUrl + "/multiple/delete";
    }

    public static final class StockIns {
        public static final String stockInsUrl = apiUrl + "/stock_ins";
        // Masters
        public static final String allStockInMasters = stockInsUrl + "/masters";
        public static final String stockInMasterById = stockInsUrl + "/master";
        public static final String searchStockInMasters = stockInsUrl + "/masters/search";
        public static final String addStockInMaster = stockInsUrl + "/master/add";
        public static final String updateStockInMaster = stockInsUrl + "/master/update";
        public static final String deleteStockInMaster = stockInsUrl + "/master/delete";
        public static final String deleteStockInMasters = stockInsUrl + "/masters/delete";
        // Details
        public static final String allStockInDetails = stockInsUrl + "/details";
        public static final String stockInDetailById = stockInsUrl + "/detail";
        public static final String searchStockInDetails = stockInsUrl + "/details/search";
        public static final String addStockInDetail = stockInsUrl + "/detail/add";
        public static final String updateStockInDetail = stockInsUrl + "/detail/update";
        public static final String deleteStockInDetail = stockInsUrl + "/detail/delete";
        public static final String deleteStockInDetails = stockInsUrl + "/details/delete";
    }

    public static final class Suppliers {
        public static final String suppliersUrl = apiUrl + "/suppliers";
        public static final String allSuppliers = suppliersUrl + "/all";
        public static final String supplierById = suppliersUrl + "/single";
        public static final String searchSuppliers = suppliersUrl + "/search";
        public static final String addSupplier = suppliersUrl + "/add";
        public static final String updateSupplier = suppliersUrl + "/update";
        public static final String deleteSupplier = suppliersUrl + "/single/delete";
        public static final String deleteSuppliers = suppliersUrl + "/multiple/delete";
    }

    public static final class Transfers {
        public static final String transfersUrl = apiUrl + "/transfers";
        // Masters
        public static final String allTransferMasters = transfersUrl + "/masters";
        public static final String transferMasterById = transfersUrl + "/master";
        public static final String searchTransferMasters = transfersUrl + "/masters/search";
        public static final String addTransferMaster = transfersUrl + "/master/add";
        public static final String updateTransferMaster = transfersUrl + "/master/update";
        public static final String deleteTransferMaster = transfersUrl + "/master/delete";
        public static final String deleteTransferMasters = transfersUrl + "/masters/delete";
        // Details
        public static final String allTransferDetails = transfersUrl + "/details";
        public static final String transferDetailById = transfersUrl + "/detail";
        public static final String searchTransferDetails = transfersUrl + "/details/search";
        public static final String addTransferDetail = transfersUrl + "/detail/add";
        public static final String updateTransferDetail = transfersUrl + "/detail/update";
        public static final String deleteTransferDetail = transfersUrl + "/detail/delete";
        public static final String deleteTransferDetails = transfersUrl + "/details/delete";
    }

    public static final class UnitsOfMeasure {
        public static final String unitsOfMeasureUrl = apiUrl + "/units_of_measure";
        public static final String allUnitsOfMeasure = unitsOfMeasureUrl + "/all";
        public static final String unitOfMeasureById = unitsOfMeasureUrl + "/single";
        public static final String searchUnitsOfMeasure = unitsOfMeasureUrl + "/search";
        public static final String addUnitOfMeasure = unitsOfMeasureUrl + "/add";
        public static final String updateUnitOfMeasure = unitsOfMeasureUrl + "/update";
        public static final String deleteUnitOfMeasure = unitsOfMeasureUrl + "/single/delete";
        public static final String deleteUnitsOfMeasure = unitsOfMeasureUrl + "/multiple/delete";
    }

    public static final class Users {
        public static final String usersUrl = apiUrl + "/users";
        public static final String allUsers = usersUrl + "/all";
        public static final String userById = usersUrl + "/single";
        public static final String searchUsers = usersUrl + "/search";
        public static final String addUser = usersUrl + "/add";
        public static final String updateUser = usersUrl + "/update";
        public static final String deleteUser = usersUrl + "/single/delete";
        public static final String deleteUsers = usersUrl + "/multiple/delete";
    }

    public static final class UserProfiles {
        public static final String userProfilesUrl = apiUrl + "/user/profiles";
        public static final String allUserProfiles = userProfilesUrl + "/all";
        public static final String userProfileById = userProfilesUrl + "/single";
        public static final String searchUserProfiles = userProfilesUrl + "/search";
        public static final String addUserProfile = userProfilesUrl + "/add";
        public static final String updateUserProfile = userProfilesUrl + "/update";
        public static final String deleteUserProfile = userProfilesUrl + "/single/delete";
        public static final String deleteUserProfiles = userProfilesUrl + "/multiple/delete";
    }

    public static final class EmploymentStatus {
        public static final String employmentStatusesUrl = apiUrl + "/employment/statuses";
        public static final String allEmploymentStatuses = employmentStatusesUrl + "/all";
        public static final String employmentStatusById = employmentStatusesUrl + "/single";
        public static final String searchEmploymentStatuses = employmentStatusesUrl + "/search";
        public static final String addEmploymentStatus = employmentStatusesUrl + "/add";
        public static final String updateEmploymentStatus = employmentStatusesUrl + "/update";
        public static final String deleteEmploymentStatus = employmentStatusesUrl + "/single/delete";
        public static final String deleteEmploymentStatuses = employmentStatusesUrl + "/multiple/delete";
    }

    public static final class LeaveStatus {
        public static final String leaveStatusesUrl = apiUrl + "/leave/statuses";
        public static final String allLeaveStatuses = leaveStatusesUrl + "/all";
        public static final String leaveStatusById = leaveStatusesUrl + "/single";
        public static final String searchLeaveStatuses = leaveStatusesUrl + "/search";
        public static final String addLeaveStatus = leaveStatusesUrl + "/add";
        public static final String updateLeaveStatus = leaveStatusesUrl + "/update";
        public static final String deleteLeaveStatus = leaveStatusesUrl + "/single/delete";
        public static final String deleteLeaveStatuses = leaveStatusesUrl + "/multiple/delete";
    }

    public static final class PaySlip {
        public static final String paySlipUrl = apiUrl + "/payslip";
        public static final String allPaySlips = paySlipUrl + "/all";
        public static final String paySlipById = paySlipUrl + "/single";
        public static final String searchPaySlips = paySlipUrl + "/search";
        public static final String addPaySlip = paySlipUrl + "/add";
        public static final String updatePaySlip = paySlipUrl + "/update";
        public static final String deletePaySlip = paySlipUrl + "/single/delete";
        public static final String deletePaySlips = paySlipUrl + "/multiple/delete";
    }

    public static final class BeneficiaryBadge {
        public static final String beneficiaryBadgeUrl = apiUrl + "/beneficiary/badge";
        public static final String allBeneficiaryBadges = beneficiaryBadgeUrl + "/all";
        public static final String beneficiaryBadgeById = beneficiaryBadgeUrl + "/single";
        public static final String searchBeneficiaryBadges = beneficiaryBadgeUrl + "/search";
        public static final String addBeneficiaryBadge = beneficiaryBadgeUrl + "/add";
        public static final String updateBeneficiaryBadge = beneficiaryBadgeUrl + "/update";
        public static final String deleteBeneficiaryBadge = beneficiaryBadgeUrl + "/single/delete";
        public static final String deleteBeneficiaryBadges = beneficiaryBadgeUrl + "/multiple/delete";
    }

    public static final class BeneficiaryType {
        public static final String beneficiaryTypeUrl = apiUrl + "/beneficiary/type";
        public static final String allBeneficiaryTypes = beneficiaryTypeUrl + "/all";
        public static final String beneficiaryTypeById = beneficiaryTypeUrl + "/single";
        public static final String searchBeneficiaryTypes = beneficiaryTypeUrl + "/search";
        public static final String addBeneficiaryType = beneficiaryTypeUrl + "/add";
        public static final String updateBeneficiaryType = beneficiaryTypeUrl + "/update";
        public static final String deleteBeneficiaryType = beneficiaryTypeUrl + "/single/delete";
        public static final String deleteBeneficiaryTypes = beneficiaryTypeUrl + "/multiple/delete";
    }

    public static final class Email {
        public static final String emailUrl = apiUrl + "/email";
        public static final String allEmails = emailUrl + "/all";
        public static final String emailById = emailUrl + "/single";
        public static final String searchEmails = emailUrl + "/search";
        public static final String addEmail = emailUrl + "/add";
        public static final String updateEmail = emailUrl + "/update";
        public static final String deleteEmail = emailUrl + "/single/delete";
        public static final String deleteEmails = emailUrl + "/multiple/delete";
    }

    public static final class Tax {
        public static final String taxUrl = apiUrl + "/tax";
        public static final String allTaxes = taxUrl + "/all";
        public static final String taxById = taxUrl + "/single";
        public static final String searchTaxes = taxUrl + "/search";
        public static final String addTax = taxUrl + "/add";
        public static final String updateTax = taxUrl + "/update";
        public static final String deleteTax = taxUrl + "/single/delete";
        public static final String deleteTaxes = taxUrl + "/multiple/delete";
    }

    public static final class Discount {
        public static final String discountUrl = apiUrl + "/discount";
        public static final String allDiscounts = discountUrl + "/all";
        public static final String discountById = discountUrl + "/single";
        public static final String searchDiscounts = discountUrl + "/search";
        public static final String addDiscount = discountUrl + "/add";
        public static final String updateDiscount = discountUrl + "/update";
        public static final String deleteDiscount = discountUrl + "/single/delete";
        public static final String deleteDiscounts = discountUrl + "/multiple/delete";
    }

    public static final class Roles {
        public static final String rolesUrl = apiUrl + "/roles";
        // Roles
        public static final String allRoles = rolesUrl + "/all";
        public static final String roleById = rolesUrl + "/single";
        public static final String addRole = rolesUrl + "/add";
        public static final String updateRole = rolesUrl + "/update";
        public static final String deleteRole = rolesUrl + "/delete";
        // Permissions
        public static final String allPermissions = rolesUrl + "/permissions";
        public static final String permissionById = rolesUrl + "/permission";
        public static final String addPermission = rolesUrl + "/permission/add";
        public static final String updatePermission = rolesUrl + "/permission/update";
        public static final String deletePermission = rolesUrl + "/permission/delete";
    }

    public static final class Payments {
        public static final String paymentsUrl = apiUrl + "/payments";
        public static final String cardPay = paymentsUrl + "/pay/card";
        public static final String mtnMomoPay = paymentsUrl + "/pay/mtn";
        public static final String airtelMoPay = paymentsUrl + "/pay/airtel";
        public static final String trial = apiUrl + "/tenants/start/trial";
    }
}
