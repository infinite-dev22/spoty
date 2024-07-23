package inc.nomard.spoty.utils.exceptions;

import lombok.extern.java.Log;

import javafx.util.Duration;
@Log
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}