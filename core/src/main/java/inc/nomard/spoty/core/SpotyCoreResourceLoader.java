/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

package inc.nomard.spoty.core;

import javafx.fxml.FXMLLoader;

import java.io.InputStream;
import java.net.URL;

public class SpotyCoreResourceLoader {
    public SpotyCoreResourceLoader() {
    }

    public static FXMLLoader fxmlLoader(String path) {
        return new FXMLLoader(SpotyCoreResourceLoader.class.getResource(path));
    }

    public static URL loadURL(String path) {
        return SpotyCoreResourceLoader.class.getResource(path);
    }

    public static String load(String path) {
        return loadURL(path).toString();
    }

    public static InputStream loadStream(String name) {
        return SpotyCoreResourceLoader.class.getResourceAsStream(name);
    }
}