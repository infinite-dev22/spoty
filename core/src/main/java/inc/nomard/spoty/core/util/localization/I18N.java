package inc.nomard.spoty.core.util.localization;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.concurrent.Callable;

public class I18N {
    private static final ObjectProperty<Locale> locale = new SimpleObjectProperty<>();

    static {
        locale.addListener((invalidated) -> {
            Locale.setDefault(getLocale());
        });
    }

    public I18N() {
    }

    public static String get(String key, Object... args) {
        return MessageFormat.format(key.toLowerCase(Locale.getDefault()), args);
    }

    public static String getOrDefault(String key, Object... args) {
        try {
            return MessageFormat.format(key.toLowerCase(Locale.getDefault()), args);
        } catch (Exception var4) {
            return get(key, args);
        }
    }

    public static String getOrDefault(String key, String def, Object... args) {
        try {
            return MessageFormat.format(key.toLowerCase(Locale.getDefault()), args);
        } catch (Exception var5) {
            return def;
        }
    }

    public static StringBinding getBinding(String key, Object... args) {
        return Bindings.createStringBinding(() -> {
            return getOrDefault(key, args);
        }, new Observable[]{locale});
    }

    public static StringBinding getBinding(Callable<String> callable) {
        return Bindings.createStringBinding(callable, new Observable[]{locale});
    }

    public static Locale getLocale() {
        return (Locale) locale.get();
    }

    public static ObjectProperty<Locale> localeProperty() {
        return locale;
    }

    public static String getBundleBaseName() {
        return "io.github.palexdev.mfxlocalization.mfxlang";
    }
}