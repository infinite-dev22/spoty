package org.infinite.spoty.database.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.infinite.spoty.GlobalActions.fineDate;

@Entity
public class RequisitionMaster implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String ref;
    private Date date;
    @ManyToOne
    private User user_detail;
    @OneToOne
    private Supplier supplier;
    @ManyToOne
    private Branch branch;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "requisition")
    private List<RequisitionDetail> requisitionDetails = new LinkedList<>();
    private String shipVia;
    private String shipMethod;
    private String shippingTerms;
    private Date deliveryDate;
    private String notes;
    private String status;
    private double totalCost;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(name = "updated_by")
    private String updatedBy;

    public RequisitionMaster() {
    }

    public RequisitionMaster(Date date,
                             Supplier supplier,
                             Branch branch,
                             String shipVia,
                             String shipMethod,
                             String shippingTerms,
                             Date deliveryDate,
                             String notes,
                             String status,
                             double totalCost) {
        this.date = date;
        this.supplier = supplier;
        this.branch = branch;
        this.shipVia = shipVia;
        this.shipMethod = shipMethod;
        this.shippingTerms = shippingTerms;
        this.deliveryDate = deliveryDate;
        this.notes = notes;
        this.status = status;
        this.totalCost = totalCost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user_detail;
    }

    public void setUser(User user_detail) {
        this.user_detail = user_detail;
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

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public String getBranchName() {
        if (branch != null)
            return branch.getName();
        else
            return null;
    }

    public List<RequisitionDetail> getRequisitionDetails() {
        return requisitionDetails;
    }

    public void setRequisitionDetails(List<RequisitionDetail> requisitionDetails) {
        this.requisitionDetails = requisitionDetails;
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

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getSupplierName() {
        return (supplier != null) ? supplier.getName() : null;
    }

    public String getShipVia() {
        return shipVia;
    }

    public void setShipVia(String shipVia) {
        this.shipVia = shipVia;
    }

    public String getShipMethod() {
        return shipMethod;
    }

    public void setShipMethod(String shipMethod) {
        this.shipMethod = shipMethod;
    }

    public String getShippingTerms() {
        return shippingTerms;
    }

    public void setShippingTerms(String shippingTerms) {
        this.shippingTerms = shippingTerms;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public String getDeliveryLocaleDate() {
        return DateFormat.getDateInstance().format(deliveryDate);
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryDateString() {
        return fineDate(deliveryDate.toString()).toString();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getLocaleDate() {
        return DateFormat.getDateInstance().format(date);
    }
}
