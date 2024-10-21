package inc.nomard.spoty.utils;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

@Slf4j
public class SpotyImageUtils {
    public static File compress(File file) throws IOException {
        var output = Thumbnails.of(file).size(160, 160);
        return output.asFiles(Rename.PREFIX_HYPHEN_THUMBNAIL).getFirst();
    }

    public static File getFileFromResource(URL resource) throws URISyntaxException {
        if (resource == null) {
            log.error("File not found!");
            throw new RuntimeException("file not found!");
        } else {
            return new File(resource.toURI());
        }

    }
}
