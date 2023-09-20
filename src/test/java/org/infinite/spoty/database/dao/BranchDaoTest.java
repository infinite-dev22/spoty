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

import static org.junit.jupiter.api.Assertions.*;

import org.infinite.spoty.viewModels.BranchViewModel;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BranchDaoTest {

  @Test
  @Order(1)
  void saveBranch() {
    try {
    BranchViewModel.setName("Branch One");
    BranchViewModel.setCity("Kampala");
    BranchViewModel.setPhone("+1234567890");
    BranchViewModel.setEmail("branchone@email.com");
    BranchViewModel.setTown("Bwaise");
    BranchViewModel.setZipcode("617867838");

    BranchViewModel.saveBranch();

    BranchViewModel.setName("Branch Two");
    BranchViewModel.setCity("Arkansas City");
    BranchViewModel.setPhone("+0987654321");
    BranchViewModel.setEmail("branchtwo@email.com");
    BranchViewModel.setTown("Ark Town");
    BranchViewModel.setZipcode("617863228");

    BranchViewModel.saveBranch();
    } catch (Exception e) {
      e.printStackTrace();
      throw new AssertionError();
    }
  }

  @Test
  @Order(2)
  void updateBranch() throws SQLException {
    BranchViewModel.setName("Branch 1");
    BranchViewModel.setPhone("+2567123456789");
    BranchViewModel.setEmail("branch1@email.com");
    BranchViewModel.setTown("Kisaasi");
    BranchViewModel.setZipcode("76782");

    BranchViewModel.saveBranch();
  }

  @Test
  @Order(3)
  void findBranch() throws SQLException {
    BranchViewModel.getItem(2);
    System.out.println(BranchViewModel.getName());
    assertEquals(BranchViewModel.getTown(), "");
  }

  @Test
  @Order(4)
  void getBranch() throws SQLException {
    BranchViewModel.getAllBranches();
    BranchViewModel.branchesList.forEach(e -> System.out.println(e.getName()));
    assertEquals(BranchViewModel.branchesList.size(), 2);
  }

  @Test
  @Order(5)
  void deleteBranch() throws SQLException {
    BranchViewModel.deleteItem(2);
    BranchViewModel.deleteItem(1);
  }
}
