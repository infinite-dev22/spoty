package org.infinite.spoty.database.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class LeaveType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private Date createdAt = null;
    private String created_by = null;
    private Date updatedAt = null;
    private String updatedBy = null;

    public LeaveType(String name,
                     Date createdAt,
                     String created_by,
                     Date updatedAt,
                     String updatedBy) {
        this.name = name;
        this.createdAt = createdAt;
        this.created_by = created_by;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    public LeaveType() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdated_by() {
        return updatedBy;
    }

    public void setUpdated_by(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
