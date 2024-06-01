package inc.nomard.spoty.utils.functional_paradigm;

public class SpotyGotFunctional {
    @FunctionalInterface
    public static interface ParameterlessConsumer {
        void run();
    }

    @FunctionalInterface
    public static interface MessageConsumer {
        void showMessage(String message);
    }
}
