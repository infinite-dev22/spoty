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

package org.infinite.spoty.components.navigation;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import org.jetbrains.annotations.Nullable;

record Nav(
    String title, @Nullable Node graphic, BorderPane view, @Nullable List<String> searchKeywords) {

  public static final Nav ROOT = new Nav("ROOT", null, null, null);

  public Nav {
    Objects.requireNonNull(title, "title");
    searchKeywords = Objects.requireNonNullElse(searchKeywords, Collections.emptyList());
  }

  public boolean isGroup() {
    return Objects.equals(view, null);
  }

  public boolean isMainPage() {
    return Objects.equals(view, null) && Objects.equals(graphic, null);
  }

  public boolean matches(String filter) {
    Objects.requireNonNull(filter);
    return contains(title, filter)
        || (searchKeywords != null
            && searchKeywords.stream().anyMatch(keyword -> contains(keyword, filter)));
  }

  private boolean contains(String text, String filter) {
    return text.toLowerCase().contains(filter.toLowerCase());
  }
}
