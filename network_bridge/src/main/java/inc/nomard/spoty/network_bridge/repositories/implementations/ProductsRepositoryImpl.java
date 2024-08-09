package inc.nomard.spoty.network_bridge.repositories.implementations;

import com.google.gson.*;
import inc.nomard.spoty.network_bridge.auth.*;
import inc.nomard.spoty.network_bridge.end_points.*;
import inc.nomard.spoty.network_bridge.models.*;
import inc.nomard.spoty.network_bridge.repositories.interfaces.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.adapters.*;
import java.io.*;
import java.net.*;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.time.*;

import java.util.*;
import java.util.concurrent.*;
import lombok.*;
import lombok.extern.java.*;
import org.apache.http.*;
import org.apache.http.entity.*;
import org.apache.http.entity.mime.*;
import org.apache.http.entity.mime.content.*;

@Log
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
    @SneakyThrows
    public CompletableFuture<HttpResponse<String>> post(Object object, File imageFile) {
        /*
         * Create a Multipart request body with MultipartEntityBuilder.
         */
        HttpEntity httpEntity = MultipartEntityBuilder.create()
                .addPart("product", new StringBody(gson.toJson(object), ContentType.APPLICATION_JSON))
                .addBinaryBody("file", imageFile)
                .build();
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
                .uri(URI.create(EndPoints.Products.addProduct))
                .header("Authorization", authToken)
                .header("Content-Type", httpEntity.getContentType().getValue())
                .method("POST", HttpRequest.BodyPublishers.ofInputStream(() -> Channels.newInputStream(pipe.source())))
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    @Override
    @SneakyThrows
    public CompletableFuture<HttpResponse<String>> put(Object object, File imageFile) {
        /*
         * Create a Multipart request body with MultipartEntityBuilder.
         */
        HttpEntity httpEntity = MultipartEntityBuilder.create()
                .addPart("product", new StringBody(gson.toJson(object), ContentType.APPLICATION_JSON))
                .addBinaryBody("file", imageFile)
                .build();
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
