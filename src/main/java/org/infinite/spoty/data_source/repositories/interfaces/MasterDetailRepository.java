package org.infinite.spoty.data_source.repositories.interfaces;

import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;

import java.net.http.HttpResponse;
import java.util.ArrayList;

public interface MasterDetailRepository {
    // Master
    HttpResponse<String> fetchAllMaster();

    HttpResponse<String> fetchMaster(FindModel findModel);

    HttpResponse<String> searchMaster(SearchModel searchModel);

    HttpResponse<String> postMaster(Object object);

    HttpResponse<String> putMaster(Object object);

    HttpResponse<String> deleteMaster(FindModel findModel);

    HttpResponse<String> deleteMultipleMasters(ArrayList<FindModel> findModelList);

    // Detail
    HttpResponse<String> fetchAllDetail();

    HttpResponse<String> fetchDetail(FindModel findModel);

    HttpResponse<String> searchDetail(SearchModel searchModel);

    HttpResponse<String> postDetail(Object object);

    HttpResponse<String> putDetail(Object object);

    HttpResponse<String> deleteDetail(FindModel findModel);

    HttpResponse<String> deleteMultipleDetails(ArrayList<FindModel> findModelList);
}
