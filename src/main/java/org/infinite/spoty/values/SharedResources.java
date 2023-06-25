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

package org.infinite.spoty.values;

import java.util.LinkedList;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class SharedResources {
  public static final LinkedList<Integer> PENDING_DELETES = new LinkedList<>();
  private static final IntegerProperty TEMP_ID = new SimpleIntegerProperty(-1);

  public static Integer getTempId() {
    return TEMP_ID.get();
  }

  public static void setTempId(int tempId) {
    SharedResources.TEMP_ID.set(tempId);
  }

  public static IntegerProperty tempIdProperty() {
    return TEMP_ID;
  }
}
