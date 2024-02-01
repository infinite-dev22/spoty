package org.infinite.spoty.data_source.repositories.interfaces;

import javafx.concurrent.Task;
import org.infinite.spoty.data_source.models.FindModel;
import org.infinite.spoty.data_source.models.SearchModel;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public interface SimpleRepository {
    Task<HttpResponse<String>> fetchAll();

    Task<HttpResponse<String>> fetch(FindModel findModel);

    Task<HttpResponse<String>> search(SearchModel searchModel);

    Task<HttpResponse<String>> post(Object object);

    Task<HttpResponse<String>> put(Object object);

    Task<HttpResponse<String>> delete(FindModel findModel);

    Task<HttpResponse<String>> deleteMultiple(ArrayList<FindModel> findModelList);
}
