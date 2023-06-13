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

package org.infinite.spoty.database.dao;

import org.infinite.spoty.database.models.Branch;
import org.junit.jupiter.api.*;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BranchDaoTest {

    @Test
    @Order(1)
    void saveBranch() {
        var branch1 = new Branch("Branch One",
                "Kampala", "+1234567890",
                "branchone@email.com", "Uganda", "617867838");
        branch1.setCreatedAt(new Date());
        branch1.setCreatedBy("Test User One");
        BranchDao.saveBranch(branch1);
        assertEquals(branch1.getId(), 1);

        var branch2 = new Branch("Branch Two",
                "Akansas City", "+0987654321",
                "branchtwo@email.com", "USA", "617863228");
        branch2.setCreatedAt(new Date());
        branch2.setCreatedBy("Test User Two");
        BranchDao.saveBranch(branch2);
        Assertions.assertEquals(branch2.getName(), "Branch Two");
    }

    @Test
    @Order(2)
    void updateBranch() {
        var branch = new Branch("Branch 1",
                "Kampala", "+2567123456789",
                "branch1@email.com", "Uganda", "76782");
        branch.setUpdatedAt(new Date());
        branch.setUpdatedBy("Test User");
        BranchDao.updateBranch(branch, 1);
        assertEquals(branch.getPhone(), "+2567123456789");
    }

    @Test
    @Order(3)
    void findBranch() {
        var obj = BranchDao.findBranch(2);
        assertEquals(obj.getTown(), "USA");
    }

    @Test
    @Order(4)
    void getBranch() {
        var obj = BranchDao.getBranches();
        assertEquals(obj.size(), 2);
    }

    @Test
    @Order(5)
    void deleteBranch() {
        var obj1 = BranchDao.deleteBranch(2);
        assertEquals(obj1, 1);

        var obj2 = BranchDao.deleteBranch(1);
        assertEquals(obj2, 1);
    }
}