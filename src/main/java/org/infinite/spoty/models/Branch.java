package org.infinite.spoty.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.util.Objects;

public class Branch implements Serializable {
    private final StringProperty branchName = new SimpleStringProperty("");
    private final StringProperty branchPhoneNumber = new SimpleStringProperty("");
    private final StringProperty branchCity = new SimpleStringProperty("");
    private final StringProperty branchTown = new SimpleStringProperty("");
    private final StringProperty branchLocation = new SimpleStringProperty("");
    private final StringProperty branchEmail = new SimpleStringProperty("");

    public String getBranchName() {
        return this.branchName.get();
    }

    public void setBranchName(String branchName) {
        this.branchName.set(branchName);
    }

    public String getBranchPhoneNumber() {
        return this.branchPhoneNumber.get();
    }

    public void setBranchPhoneNumber(String branchPhoneNumber) {
        this.branchPhoneNumber.set(branchPhoneNumber);
    }

    public String getBranchCity() {
        return this.branchCity.get();
    }

    public void setBranchCity(String branchCity) {
        this.branchCity.set(branchCity);
    }

    public String getBranchTown() {
        return this.branchTown.get();
    }

    public void setBranchTown(String branchTown) {
        this.branchTown.set(branchTown);
    }

    public String getBranchLocation() {
        return this.branchLocation.get();
    }

    public void setBranchLocation(String branchLocation) {
        this.branchLocation.set(branchLocation);
    }

    public String getBranchEmail() {
        return this.branchEmail.get();
    }

    public void setBranchEmail(String branchEmail) {
        this.branchEmail.set(branchEmail);
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final Branch other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$branchName = this.getBranchName();
        final Object other$branchName = other.getBranchName();
        if (!Objects.equals(this$branchName, other$branchName))
            return false;
        final Object this$branchPhoneNumber = this.getBranchPhoneNumber();
        final Object other$branchPhoneNumber = other.getBranchPhoneNumber();
        if (!Objects.equals(this$branchPhoneNumber, other$branchPhoneNumber))
            return false;
        final Object this$branchCity = this.getBranchCity();
        final Object other$branchCity = other.getBranchCity();
        if (!Objects.equals(this$branchCity, other$branchCity))
            return false;
        final Object this$branchTown = this.getBranchTown();
        final Object other$branchTown = other.getBranchTown();
        if (!Objects.equals(this$branchTown, other$branchTown))
            return false;
        final Object this$branchLocation = this.getBranchLocation();
        final Object other$branchLocation = other.getBranchLocation();
        if (!Objects.equals(this$branchLocation, other$branchLocation))
            return false;
        final Object this$branchEmail = this.getBranchEmail();
        final Object other$branchEmail = other.getBranchEmail();
        return Objects.equals(this$branchEmail, other$branchEmail);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Branch;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $branchName = this.getBranchName();
        result = result * PRIME + ($branchName == null ? 43 : $branchName.hashCode());
        final Object $branchPhoneNumber = this.getBranchPhoneNumber();
        result = result * PRIME + ($branchPhoneNumber == null ? 43 : $branchPhoneNumber.hashCode());
        final Object $branchCity = this.getBranchCity();
        result = result * PRIME + ($branchCity == null ? 43 : $branchCity.hashCode());
        final Object $branchTown = this.getBranchTown();
        result = result * PRIME + ($branchTown == null ? 43 : $branchTown.hashCode());
        final Object $branchLocation = this.getBranchLocation();
        result = result * PRIME + ($branchLocation == null ? 43 : $branchLocation.hashCode());
        final Object $branchEmail = this.getBranchEmail();
        result = result * PRIME + ($branchEmail == null ? 43 : $branchEmail.hashCode());
        return result;
    }

    public String toString() {
        return "Branch(branchName=" + this.getBranchName() + ", branchPhoneNumber=" + this.getBranchPhoneNumber() + ", branchCity=" + this.getBranchCity() + ", branchTown=" + this.getBranchTown() + ", branchLocation=" + this.getBranchLocation() + ", branchEmail=" + this.getBranchEmail() + ")";
    }
}
