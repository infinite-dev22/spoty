package org.infinite.spoty.data_source.repositories.interfaces;

import javafx.concurrent.Task;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedList;

public interface MasterDetailRepository {
    // Master
    Task<HttpResponse<String>> fetchAllMaster();

    Task<HttpResponse<String>> fetchMaster(FindModel findModel);

    Task<HttpResponse<String>> searchMaster(SearchModel searchModel);

    Task<HttpResponse<String>> postMaster(Object object);

    Task<HttpResponse<String>> putMaster(Object object);

    Task<HttpResponse<String>> deleteMaster(FindModel findModel);

    Task<HttpResponse<String>> deleteMultipleMasters(ArrayList<FindModel> findModelList);

    // Detail
    Task<HttpResponse<String>> fetchAllDetail();

    Task<HttpResponse<String>> fetchDetail(FindModel findModel);

    Task<HttpResponse<String>> searchDetail(SearchModel searchModel);

    Task<HttpResponse<String>> postDetail(Object object);

    Task<HttpResponse<String>> putDetail(Object object);

    Task<HttpResponse<String>> deleteDetail(FindModel findModel);

    Task<HttpResponse<String>> deleteMultipleDetails(LinkedList<FindModel> findModelList);
}
