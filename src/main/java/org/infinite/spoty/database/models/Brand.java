package org.infinite.spoty.database.models;

import javax.persistence.*;
import java.sql.Blob;
import java.util.Date;

@Entity
@Table(name = "brand")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String description;
    private Blob image;
    private Date createdAt;
    private String createdBy;
    private Date updatedAt;
    private String updatedBy;

    public Brand() {
    }

    public Brand(String name,
                 String description,
                 Blob image,
                 Date createdAt,
                 String createdBy,
                 Date updatedAt,
                 String updatedBy) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }
}
