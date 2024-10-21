package inc.nomard.spoty.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpotyLogger {
    public static <T> void writeToFile(Throwable throwable, Class<T> currentClass) {
        log.error(throwable.getMessage(), throwable, currentClass);
    }
}
