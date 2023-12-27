package org.infinite.spoty.data_source.repositories.interfaces;

import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public interface SimpleRepository {
    HttpResponse<String> fetchAll() throws IOException, InterruptedException;

    HttpResponse<String> fetch(FindModel findModel) throws IOException, InterruptedException;

    HttpResponse<String> search(SearchModel searchModel) throws IOException, InterruptedException;

    HttpResponse<String> post(Object object) throws IOException, InterruptedException;

    HttpResponse<String> put(Object object) throws IOException, InterruptedException;

    HttpResponse<String> delete(FindModel findModel) throws IOException, InterruptedException;

    HttpResponse<String> deleteMultiple(ArrayList<FindModel> findModelList);
}
