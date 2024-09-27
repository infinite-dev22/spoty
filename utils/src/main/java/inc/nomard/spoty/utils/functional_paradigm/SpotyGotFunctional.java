package inc.nomard.spoty.utils.functional_paradigm;

import javafx.event.ActionEvent;

public class SpotyGotFunctional {
    @FunctionalInterface
    public interface ParameterlessConsumer {
        void run();
    }

    @FunctionalInterface
    public interface EventConsumer {
        void run(ActionEvent event);
    }

    @FunctionalInterface
    public interface MessageConsumer {
        void showMessage(String message);
    }
}
