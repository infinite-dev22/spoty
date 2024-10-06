package inc.nomard.spoty.network_bridge.repositories.implementations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import inc.nomard.spoty.network_bridge.auth.ProtectedGlobals;
import inc.nomard.spoty.network_bridge.end_points.EndPoints;
import inc.nomard.spoty.network_bridge.models.FindModel;
import inc.nomard.spoty.network_bridge.models.SearchModel;
import inc.nomard.spoty.network_bridge.repositories.interfaces.ProductRepository;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.adapters.LocalDateTimeTypeAdapter;
import inc.nomard.spoty.utils.adapters.LocalDateTypeAdapter;
import inc.nomard.spoty.utils.adapters.LocalTimeTypeAdapter;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.channels.Channels;
import java.nio.channels.Pipe;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Log4j2
public class ProductsRepositoryImpl extends ProtectedGlobals implements ProductRepository {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class,
                    new LocalDateTypeAdapter())
            .registerTypeAdapter(LocalTime.class,
                    new LocalTimeTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class,
                    new LocalDateTimeTypeAdapter())
            .create();

    @Override
    public CompletableFuture<HttpResponse<String>> fetchAll(Integer pageNo, Integer pageSize) {
        if (pageNo == null) {
            pageNo = 0;
        }
        if (pageSize == null) {
            pageSize = 50;
        }

        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Products.allProducts + "?pageNo=" + pageNo + "&pageSize=" + pageSize))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> fetchAllNonPaged() {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Products.allProductsNonPaged))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> fetch(FindModel findModel) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Products.productById))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModel)))
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> search(SearchModel searchModel) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Products.searchProducts))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.ofString(new Gson().toJson(searchModel)))
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> stockAlert() {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Products.productsStockAlert))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> post(Object object, File imageFile) {
        /*
         * Create a Multipart request body with MultipartEntityBuilder.
         */
        HttpEntity httpEntity = MultipartEntityBuilder.create()
                .addPart("product", new StringBody(gson.toJson(object), ContentType.APPLICATION_JSON))
                .addBinaryBody("file", imageFile)
                .build();

        try {
            /*
             * Use pipeline streams to write the encoded data directly to the network
             * instead of caching it in memory. Because Multipart request bodies contain
             * files, they can cause memory overflows if cached in memory.
             */
            var pipe = Pipe.open();
            /* Pipeline streams must be used in a multi-threaded environment. Using one
             * thread for simultaneous reads and writes can lead to deadlocks.
             */
            new Thread(() -> {
                try (OutputStream outputStream = Channels.newOutputStream(pipe.sink())) {
                    // Write the encoded data to the pipeline.
                    httpEntity.writeTo(outputStream);
                } catch (IOException e) {
                    SpotyLogger.writeToFile(e, ProductsRepositoryImpl.class);
                }

            }).start();

            var request = HttpRequest.newBuilder()
                    .uri(URI.create(EndPoints.Products.addProduct))
                    .header("Authorization", authToken)
                    .header("Content-Type", httpEntity.getContentType().getValue())
                    .method("POST", HttpRequest.BodyPublishers.ofInputStream(() -> Channels.newInputStream(pipe.source())))
                    .build();

            return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<HttpResponse<String>> put(Object object, File imageFile) {
        /*
         * Create a Multipart request body with MultipartEntityBuilder.
         */
        HttpEntity httpEntity = MultipartEntityBuilder.create()
                .addPart("product", new StringBody(gson.toJson(object), ContentType.APPLICATION_JSON))
                .addBinaryBody("file", imageFile)
                .build();

        try {
            /*
             * Use pipeline streams to write the encoded data directly to the network
             * instead of caching it in memory. Because Multipart request bodies contain
             * files, they can cause memory overflows if cached in memory.
             */
            Pipe pipe = Pipe.open();
            /* Pipeline streams must be used in a multi-threaded environment. Using one
             * thread for simultaneous reads and writes can lead to deadlocks.
             */
            new Thread(() -> {
                try (OutputStream outputStream = Channels.newOutputStream(pipe.sink())) {
                    // Write the encoded data to the pipeline.
                    httpEntity.writeTo(outputStream);
                } catch (IOException e) {
                    SpotyLogger.writeToFile(e, ProductsRepositoryImpl.class);
                }

            }).start();

            var request = HttpRequest.newBuilder()
                    .uri(URI.create(EndPoints.Products.updateProduct))
                    .header("Authorization", authToken)
                    .header("Content-Type", httpEntity.getContentType().getValue())
                    .method("PUT", HttpRequest.BodyPublishers.ofInputStream(() -> Channels.newInputStream(pipe.source())))
                    .build();

            return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<HttpResponse<String>> delete(FindModel findModel) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Products.deleteProduct))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModel)))
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public CompletableFuture<HttpResponse<String>> deleteMultiple(ArrayList<FindModel> findModelList) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(EndPoints.Products.deleteProducts))
                .header("Authorization", authToken)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(new Gson().toJson(findModelList)))
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }
}
