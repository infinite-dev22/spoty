package org.infinite.spoty.values;

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

    public static final class Products {
        public static final String productsUrl = apiUrl + "/products";
        public static final String allProducts = productsUrl + "/all";
        public static final String productById = productsUrl + "/single";
        public static final String searchProducts = productsUrl + "/search";
        public static final String productsStockAlert = productsUrl + "/stock_alert";
        public static final String addProduct = productsUrl + "/add";
        public static final String updateProduct = productsUrl + "/update";
        public static final String deleteProduct = productsUrl + "/single/delete";
    }

    public static final class Attendances {
        public static final String attendancesUrl = apiUrl + "/attendances";
        public static final String allAttendances = attendancesUrl + "/all";
        public static final String attendanceById = attendancesUrl + "/single";
        public static final String searchAttendances = attendancesUrl + "/search";
        public static final String addAttendance = attendancesUrl + "/add";
        public static final String updateAttendance = attendancesUrl + "/update";
        public static final String deleteAttendance = attendancesUrl + "/single/delete";
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
    }

    public static final class Branches {
        public static final String branchesUrl = apiUrl + "/branches";
        public static final String allBranches = branchesUrl + "/all";
        public static final String branchById = branchesUrl + "/single";
        public static final String searchBranches = branchesUrl + "/search";
        public static final String addBranch = branchesUrl + "/add";
        public static final String updateBranch = branchesUrl + "/update";
        public static final String deleteBranch = branchesUrl + "/single/delete";
    }

    public static final class Brands {
        public static final String brandsUrl = apiUrl + "/brands";
        public static final String allBrands = brandsUrl + "/all";
        public static final String brandById = brandsUrl + "/single";
        public static final String searchBrands = brandsUrl + "/search";
        public static final String addBrand = brandsUrl + "/add";
        public static final String updateBrand = brandsUrl + "/update";
        public static final String deleteBrand = brandsUrl + "/single/delete";
    }

    public static final class Currencies {
        public static final String currenciesUrl = apiUrl + "/currencies";
        public static final String allCurrencies = currenciesUrl + "/all";
        public static final String currencyById = currenciesUrl + "/single";
        public static final String searchCurrencies = currenciesUrl + "/search";
        public static final String addCurrency = currenciesUrl + "/add";
        public static final String updateCurrency = currenciesUrl + "/update";
        public static final String deleteCurrency = currenciesUrl + "/single/delete";
    }
}
