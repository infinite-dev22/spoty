package inc.nomard.spoty.utils.functional_paradigm;

import javafx.event.*;

public class SpotyGotFunctional {
    @FunctionalInterface
    public static interface ParameterlessConsumer {
        void run();
    }

    @FunctionalInterface
    public static interface EventConsumer {
        void run(ActionEvent event);
    }

    @FunctionalInterface
    public static interface MessageConsumer {
        void showMessage(String message);
    }
}
