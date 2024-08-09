package inc.nomard.spoty.network_bridge.repositories.interfaces;

import inc.nomard.spoty.network_bridge.models.*;
import java.io.*;
import java.net.http.*;

import java.util.*;
import java.util.concurrent.*;

public interface ProductRepository {
    CompletableFuture<HttpResponse<String>> fetchAll(Integer pageNo, Integer pageSize);

    CompletableFuture<HttpResponse<String>> fetch(FindModel findModel);

    CompletableFuture<HttpResponse<String>> search(SearchModel searchModel);

    CompletableFuture<HttpResponse<String>> stockAlert() throws IOException, InterruptedException;

    CompletableFuture<HttpResponse<String>> post(Object object, File imageFile);

    CompletableFuture<HttpResponse<String>> put(Object object, File imageFile);

    CompletableFuture<HttpResponse<String>> delete(FindModel findModel);

    CompletableFuture<HttpResponse<String>> deleteMultiple(ArrayList<FindModel> findModelList);
}
