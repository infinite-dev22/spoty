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

package org.infinite.spoty.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.infinite.spoty.database.models.Role;
import org.infinite.spoty.models.QuickStats;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SampleData implements Serializable {
    public static List<QuickStats> quickStatsSampleData() {
        List<QuickStats> samples = new ArrayList<>();

        QuickStats quickStats = new QuickStats();
        quickStats.setTitle("2050");
        quickStats.setSubtitle("Total Orders");
        samples.add(quickStats);

        quickStats = new QuickStats();
        quickStats.setTitle("3250");
        quickStats.setSubtitle("Total Expense");
        samples.add(quickStats);

        quickStats = new QuickStats();
        quickStats.setTitle("87.5%");
        quickStats.setSubtitle("Total Revenue");
        samples.add(quickStats);

        quickStats = new QuickStats();
        quickStats.setTitle("2550");
        quickStats.setSubtitle("New Users");
        samples.add(quickStats);

        return samples;
    }

    public static ObservableList<Role> roleMasterSampleData() {
        ObservableList<Role> roleMasterList = FXCollections.observableArrayList();

        Role roleMaster = new Role();
        roleMaster.setName("Owner");
        roleMaster.setDescription("Administrator role");
        roleMasterList.add(roleMaster);

        return roleMasterList;
    }
}
