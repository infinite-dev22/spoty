package inc.nomard.spoty.network_bridge.repositories.interfaces;

import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.network_bridge.models.SearchModel;
import javafx.concurrent.Task;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public interface CompanyRepository {
    Task<HttpResponse<String>> fetchAll();

    Task<HttpResponse<String>> fetch(FindModel findModel);

    Task<HttpResponse<String>> search(SearchModel searchModel);

    Task<HttpResponse<String>> post(Object object);

    Task<HttpResponse<String>> put(Object object);

    Task<HttpResponse<String>> delete(FindModel findModel);

    Task<HttpResponse<String>> deleteMultiple(ArrayList<FindModel> findModelList);
}