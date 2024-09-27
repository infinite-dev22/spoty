package inc.nomard.spoty.utils.exceptions;

import lombok.extern.java.Log;

@Log
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}