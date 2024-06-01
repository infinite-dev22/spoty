package inc.nomard.spoty.network_bridge.repositories.interfaces;

import inc.nomard.spoty.network_bridge.models.*;
import java.net.http.*;
import java.util.concurrent.*;

public interface PaymentsRepository {
    CompletableFuture<HttpResponse<String>> cardPay(Object object);

    CompletableFuture<HttpResponse<String>> mtnMomoPay(Object object);

    CompletableFuture<HttpResponse<String>> airtelMomoPay(Object object);

    CompletableFuture<HttpResponse<String>> startTrial(FindModel findModel);
}
