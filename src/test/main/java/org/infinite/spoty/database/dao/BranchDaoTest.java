package org.infinite.spoty.database.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import org.infinite.spoty.data_source.dtos.Branch;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BranchDaoTest {

    @Test
    @Order(1)
    void saveBranch() {
        var branch1 = new Branch("Branch One",
                "Kampala", "+1234567890",
                "branchone@email.com", "Uganda", "617867838");
        branch1.setCreatedAt(Date.now());
        branch1.setCreatedBy("Test User One");
        BranchDao.saveBranch(branch1);
        assertEquals(branch1.getId(), 1);

        var branch2 = new Branch("Branch Two",
                "Akansas City", "+0987654321",
                "branchtwo@email.com", "USA", "617863228");
        branch2.setCreatedAt(Date.now());
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
        branch.setUpdatedAt(Date.now());
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