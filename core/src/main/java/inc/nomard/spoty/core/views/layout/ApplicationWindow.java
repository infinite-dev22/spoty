package inc.nomard.spoty.core.views.layout;

import javafx.scene.*;

public interface ApplicationWindow {
    int MAX_WIDTH = 32767;
    int MAX_HEIGHT = 32767;
    int HGAP_20 = 20;
    int HGAP_30 = 30;
    int VGAP_10 = 10;
    int VGAP_20 = 20;

    Parent getView();

    void dispose();

    void setLightMode();

    void setDarkMode();

    void setMorph(Boolean morph);
}
