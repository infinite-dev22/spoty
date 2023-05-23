package org.infinite.spoty.database.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class PaymentPurchaseReturn implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    private User user;
    private Date date;
    private String ref;
    @ManyToOne
    private PurchaseReturnMaster purchaseReturn;
    private String paymentMethod;
    private double amount;
    private double change;
    private String notes;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(name = "updated_by")
    private String updatedBy;
    public PaymentPurchaseReturn(User user, Date date, String ref, PurchaseReturnMaster purchaseReturn, String paymentMethod, double amount, double change, String notes) {
        this.user = user;
        this.date = date;
        this.ref = ref;
        this.purchaseReturn = purchaseReturn;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.change = change;
        this.notes = notes;
    }

    public PaymentPurchaseReturn() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public PurchaseReturnMaster getPurchaseReturn() {
        return purchaseReturn;
    }

    public void setPurchaseReturn(PurchaseReturnMaster purchaseReturn) {
        this.purchaseReturn = purchaseReturn;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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