package org.infinite.spoty;

import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import io.github.palexdev.materialfx.utils.ToggleButtonsUtil;
import javafx.geometry.Pos;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

import java.io.Serializable;

public class Utils implements Serializable {

    private static ToggleGroup toggleGroup = new ToggleGroup();

    public Utils(ToggleGroup toggleGroup) {
        Utils.toggleGroup = toggleGroup;
        ToggleButtonsUtil.addAlwaysOneSelectedSupport(toggleGroup);
    }

    public static ToggleButton createToggle(String icon, String text) {
        return createToggle(icon, text, 0);
    }

    private static ToggleButton createToggle(String icon, String text, double rotate) {
        MFXIconWrapper wrapper = new MFXIconWrapper(icon, 24, 32);
        MFXRectangleToggleNode toggleNode = new MFXRectangleToggleNode(text, wrapper);
        toggleNode.setAlignment(Pos.CENTER_LEFT);
        toggleNode.setMaxWidth(Double.MAX_VALUE);
        toggleNode.setToggleGroup(toggleGroup);
        if (rotate != 0) wrapper.getIcon().setRotate(rotate);
        return toggleNode;
    }
}

