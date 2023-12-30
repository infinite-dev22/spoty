package org.infinite.spoty.data_source.repositories.interfaces;

import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public interface MasterDetailRepository {
    // Master
    HttpResponse<String> fetchAllMaster() throws IOException, InterruptedException;

    HttpResponse<String> fetchMaster(FindModel findModel) throws IOException, InterruptedException;

    HttpResponse<String> searchMaster(SearchModel searchModel) throws IOException, InterruptedException;

    HttpResponse<String> postMaster(Object object) throws IOException, InterruptedException;

    HttpResponse<String> putMaster(Object object) throws IOException, InterruptedException;

    HttpResponse<String> deleteMaster(FindModel findModel) throws IOException, InterruptedException;

    HttpResponse<String> deleteMultipleMasters(ArrayList<FindModel> findModelList) throws IOException, InterruptedException;

    // Detail
    HttpResponse<String> fetchAllDetail() throws IOException, InterruptedException;

    HttpResponse<String> fetchDetail(FindModel findModel) throws IOException, InterruptedException;

    HttpResponse<String> searchDetail(SearchModel searchModel) throws IOException, InterruptedException;

    HttpResponse<String> postDetail(Object object) throws IOException, InterruptedException;

    HttpResponse<String> putDetail(Object object) throws IOException, InterruptedException;

    HttpResponse<String> deleteDetail(FindModel findModel) throws IOException, InterruptedException;

    HttpResponse<String> deleteMultipleDetails(ArrayList<FindModel> findModelList) throws IOException, InterruptedException;
}
