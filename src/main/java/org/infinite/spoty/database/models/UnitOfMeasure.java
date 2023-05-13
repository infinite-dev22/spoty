package org.infinite.spoty.database.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "unit_of_measure")
public class UnitOfMeasure {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String shortName;
    @ManyToOne
    private UnitOfMeasure baseUnit;
    private String operator;
    @Column(name = "operator_value")
    private double operatorValue;
    private Date createdAt = null;
    private String createdBy = null;
    private Date updatedAt = null;
    private String updatedBy = null;

    public UnitOfMeasure(String name,
                         String shortName,
                         UnitOfMeasure baseUnit,
                         String operator,
                         double operatorValue,
                         Date createdAt,
                         String createdBy,
                         Date updatedAt,
                         String updatedBy) {
        this.name = name;
        this.shortName = shortName;
        this.baseUnit = baseUnit;
        this.operator = operator;
        this.operatorValue = operatorValue;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    public UnitOfMeasure() {
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public UnitOfMeasure getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(UnitOfMeasure baseUnit) {
        this.baseUnit = baseUnit;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public double getOperatorValue() {
        return operatorValue;
    }

    public void setOperatorValue(double operatorValue) {
        this.operatorValue = operatorValue;
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
