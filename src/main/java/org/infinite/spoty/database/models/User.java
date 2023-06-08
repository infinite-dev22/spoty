package org.infinite.spoty.database.models;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "username", nullable = false, unique = true)
    private String userName;
    private String email;
    private String password;
    private String phone;
    @ManyToOne(optional = false)
    private Role role;
    @Column(nullable = false)
    private String status;
    @Column(name = "access_all_branches", nullable = false)
    private boolean accessAllBranches;
    @Column(unique = true, nullable = true)
    private Blob avatar;
    private Date createdAt;
    private String createdBy;
    private Date updatedAt;
    private String updatedBy;

    public User(String firstName,
                String lastName,
                String userName,
                String email,
                String password,
                String phone,
                Role role,
                String status,
                boolean accessAllBranches,
                Blob avatar,
                Date createdAt,
                String createdBy,
                Date updatedAt,
                String updatedBy) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.status = status;
        this.accessAllBranches = accessAllBranches;
        this.avatar = avatar;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    public User(String firstName,
                String lastName,
                String userName,
                String email,
                String password,
                String phone,
                Role role,
                String status,
                boolean accessAllBranches,
                Blob avatar) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.status = status;
        this.accessAllBranches = accessAllBranches;
        this.avatar = avatar;
    }

    public User(String firstName,
                String lastName,
                String userName,
                Role role,
                String status,
                boolean accessAllBranches) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.role = role;
        this.status = status;
        this.accessAllBranches = accessAllBranches;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAccessAllBranches() {
        return accessAllBranches;
    }

    public void setAccessAllBranches(boolean accessAllBranches) {
        this.accessAllBranches = accessAllBranches;
    }

    public Blob getAvatar() {
        return avatar;
    }

    public void setAvatar(Blob avatar) {
        this.avatar = avatar;
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
