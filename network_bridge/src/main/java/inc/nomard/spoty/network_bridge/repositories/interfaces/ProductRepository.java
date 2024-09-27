package inc.nomard.spoty.network_bridge.repositories.interfaces;

import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.network_bridge.models.SearchModel;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public interface ProductRepository {
    CompletableFuture<HttpResponse<String>> fetchAll(Integer pageNo, Integer pageSize);

    CompletableFuture<HttpResponse<String>> fetchAllNonPaged();

    CompletableFuture<HttpResponse<String>> fetch(FindModel findModel);

    CompletableFuture<HttpResponse<String>> search(SearchModel searchModel);

    CompletableFuture<HttpResponse<String>> stockAlert() throws IOException, InterruptedException;

    CompletableFuture<HttpResponse<String>> post(Object object, File imageFile);

    CompletableFuture<HttpResponse<String>> put(Object object, File imageFile);

    CompletableFuture<HttpResponse<String>> delete(FindModel findModel);

    CompletableFuture<HttpResponse<String>> deleteMultiple(ArrayList<FindModel> findModelList);
}
