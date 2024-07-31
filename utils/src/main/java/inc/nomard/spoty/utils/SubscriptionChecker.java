package inc.nomard.spoty.utils;

public class SubscriptionChecker {
//    private static final String SUBSCRIPTION_STATUS_URL = "http://your-api-url/subscription/status?email=";
//    private static final int CHECK_INTERVAL = 60000; // Check every 60 seconds
//
//    private String userEmail;
//    private Timer timer;
//    private ObjectMapper objectMapper;
//
//    public SubscriptionChecker(String userEmail) {
//        this.userEmail = userEmail;
//        this.objectMapper = new ObjectMapper();
//        this.timer = new Timer(true);
//    }
//
//    public void startChecking() {
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                checkSubscriptionStatus();
//            }
//        }, 0, CHECK_INTERVAL);
//    }
//
//    private void checkSubscriptionStatus() {
//        try {
//            URL url = new URL(SUBSCRIPTION_STATUS_URL + userEmail);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setRequestProperty("Content-Type", "application/json");
//
//            int responseCode = connection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                JsonNode response = objectMapper.readTree(connection.getInputStream());
//                String status = response.get("status").asText();
//                String message = response.get("message").asText();
//
//                Platform.runLater(() -> showAlert(status, message));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void showAlert(String status, String message) {
//        AlertType alertType;
//        switch (status) {
//            case "gracePeriod":
//            case "aboutToExpire":
//                alertType = AlertType.WARNING;
//                break;
//            case "expired":
//                alertType = AlertType.ERROR;
//                break;
//            default:
//                alertType = AlertType.INFORMATION;
//                break;
//        }
//
//        Alert alert = new Alert(alertType);
//        alert.setTitle("Subscription Status");
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    public void stopChecking() {
//        timer.cancel();
//    }
}
