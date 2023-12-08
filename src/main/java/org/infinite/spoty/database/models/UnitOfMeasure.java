/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

package org.infinite.spoty.database.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "units_of_measure")
public class UnitOfMeasure {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField(columnName = "short_name")
    private String shortName;

    @DatabaseField(foreign = true, columnName = "base_unit_id", foreignAutoRefresh = true)
    private UnitOfMeasure baseUnit;

    @DatabaseField
    private String operator;

    @DatabaseField(columnName = "operator_value")
    private double operatorValue;

    @DatabaseField(columnName = "created_at")
    private Date createdAt = null;

    @DatabaseField(columnName = "created_by")
    private String createdBy = null;

    @DatabaseField(columnName = "updated_at")
    private Date updatedAt = null;

    @DatabaseField(columnName = "updated_by")
    private String updatedBy = null;

    public UnitOfMeasure(
            String name,
            String shortName,
            UnitOfMeasure baseUnit,
            String operator,
            double operatorValue) {
        this.name = name;
        this.shortName = shortName;
        this.baseUnit = baseUnit;
        this.operator = operator;
        this.operatorValue = operatorValue;
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

    public String getBaseUnitName() {
        if (baseUnit != null) return baseUnit.name;
        else return null;
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
