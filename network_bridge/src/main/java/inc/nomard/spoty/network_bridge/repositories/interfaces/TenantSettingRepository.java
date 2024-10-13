package inc.nomard.spoty.network_bridge.repositories.interfaces;

import inc.nomard.spoty.network_bridge.models.FindModel;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public interface TenantSettingRepository {

    CompletableFuture<HttpResponse<String>> fetch();

    CompletableFuture<HttpResponse<String>> post(Object object);

    CompletableFuture<HttpResponse<String>> put(Object object);

    CompletableFuture<HttpResponse<String>> delete();
}
