package inc.nomard.spoty.network_bridge.repositories.interfaces;

import inc.nomard.spoty.network_bridge.models.FindModel;
import javafx.concurrent.Task;

import java.net.http.HttpResponse;

public interface RoleRepository {
    // Roles
    Task<HttpResponse<String>> fetchAllRoles();

    Task<HttpResponse<String>> fetchRole(FindModel findModel);

    Task<HttpResponse<String>> postRole(Object object);

    Task<HttpResponse<String>> putRole(Object object);

    Task<HttpResponse<String>> deleteRole(FindModel findModel);

    // Permissions
    Task<HttpResponse<String>> fetchAllPermissions();

    Task<HttpResponse<String>> fetchPermission(FindModel findModel);

    Task<HttpResponse<String>> postPermission(Object object);

    Task<HttpResponse<String>> putPermission(Object object);

    Task<HttpResponse<String>> deletePermission(FindModel findModel);
}
