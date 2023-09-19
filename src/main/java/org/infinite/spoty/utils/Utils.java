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

package org.infinite.spoty.utils;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.utils.ToggleButtonsUtil;
import java.io.Serializable;
import javafx.geometry.Pos;
import javafx.scene.control.ToggleGroup;

public class Utils implements Serializable {
  public Utils(ToggleGroup toggleGroup) {
    ToggleButtonsUtil.addAlwaysOneSelectedSupport(toggleGroup);
  }

  public static MFXButton createToggle(String icon, String text) {
    return createToggle(icon, text, 0);
  }

  private static MFXButton createToggle(String icon, String text, double rotate) {
    MFXIconWrapper wrapper = new MFXIconWrapper(icon, 24, 32);
    MFXButton toggleNode = new MFXButton(text, wrapper);
    toggleNode.setAlignment(Pos.CENTER_LEFT);
    toggleNode.setMaxWidth(Double.MAX_VALUE);
    if (rotate != 0) wrapper.getIcon().setRotate(rotate);
    return toggleNode;
  }
}
