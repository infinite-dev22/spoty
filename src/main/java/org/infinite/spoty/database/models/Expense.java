package org.infinite.spoty.database.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

// TODO: Remove uUser Property.

@Entity
public class Expense implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private Date date;
    private String ref;
    @ManyToOne
    private User user;
    @ManyToOne
    private ExpenseCategory expenseCategory;
    @ManyToOne
    private Branch branch;
    private String details;
    private double amount;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(name = "updated_by")
    private String updatedBy;

    public Expense(Date date,
                   ExpenseCategory expenseCategory,
                   Branch branch,
                   String details,
                   double amount) {
        this.date = date;
        this.expenseCategory = expenseCategory;
        this.branch = branch;
        this.details = details;
        this.amount = amount;
    }

    public Expense(Date date,
                   ExpenseCategory expenseCategory,
                   double amount) {
        this.date = date;
        this.expenseCategory = expenseCategory;
        this.amount = amount;
    }

    public Expense() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ExpenseCategory getExpenseCategory() {
        return expenseCategory;
    }

    public String getExpenseCategoryName() {
        return expenseCategory.getName();
    }

    public void setExpenseCategory(ExpenseCategory expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public Branch getBranch() {
        return branch;
    }

    public String getBranchName() {
        return (branch != null) ? branch.getName() : null;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
