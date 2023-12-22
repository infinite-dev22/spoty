package org.infinite.spoty.data_source.repositories.interfaces;

import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;

import java.net.http.HttpResponse;
import java.util.ArrayList;

public interface SimpleRepository {
    HttpResponse<String> fetchAll();

    HttpResponse<String> fetch(FindModel findModel);

    HttpResponse<String> search(SearchModel searchModel);

    HttpResponse<String> post(Object object);

    HttpResponse<String> put(Object object);

    HttpResponse<String> delete(FindModel findModel);

    HttpResponse<String> deleteMultiple(ArrayList<FindModel> findModelList);
}
