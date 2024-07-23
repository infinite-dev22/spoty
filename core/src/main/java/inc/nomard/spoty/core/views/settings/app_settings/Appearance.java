package inc.nomard.spoty.core.views.settings.app_settings;

import atlantafx.base.controls.*;
import atlantafx.base.theme.*;
import inc.nomard.spoty.core.*;
import inc.nomard.spoty.core.views.components.label_components.controls.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.mfxcore.controls.Label;
import java.util.*;
import java.util.prefs.*;
import java.util.stream.*;
import javafx.collections.*;
import javafx.css.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import lombok.extern.java.*;

@Log
public class Appearance extends VBox {
    private final String darkTheme = SpotyCoreResourceLoader.load("images/dark.png");
    private final String lightTheme = SpotyCoreResourceLoader.load("images/light.png");
    private final String systemTheme = SpotyCoreResourceLoader.load("images/dark_or_light.png");
    LabeledComboBox<String> languageCombo = new LabeledComboBox<>();
    private Preferences preferences;

    public Appearance() {
        initApplicationPreferences();
        init();
    }

    private void initApplicationPreferences() {
        if (Objects.equals(preferences, null)) {
            preferences = Preferences.userRoot().node(this.getClass().getName());
        }
    }

    private void setApplicationPreferences() {
//        defaultCurrency
//                .valueProperty()
//                .addListener(
//                        (observable, oldValue, newValue) -> {
//                            preferences.putLong("default_currency", newValue.getId());
//                            try {
//                                preferences.flush();
//                            } catch (BackingStoreException e) {
//                                SpotyLogger.writeToFile(e, this.getClass());
//                            }
//                        });
        languageCombo
                .valueProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            preferences.put("default_language", newValue);
                            try {
                                preferences.flush();
                            } catch (BackingStoreException e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                        });
//        defaultBranch
//                .valueProperty()
//                .addListener(
//                        (observable, oldValue, newValue) -> {
//                            preferences.putLong("default_branch", newValue.getId());
//                            try {
//                                preferences.flush();
//                            } catch (BackingStoreException e) {
//                                SpotyLogger.writeToFile(e, this.getClass());
//                            }
//                        });
//        defaultEmail
//                .textProperty()
//                .addListener(
//                        (observable, oldValue, newValue) -> {
//                            preferences.put("branch_email", newValue);
//                            try {
//                                preferences.flush();
//                            } catch (BackingStoreException e) {
//                                SpotyLogger.writeToFile(e, this.getClass());
//                            }
//                        });
//        companyName
//                .textProperty()
//                .addListener(
//                        (observable, oldValue, newValue) -> {
//                            preferences.put("company_name", newValue);
//                            try {
//                                preferences.flush();
//                            } catch (BackingStoreException e) {
//                                SpotyLogger.writeToFile(e, this.getClass());
//                            }
//                        });
//        companyPhone
//                .textProperty()
//                .addListener(
//                        (observable, oldValue, newValue) -> {
//                            preferences.put("company_phone", newValue);
//                            try {
//                                preferences.flush();
//                            } catch (BackingStoreException e) {
//                                SpotyLogger.writeToFile(e, this.getClass());
//                            }
//                        });
//        branchAddress
//                .textProperty()
//                .addListener(
//                        (observable, oldValue, newValue) -> {
//                            preferences.put("branch_address", newValue);
//                            try {
//                                preferences.flush();
//                            } catch (BackingStoreException e) {
//                                SpotyLogger.writeToFile(e, this.getClass());
//                            }
//                        });
//        invoiceFooter
//                .selectedProperty()
//                .addListener(
//                        (observable, oldValue, newValue) -> {
//                            preferences.putBoolean("invoice_footer", newValue);
//                            try {
//                                preferences.flush();
//                            } catch (BackingStoreException e) {
//                                SpotyLogger.writeToFile(e, this.getClass());
//                            }
//                        });
    }

    private VBox buildHeaderText() {
        var vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(20, 0, 20, 0));
        var titleLbl = new Label("Appearance");
        titleLbl.getStyleClass().add("header-title");
        var descriptionLbl = new Label("Change how your app looks and feels on your computer");
        descriptionLbl.getStyleClass().add(Styles.TEXT_SUBTLE);

        vbox.getChildren().addAll(titleLbl, descriptionLbl);

        return vbox;
    }

    private VBox buildSubHeaderText(String title, String description) {
        var vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setMinWidth(300);
        vbox.setPrefWidth(400);
        vbox.setMaxWidth(500);
        var titleLbl = new Label(title);
        titleLbl.getStyleClass().add("sub-header-title");
        var descriptionLbl = new Label(description);
        descriptionLbl.getStyleClass().add(Styles.TEXT_SUBTLE);

        vbox.getChildren().addAll(titleLbl, descriptionLbl);

        return vbox;
    }

    private ToggleButton buildThemeItem(ToggleGroup toggleGroup, String imageUrl, String label) {
        final PseudoClass SELECTED = PseudoClass.getPseudoClass("selected");

        var vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setAlignment(Pos.TOP_CENTER);
        var vbox1 = new VBox();
        vbox1.setSpacing(10);
        vbox1.getStyleClass().add("theme-item");
        vbox1.setPadding(new Insets(5, 5, 0, 5));
        var labelLbl = new Label(label);
        labelLbl.getStyleClass().add("sub-header-label");

        vbox1.getChildren().add(getThemeImage(imageUrl));
        vbox.getChildren().addAll(vbox1, labelLbl);

        var toggle = new ToggleButton();
        toggle.setGraphic(vbox);
        toggle.getStyleClass().add("rounded");
        toggle.setPadding(new Insets(5, 5, 0, 5));
        toggle.setToggleGroup(toggleGroup);

        toggle.selectedProperty().addListener(
                (observableValue, aBoolean, t1) -> vbox1.pseudoClassStateChanged(SELECTED, t1));

        return toggle;
    }

    private ImageView getThemeImage(String image) {
        var themeItemImage = new Image(image, 150, 200, true, false);
        var themeItemImageView = new ImageView(themeItemImage);
        themeItemImageView.setCache(true);
        themeItemImageView.setCacheHint(CacheHint.SPEED);
        return themeItemImageView;
    }

    private HBox buildThemeSettings() {
        var hbox = new HBox();
        hbox.setSpacing(20);
        hbox.setPadding(new Insets(20, 0, 20, 0));
        var toggleGroup = new ToggleGroup();
        var systemDefaultToggle =
                buildThemeItem(toggleGroup, systemTheme, "System preference");
        var lightThemeToggle =
                buildThemeItem(toggleGroup, lightTheme, "Light");
        var darkThemeToggle =
                buildThemeItem(toggleGroup, darkTheme, "Dark");

        toggleGroup.selectToggle(lightThemeToggle);

        hbox.getChildren().addAll(
                buildSubHeaderText("Interface theme", "Select or customize your UI theme"),
                systemDefaultToggle,
                lightThemeToggle,
                darkThemeToggle
        );

        return hbox;
    }

    private HBox buildAccentSettings() {
        var hbox = new HBox();
        hbox.setSpacing(20);
        hbox.setPadding(new Insets(20, 0, 20, 0));
        var hbox1 = new HBox();
        hbox1.setSpacing(10);
        hbox1.setAlignment(Pos.BOTTOM_LEFT);
        hbox1.setPadding(new Insets(20, 0, 20, 0));
        var toggleGroup = new ToggleGroup();

        var toggle1 =
                buildAccentItem(toggleGroup, "#0875f5");
        toggleGroup.selectToggle(toggle1);

        hbox1.getChildren().addAll(
                toggle1,
                buildAccentItem(toggleGroup, "#e94d4d"),
                buildAccentItem(toggleGroup, "#c1f651"),
                buildAccentItem(toggleGroup, "#5d5afa"),
                buildAccentItem(toggleGroup, "#f767e8"),
                buildAccentItem(toggleGroup, "#ce4d4d"),
                buildAccentItem(toggleGroup, "#898282"),
                buildAccentItem(toggleGroup, "#4d4d4d"),
                new Label("Custom")
        );

        hbox.getChildren().addAll(
                buildSubHeaderText("Interface accent", "Select or customize your UI primary action and active color"),
                hbox1
        );

        return hbox;
    }

    private HBox buildFontSettings() {
        var hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(20, 0, 20, 0));
        var fontCombo = new LabeledComboBox<>();
        fontCombo.setLabel("Font type");
        fontCombo.setPrefWidth(300);
        var hbox1 = new HBox();
        hbox1.setAlignment(Pos.BOTTOM_LEFT);
        hbox1.getChildren().add(fontCombo);

        hbox.getChildren().addAll(
                buildSubHeaderText("App Font", "Select or customize your font type"),
                hbox1
        );

        return hbox;
    }

    private ToggleButton buildAccentItem(ToggleGroup toggleGroup, String color) {
        final PseudoClass SELECTED = PseudoClass.getPseudoClass("selected");

        var circle = new Circle(175, 100, 15);
        circle.setFill(Color.web(color));

        var toggle = new ToggleButton();
        toggle.setGraphic(circle);
        toggle.getStyleClass().add("rounded-accent-border");
        toggle.setPadding(new Insets(5, 5, 5, 5));
        toggle.setToggleGroup(toggleGroup);

        toggle.selectedProperty().addListener(
                (observableValue, aBoolean, t1) -> toggle.pseudoClassStateChanged(SELECTED, t1));

        return toggle;
    }

    private HBox buildSidebarSettings() {
        var hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(20, 0, 20, 0));
        var toggle = new ToggleSwitch();
        var hbox1 = new HBox();
        hbox1.setAlignment(Pos.BOTTOM_LEFT);
        hbox1.getChildren().add(toggle);

        hbox.getChildren().addAll(
                buildSubHeaderText("Transparent sidebar", "Make the desktop sidebar transparent"),
                hbox1
        );

        return hbox;
    }

    private HBox buildLanguageSettings() {
        var languageSet = Arrays.stream(Locale.getISOLanguages())
                .map(Locale::new)
                .map(Locale::getDisplayLanguage)
                .collect(Collectors.toCollection(TreeSet::new));
        var languageList = new ArrayList<>(languageSet.stream().toList());
        var localeList = FXCollections.observableArrayList(languageList);

        var hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(20, 0, 20, 0));
        languageCombo.setLabel("Language");
        languageCombo.setPrefWidth(300);
        languageCombo.setItems(localeList);
        languageCombo.setValue(preferences.get(
                "default_language",
                "English"
        ));
        var hbox1 = new HBox();
        hbox1.setAlignment(Pos.BOTTOM_LEFT);
        hbox1.getChildren().add(languageCombo);

        hbox.getChildren().addAll(
                buildSubHeaderText("App Language", "Select or customize your display language"),
                hbox1
        );

        return hbox;
    }

    private void init() {
        setApplicationPreferences();

        var separator1 = new Separator(Orientation.HORIZONTAL);
        separator1.setPrefWidth(200);
        var separator2 = new Separator(Orientation.HORIZONTAL);
        separator2.setPrefWidth(200);
        var separator3 = new Separator(Orientation.HORIZONTAL);
        separator3.setPrefWidth(200);
        var separator4 = new Separator(Orientation.HORIZONTAL);
        separator4.setPrefWidth(200);
        var separator5 = new Separator(Orientation.HORIZONTAL);
        separator5.setPrefWidth(200);

        this.setPadding(new Insets(0, 20, 0, 20));
        this.getChildren().addAll(
                buildHeaderText(),
                separator1,
                buildThemeSettings(),
                separator2,
                buildAccentSettings(),
                separator3,
                buildFontSettings(),
                separator4,
                buildSidebarSettings(),
                separator5,
                buildLanguageSettings()
        );
    }
}
