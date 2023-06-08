package org.infinite.spoty.database.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
public class TransferMaster implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private User user;
    private String ref;
    private Date date;
    @ManyToOne
    private Branch fromBranch;
    @ManyToOne
    private Branch toBranch;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transfer")
    private List<TransferDetail> transferDetails;
    private String shipping;
    private double total;
    private String status;
    private User approvedBy;
    private User receivedBy;
    private Date approvalDate;
    private Date receiveDate;
    private String notes;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(name = "updated_by")
    private String updatedBy;

    public TransferMaster() {
    }

    public TransferMaster(Date date,
                          Branch fromBranch,
                          Branch toBranch,
                          double total,
                          String status,
                          String notes) {
        this.date = date;
        this.fromBranch = fromBranch;
        this.toBranch = toBranch;
        this.total = total;
        this.status = status;
        this.notes = notes;
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

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Branch getFromBranch() {
        return fromBranch;
    }

    public String  getFromBranchName() {
        return (fromBranch != null) ? fromBranch.getName() : null;
    }

    public void setFromBranch(Branch fromBranch) {
        this.fromBranch = fromBranch;
    }

    public Branch getToBranch() {
        return toBranch;
    }

    public String  getToBranchName() {
        return (toBranch != null) ? toBranch.getName() : null;
    }

    public void setToBranch(Branch toBranch) {
        this.toBranch = toBranch;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<TransferDetail> getTransferDetails() {
        return transferDetails;
    }

    public void setTransferDetails(List<TransferDetail> transferDetails) {
        this.transferDetails = transferDetails;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public User getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(User receivedBy) {
        this.receivedBy = receivedBy;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
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
