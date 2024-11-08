package inc.nomard.spoty.network_bridge.repositories.interfaces;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public interface TenantSettingRepository {

    CompletableFuture<HttpResponse<String>> fetch();

    CompletableFuture<HttpResponse<String>> post(Object object);

    CompletableFuture<HttpResponse<String>> put(Object object);

    CompletableFuture<HttpResponse<String>> delete();

    CompletableFuture<HttpResponse<String>> addReviewer(Object object);
    CompletableFuture<HttpResponse<String>> editReviewer(Object object);

    CompletableFuture<HttpResponse<String>> removeReviewer(Object object);
}
