package inc.nomard.spoty.network_bridge.repositories.interfaces;

import inc.nomard.spoty.network_bridge.models.*;
import java.net.http.*;

import java.util.*;
import java.util.concurrent.*;

public interface CompanyRepository {
    CompletableFuture<HttpResponse<String>> fetchAll(Integer pageNo, Integer pageSize);

    CompletableFuture<HttpResponse<String>> fetch(FindModel findModel);

    CompletableFuture<HttpResponse<String>> search(SearchModel searchModel);

    CompletableFuture<HttpResponse<String>> post(Object object);

    CompletableFuture<HttpResponse<String>> put(Object object);

    CompletableFuture<HttpResponse<String>> delete(FindModel findModel);

    CompletableFuture<HttpResponse<String>> deleteMultiple(ArrayList<FindModel> findModelList);
}