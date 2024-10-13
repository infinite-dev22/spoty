package inc.nomard.spoty.network_bridge.repositories.interfaces;

import inc.nomard.spoty.network_bridge.models.FindModel;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public interface RoleRepository {
    // Roles
    CompletableFuture<HttpResponse<String>> fetchAllRoles(Integer pageNo, Integer pageSize);

    CompletableFuture<HttpResponse<String>> fetchRole(FindModel findModel);

    CompletableFuture<HttpResponse<String>> postRole(Object object);

    CompletableFuture<HttpResponse<String>> putRole(Object object);

    CompletableFuture<HttpResponse<String>> deleteRole(FindModel findModel);

    // Permissions
    CompletableFuture<HttpResponse<String>> fetchAllPermissions();

    CompletableFuture<HttpResponse<String>> fetchPermission(FindModel findModel);

    CompletableFuture<HttpResponse<String>> postPermission(Object object);

    CompletableFuture<HttpResponse<String>> putPermission(Object object);

    CompletableFuture<HttpResponse<String>> deletePermission(FindModel findModel);
}
