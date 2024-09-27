package inc.nomard.spoty.network_bridge.repositories.interfaces;

import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.network_bridge.models.SearchModel;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public interface SimpleRepository {
    CompletableFuture<HttpResponse<String>> fetchAll(Integer pageNo, Integer pageSize);

    CompletableFuture<HttpResponse<String>> fetch(FindModel findModel);

    CompletableFuture<HttpResponse<String>> search(SearchModel searchModel);

    CompletableFuture<HttpResponse<String>> post(Object object);

    CompletableFuture<HttpResponse<String>> put(Object object);

    CompletableFuture<HttpResponse<String>> delete(FindModel findModel);

    CompletableFuture<HttpResponse<String>> deleteMultiple(ArrayList<FindModel> findModelList);
}
