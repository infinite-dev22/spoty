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

package inc.normad.spoty.utils.navigation;

import javafx.geometry.Orientation;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class Spacer extends Region {

    /**
     * Creates a new horizontally oriented Spacer that expands to fill remaining space.
     */
    public Spacer() {
        this(Orientation.HORIZONTAL);
    }

    /**
     * Creates a new Spacer with the given orientation that expands to fill remaining space.
     *
     * @param orientation The orientation of the spacer.
     */
    public Spacer(Orientation orientation) {
        super();

        switch (orientation) {
            case HORIZONTAL -> HBox.setHgrow(this, Priority.ALWAYS);
            case VERTICAL -> VBox.setVgrow(this, Priority.ALWAYS);
        }
    }

    /**
     * Creates a new Spacer with the fixed size.
     *
     * @param size The size of the spacer.
     */
    public Spacer(double size) {
        this(size, Orientation.HORIZONTAL);
    }

    /**
     * Creates a new Spacer with the fixed size and orientation.
     *
     * @param size        The size of the spacer.
     * @param orientation The orientation of the spacer.
     */
    public Spacer(double size, Orientation orientation) {
        super();

        switch (orientation) {
            case HORIZONTAL -> {
                setMinWidth(size);
                setPrefWidth(size);
                setMaxWidth(size);
            }
            case VERTICAL -> {
                setMinHeight(size);
                setPrefHeight(size);
                setMaxHeight(size);
            }
        }
    }
}
