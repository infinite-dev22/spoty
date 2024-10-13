package inc.nomard.spoty.network_bridge.repositories.interfaces;

import inc.nomard.spoty.network_bridge.models.FindModel;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public interface PaymentsRepository {
    CompletableFuture<HttpResponse<String>> cardPay(Object object);

    CompletableFuture<HttpResponse<String>> mtnMomoPay(Object object);

    CompletableFuture<HttpResponse<String>> airtelMomoPay(Object object);

    CompletableFuture<HttpResponse<String>> startTrial(FindModel findModel);
}
