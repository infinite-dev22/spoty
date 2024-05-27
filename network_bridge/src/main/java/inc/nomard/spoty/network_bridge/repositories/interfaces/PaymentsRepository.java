package inc.nomard.spoty.network_bridge.repositories.interfaces;

import inc.nomard.spoty.network_bridge.models.*;
import java.net.http.*;
import java.util.*;
import javafx.concurrent.*;

public interface PaymentsRepository {
    Task<HttpResponse<String>> cardPay(Object object);

    Task<HttpResponse<String>> mtnMomoPay(Object object);

    Task<HttpResponse<String>> airtelMomoPay(Object object);

    Task<HttpResponse<String>> startTrial(FindModel findModel);
}
