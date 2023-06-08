package org.infinite.spoty.database.models;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
public class AdjustmentMaster implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    private User user;
    private Date date;
    private String ref;
    @ManyToOne
    private Branch branch;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "adjustment")
    private List<AdjustmentDetail> adjustmentDetails = new LinkedList<>();
    private String notes;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(name = "updated_by")
    private String updatedBy;

    public AdjustmentMaster() {
    }

    public AdjustmentMaster(Branch branch,
                            String notes,
                            Date date) {
        this.date = date;
        this.branch = branch;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getLocaleDate() {
        return DateFormat.getDateInstance().format(date);
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

    public List<AdjustmentDetail> getAdjustmentDetails() {
        return adjustmentDetails;
    }

    public void setAdjustmentDetails(List<AdjustmentDetail> adjustmentDetails) {
        this.adjustmentDetails = adjustmentDetails;
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
