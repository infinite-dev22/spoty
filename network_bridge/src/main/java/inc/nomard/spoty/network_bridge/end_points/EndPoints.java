package inc.nomard.spoty.network_bridge.end_points;

import lombok.extern.java.*;

@Log
public class EndPoints {
    public static final String appDNS = "http://localhost:8080";
    public static final String apiUrl = appDNS + "/api/v1";

    public static final class Adjustments {
        public static final String adjustmentsUrl = apiUrl + "/adjustments";
        public static final String allAdjustments = adjustmentsUrl + "/masters";
        public static final String adjustmentById = adjustmentsUrl + "/master";
        public static final String searchAdjustments = adjustmentsUrl + "/search";
        public static final String addAdjustment = adjustmentsUrl + "/master/add";
        public static final String updateAdjustment = adjustmentsUrl + "/master/update";
        public static final String deleteAdjustment = adjustmentsUrl + "/delete";
        public static final String deleteAdjustments = adjustmentsUrl + "/delete/multiple";
    }

    public static final class Attendances {
        public static final String attendancesUrl = apiUrl + "/attendances";
        public static final String allAttendances = attendancesUrl + "/all";
        public static final String attendanceById = attendancesUrl + "/single";
        public static final String searchAttendances = attendancesUrl + "/search";
        public static final String addAttendance = attendancesUrl + "/add";
        public static final String updateAttendance = attendancesUrl + "/update";
        public static final String deleteAttendance = attendancesUrl + "/delete";
        public static final String deleteAttendances = attendancesUrl + "/delete/multiple";
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
        public static final String deleteBank = banksUrl + "/delete";
        public static final String deleteBanks = banksUrl + "/delete/multiple";
    }

    public static final class Branches {
        public static final String branchesUrl = apiUrl + "/branches";
        public static final String allBranches = branchesUrl + "/all";
        public static final String branchById = branchesUrl + "/single";
        public static final String searchBranches = branchesUrl + "/search";
        public static final String addBranch = branchesUrl + "/add";
        public static final String updateBranch = branchesUrl + "/update";
        public static final String deleteBranch = branchesUrl + "/delete";
        public static final String deleteBranches = branchesUrl + "/delete/multiple";
    }

    public static final class Brands {
        public static final String brandsUrl = apiUrl + "/brands";
        public static final String allBrands = brandsUrl + "/all";
        public static final String brandById = brandsUrl + "/single";
        public static final String searchBrands = brandsUrl + "/search";
        public static final String addBrand = brandsUrl + "/add";
        public static final String updateBrand = brandsUrl + "/update";
        public static final String deleteBrand = brandsUrl + "/delete";
        public static final String deleteBrands = brandsUrl + "/delete/multiple";
    }

    public static final class Currencies {
        public static final String currenciesUrl = apiUrl + "/currencies";
        public static final String allCurrencies = currenciesUrl + "/all";
        public static final String currencyById = currenciesUrl + "/single";
        public static final String searchCurrencies = currenciesUrl + "/search";
        public static final String addCurrency = currenciesUrl + "/add";
        public static final String updateCurrency = currenciesUrl + "/update";
        public static final String deleteCurrency = currenciesUrl + "/delete";
        public static final String deleteCurrencies = currenciesUrl + "/delete/multiple";
    }

    public static final class Customers {
        public static final String customersUrl = apiUrl + "/customers";
        public static final String allCustomers = customersUrl + "/all";
        public static final String customerById = customersUrl + "/single";
        public static final String searchCustomers = customersUrl + "/search";
        public static final String addCustomer = customersUrl + "/add";
        public static final String updateCustomer = customersUrl + "/update";
        public static final String deleteCustomer = customersUrl + "/delete";
        public static final String deleteCustomers = customersUrl + "/delete/multiple";
    }

    public static final class Departments {
        public static final String departmentsUrl = apiUrl + "/departments";
        public static final String allDepartments = departmentsUrl + "/all";
        public static final String departmentById = departmentsUrl + "/single";
        public static final String searchDepartments = departmentsUrl + "/search";
        public static final String addDepartment = departmentsUrl + "/add";
        public static final String updateDepartment = departmentsUrl + "/update";
        public static final String deleteDepartment = departmentsUrl + "/delete";
        public static final String deleteDepartments = departmentsUrl + "/delete/multiple";
    }

    public static final class Designations {
        public static final String designationsUrl = apiUrl + "/designations";
        public static final String allDesignations = designationsUrl + "/all";
        public static final String designationById = designationsUrl + "/single";
        public static final String searchDesignations = designationsUrl + "/search";
        public static final String addDesignation = designationsUrl + "/add";
        public static final String updateDesignation = designationsUrl + "/update";
        public static final String deleteDesignation = designationsUrl + "/delete";
        public static final String deleteDesignations = designationsUrl + "/delete/multiple";
    }

    public static final class Expense {
        public static final String expenseUrl = apiUrl + "/expenses";
        public static final String allExpense = expenseUrl + "/all";
        public static final String designationById = expenseUrl + "/single";
        public static final String searchExpense = expenseUrl + "/search";
        public static final String addExpense = expenseUrl + "/add";
        public static final String updateExpense = expenseUrl + "/update";
        public static final String deleteExpense = expenseUrl + "/delete";
        public static final String deleteExpenses = expenseUrl + "/delete/multiple";
    }

    public static final class ExpenseCategories {
        public static final String expenseCategoriesUrl = apiUrl + "/expense/categories";
        public static final String allExpenseCategories = expenseCategoriesUrl + "/all";
        public static final String designationById = expenseCategoriesUrl + "/single";
        public static final String searchExpenseCategories = expenseCategoriesUrl + "/search";
        public static final String addExpenseCategory = expenseCategoriesUrl + "/add";
        public static final String updateExpenseCategory = expenseCategoriesUrl + "/update";
        public static final String deleteExpenseCategory = expenseCategoriesUrl + "/delete";
        public static final String deleteExpenseCategories = expenseCategoriesUrl + "/delete/multiple";
    }

    public static final class Organisations {
        public static final String organisationsUrl = apiUrl + "/organisations";
        public static final String allOrganisations = organisationsUrl + "/all";
        public static final String designationById = organisationsUrl + "/single";
        public static final String searchOrganisations = organisationsUrl + "/search";
        public static final String addOrganisation = organisationsUrl + "/add";
        public static final String updateOrganisation = organisationsUrl + "/update";
        public static final String deleteOrganisation = organisationsUrl + "/delete";
        public static final String deleteOrganisations = organisationsUrl + "/delete/multiple";
    }

    public static final class ProductCategories {
        public static final String productCategoryUrl = apiUrl + "/product/categories";
        public static final String allProductCategories = productCategoryUrl + "/all";
        public static final String productCategoriesById = productCategoryUrl + "/single";
        public static final String searchProductCategories = productCategoryUrl + "/search";
        public static final String addProductCategory = productCategoryUrl + "/add";
        public static final String updateProductCategory = productCategoryUrl + "/update";
        public static final String deleteProductCategory = productCategoryUrl + "/delete";
        public static final String deleteProductCategories = productCategoryUrl + "/delete/multiple";
    }

    public static final class Products {
        public static final String productsUrl = apiUrl + "/products";
        public static final String allProducts = productsUrl + "/all";
        public static final String productById = productsUrl + "/single";
        public static final String searchProducts = productsUrl + "/search";
        public static final String productsStockAlert = productsUrl + "/stock_alert";
        public static final String addProduct = productsUrl + "/add";
        public static final String updateProduct = productsUrl + "/update";
        public static final String deleteProduct = productsUrl + "/delete";
        public static final String deleteProducts = productsUrl + "/delete/multiple";
    }

    public static final class Purchases {
        public static final String purchasesUrl = apiUrl + "/purchases";
        public static final String allPurchases = purchasesUrl + "/masters";
        public static final String purchaseById = purchasesUrl + "/master";
        public static final String searchPurchases = purchasesUrl + "/search";
        public static final String addPurchase = purchasesUrl + "/master/add";
        public static final String updatePurchase = purchasesUrl + "/master/update";
        public static final String deletePurchase = purchasesUrl + "/delete";
        public static final String deletePurchases = purchasesUrl + "/delete/multiple";
    }

    public static final class Quotations {
        public static final String quotationsUrl = apiUrl + "/quotations";
        public static final String allQuotations = quotationsUrl + "/masters";
        public static final String quotationById = quotationsUrl + "/master";
        public static final String searchQuotations = quotationsUrl + "/search";
        public static final String addQuotation = quotationsUrl + "/master/add";
        public static final String updateQuotation = quotationsUrl + "/master/update";
        public static final String deleteQuotation = quotationsUrl + "/delete";
        public static final String deleteQuotations = quotationsUrl + "/delete/multiple";
    }

    public static final class Requisitions {
        public static final String requisitionsUrl = apiUrl + "/requisitions";
        public static final String allRequisitions = requisitionsUrl + "/masters";
        public static final String requisitionById = requisitionsUrl + "/master";
        public static final String searchRequisitions = requisitionsUrl + "/search";
        public static final String addRequisition = requisitionsUrl + "/master/add";
        public static final String updateRequisition = requisitionsUrl + "/master/update";
        public static final String deleteRequisition = requisitionsUrl + "/delete";
        public static final String deleteRequisitions = requisitionsUrl + "/delete/multiple";
    }

    public static final class SalaryAdvances {
        public static final String salaryAdvancesUrl = apiUrl + "/salary/advances";
        public static final String allSalaryAdvances = salaryAdvancesUrl + "/all";
        public static final String salaryAdvanceById = salaryAdvancesUrl + "/single";
        public static final String searchSalaryAdvances = salaryAdvancesUrl + "/search";
        public static final String addSalaryAdvance = salaryAdvancesUrl + "/add";
        public static final String updateSalaryAdvance = salaryAdvancesUrl + "/update";
        public static final String deleteSalaryAdvance = salaryAdvancesUrl + "/delete";
        public static final String deleteSalaryAdvances = salaryAdvancesUrl + "/delete/multiple";
    }

    public static final class Salaries {
        public static final String salariesUrl = apiUrl + "/salaries";
        public static final String allSalaries = salariesUrl + "/all";
        public static final String salaryById = salariesUrl + "/single";
        public static final String searchSalaries = salariesUrl + "/search";
        public static final String addSalary = salariesUrl + "/add";
        public static final String updateSalary = salariesUrl + "/update";
        public static final String deleteSalary = salariesUrl + "/delete";
        public static final String deleteSalaries = salariesUrl + "/delete/multiple";
    }

    public static final class Sales {
        public static final String salesUrl = apiUrl + "/sales";
        public static final String allSales = salesUrl + "/masters";
        public static final String saleById = salesUrl + "/master";
        public static final String searchSales = salesUrl + "/search";
        public static final String addSale = salesUrl + "/master/add";
        public static final String updateSale = salesUrl + "/master/update";
        public static final String deleteSale = salesUrl + "/delete";
        public static final String deleteSales = salesUrl + "/delete/multiple";
    }

    public static final class SaleTermsAndConditions {
        public static final String saleTermsAndConditionsUrl = apiUrl + "/sale_terms_and_conditions";
        public static final String allSaleTermsAndConditions = saleTermsAndConditionsUrl + "/all";
        public static final String saleTermAndConditionById = saleTermsAndConditionsUrl + "/single";
        public static final String searchSaleTermsAndConditions = saleTermsAndConditionsUrl + "/search";
        public static final String addSaleTermAndCondition = saleTermsAndConditionsUrl + "/add";
        public static final String updateSaleTermAndCondition = saleTermsAndConditionsUrl + "/update";
        public static final String deleteSaleTermAndCondition = saleTermsAndConditionsUrl + "/delete";
        public static final String deleteSaleTermsAndConditions = saleTermsAndConditionsUrl + "/delete/multiple";
    }

    public static final class StockIns {
        public static final String stockInsUrl = apiUrl + "/stock_ins";
        public static final String allStockIns = stockInsUrl + "/masters";
        public static final String stockInById = stockInsUrl + "/master";
        public static final String searchStockIns = stockInsUrl + "/search";
        public static final String addStockIn = stockInsUrl + "/master/add";
        public static final String updateStockIn = stockInsUrl + "/master/update";
        public static final String deleteStockIn = stockInsUrl + "/delete";
        public static final String deleteStockIns = stockInsUrl + "/delete/multiple";
    }

    public static final class Suppliers {
        public static final String suppliersUrl = apiUrl + "/suppliers";
        public static final String allSuppliers = suppliersUrl + "/all";
        public static final String supplierById = suppliersUrl + "/single";
        public static final String searchSuppliers = suppliersUrl + "/search";
        public static final String addSupplier = suppliersUrl + "/add";
        public static final String updateSupplier = suppliersUrl + "/update";
        public static final String deleteSupplier = suppliersUrl + "/delete";
        public static final String deleteSuppliers = suppliersUrl + "/delete/multiple";
    }

    public static final class Transfers {
        public static final String transfersUrl = apiUrl + "/transfers";
        public static final String allTransfers = transfersUrl + "/masters";
        public static final String transferById = transfersUrl + "/master";
        public static final String searchTransfers = transfersUrl + "/search";
        public static final String addTransfer = transfersUrl + "/master/add";
        public static final String updateTransfer = transfersUrl + "/master/update";
        public static final String deleteTransfer = transfersUrl + "/delete";
        public static final String deleteTransfers = transfersUrl + "/delete/multiple";
    }

    public static final class UnitsOfMeasure {
        public static final String unitsOfMeasureUrl = apiUrl + "/units_of_measure";
        public static final String allUnitsOfMeasure = unitsOfMeasureUrl + "/all";
        public static final String unitOfMeasureById = unitsOfMeasureUrl + "/single";
        public static final String searchUnitsOfMeasure = unitsOfMeasureUrl + "/search";
        public static final String addUnitOfMeasure = unitsOfMeasureUrl + "/add";
        public static final String updateUnitOfMeasure = unitsOfMeasureUrl + "/update";
        public static final String deleteUnitOfMeasure = unitsOfMeasureUrl + "/delete";
        public static final String deleteUnitsOfMeasure = unitsOfMeasureUrl + "/delete/multiple";
    }

    public static final class Users {
        public static final String usersUrl = apiUrl + "/users";
        public static final String allUsers = usersUrl + "/all";
        public static final String userById = usersUrl + "/single";
        public static final String searchUsers = usersUrl + "/search";
        public static final String addUser = usersUrl + "/add";
        public static final String updateUser = usersUrl + "/update";
        public static final String deleteUser = usersUrl + "/delete";
        public static final String deleteUsers = usersUrl + "/delete/multiple";
    }

    public static final class UserProfiles {
        public static final String userProfilesUrl = apiUrl + "/user/profiles";
        public static final String allUserProfiles = userProfilesUrl + "/all";
        public static final String userProfileById = userProfilesUrl + "/single";
        public static final String searchUserProfiles = userProfilesUrl + "/search";
        public static final String addUserProfile = userProfilesUrl + "/add";
        public static final String updateUserProfile = userProfilesUrl + "/update";
        public static final String deleteUserProfile = userProfilesUrl + "/delete";
        public static final String deleteUserProfiles = userProfilesUrl + "/delete/multiple";
    }

    public static final class EmploymentStatus {
        public static final String employmentStatusesUrl = apiUrl + "/employment/statuses";
        public static final String allEmploymentStatuses = employmentStatusesUrl + "/all";
        public static final String employmentStatusById = employmentStatusesUrl + "/single";
        public static final String searchEmploymentStatuses = employmentStatusesUrl + "/search";
        public static final String addEmploymentStatus = employmentStatusesUrl + "/add";
        public static final String updateEmploymentStatus = employmentStatusesUrl + "/update";
        public static final String deleteEmploymentStatus = employmentStatusesUrl + "/delete";
        public static final String deleteEmploymentStatuses = employmentStatusesUrl + "/delete/multiple";
    }

    public static final class LeaveStatus {
        public static final String leaveStatusesUrl = apiUrl + "/leave/statuses";
        public static final String allLeaveStatuses = leaveStatusesUrl + "/all";
        public static final String leaveStatusById = leaveStatusesUrl + "/single";
        public static final String searchLeaveStatuses = leaveStatusesUrl + "/search";
        public static final String addLeaveStatus = leaveStatusesUrl + "/add";
        public static final String updateLeaveStatus = leaveStatusesUrl + "/update";
        public static final String deleteLeaveStatus = leaveStatusesUrl + "/delete";
        public static final String deleteLeaveStatuses = leaveStatusesUrl + "/delete/multiple";
    }

    public static final class PaySlip {
        public static final String paySlipUrl = apiUrl + "/payslip";
        public static final String allPaySlips = paySlipUrl + "/all";
        public static final String paySlipById = paySlipUrl + "/single";
        public static final String searchPaySlips = paySlipUrl + "/search";
        public static final String addPaySlip = paySlipUrl + "/add";
        public static final String updatePaySlip = paySlipUrl + "/update";
        public static final String deletePaySlip = paySlipUrl + "/delete";
        public static final String deletePaySlips = paySlipUrl + "/delete/multiple";
    }

    public static final class BeneficiaryBadge {
        public static final String beneficiaryBadgeUrl = apiUrl + "/beneficiary/badge";
        public static final String allBeneficiaryBadges = beneficiaryBadgeUrl + "/all";
        public static final String beneficiaryBadgeById = beneficiaryBadgeUrl + "/single";
        public static final String searchBeneficiaryBadges = beneficiaryBadgeUrl + "/search";
        public static final String addBeneficiaryBadge = beneficiaryBadgeUrl + "/add";
        public static final String updateBeneficiaryBadge = beneficiaryBadgeUrl + "/update";
        public static final String deleteBeneficiaryBadge = beneficiaryBadgeUrl + "/delete";
        public static final String deleteBeneficiaryBadges = beneficiaryBadgeUrl + "/delete/multiple";
    }

    public static final class BeneficiaryType {
        public static final String beneficiaryTypeUrl = apiUrl + "/beneficiary/type";
        public static final String allBeneficiaryTypes = beneficiaryTypeUrl + "/all";
        public static final String beneficiaryTypeById = beneficiaryTypeUrl + "/single";
        public static final String searchBeneficiaryTypes = beneficiaryTypeUrl + "/search";
        public static final String addBeneficiaryType = beneficiaryTypeUrl + "/add";
        public static final String updateBeneficiaryType = beneficiaryTypeUrl + "/update";
        public static final String deleteBeneficiaryType = beneficiaryTypeUrl + "/delete";
        public static final String deleteBeneficiaryTypes = beneficiaryTypeUrl + "/delete/multiple";
    }

    public static final class Email {
        public static final String emailUrl = apiUrl + "/email";
        public static final String allEmails = emailUrl + "/all";
        public static final String emailById = emailUrl + "/single";
        public static final String searchEmails = emailUrl + "/search";
        public static final String addEmail = emailUrl + "/add";
        public static final String updateEmail = emailUrl + "/update";
        public static final String deleteEmail = emailUrl + "/delete";
        public static final String deleteEmails = emailUrl + "/delete/multiple";
    }

    public static final class Tax {
        public static final String taxUrl = apiUrl + "/tax";
        public static final String allTaxes = taxUrl + "/all";
        public static final String taxById = taxUrl + "/single";
        public static final String searchTaxes = taxUrl + "/search";
        public static final String addTax = taxUrl + "/add";
        public static final String updateTax = taxUrl + "/update";
        public static final String deleteTax = taxUrl + "/delete";
        public static final String deleteTaxes = taxUrl + "/delete/multiple";
    }

    public static final class Discount {
        public static final String discountUrl = apiUrl + "/discount";
        public static final String allDiscounts = discountUrl + "/all";
        public static final String discountById = discountUrl + "/single";
        public static final String searchDiscounts = discountUrl + "/search";
        public static final String addDiscount = discountUrl + "/add";
        public static final String updateDiscount = discountUrl + "/update";
        public static final String deleteDiscount = discountUrl + "/delete";
        public static final String deleteDiscounts = discountUrl + "/delete/multiple";
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
