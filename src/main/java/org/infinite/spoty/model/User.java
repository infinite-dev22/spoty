package org.infinite.spoty.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private final StringProperty firstName = new SimpleStringProperty("");
    private final StringProperty lastName = new SimpleStringProperty("");
    private final StringProperty userName = new SimpleStringProperty("");
    private final StringProperty userEmail = new SimpleStringProperty("");
    private final StringProperty userPhoneNumber = new SimpleStringProperty("");
    private final StringProperty userStatus = new SimpleStringProperty("");

    public String getFirstName() {
        return this.firstName.get();
    }

    public String getLastName() {
        return this.lastName.get();
    }

    public String getUserName() {
        return this.userName.get();
    }

    public String getUserEmail() {
        return this.userEmail.get();
    }

    public String getUserPhoneNumber() {
        return this.userPhoneNumber.get();
    }

    public String getUserStatus() {
        return this.userStatus.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public void setUserEmail(String userEmail) {
        this.userEmail.set(userEmail);
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber.set(userPhoneNumber);
    }

    public void setUserStatus(String userStatus) {
        this.userStatus.set(userStatus);
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final User other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$firstName = this.getFirstName();
        final Object other$firstName = other.getFirstName();
        if (!Objects.equals(this$firstName, other$firstName)) return false;
        final Object this$lastName = this.getLastName();
        final Object other$lastName = other.getLastName();
        if (!Objects.equals(this$lastName, other$lastName)) return false;
        final Object this$userName = this.getUserName();
        final Object other$userName = other.getUserName();
        if (!Objects.equals(this$userName, other$userName)) return false;
        final Object this$userEmail = this.getUserEmail();
        final Object other$userEmail = other.getUserEmail();
        if (!Objects.equals(this$userEmail, other$userEmail)) return false;
        final Object this$userPhoneNumber = this.getUserPhoneNumber();
        final Object other$userPhoneNumber = other.getUserPhoneNumber();
        if (!Objects.equals(this$userPhoneNumber, other$userPhoneNumber))
            return false;
        final Object this$userStatus = this.getUserStatus();
        final Object other$userStatus = other.getUserStatus();
        return Objects.equals(this$userStatus, other$userStatus);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof User;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $firstName = this.getFirstName();
        result = result * PRIME + ($firstName == null ? 43 : $firstName.hashCode());
        final Object $lastName = this.getLastName();
        result = result * PRIME + ($lastName == null ? 43 : $lastName.hashCode());
        final Object $userName = this.getUserName();
        result = result * PRIME + ($userName == null ? 43 : $userName.hashCode());
        final Object $userEmail = this.getUserEmail();
        result = result * PRIME + ($userEmail == null ? 43 : $userEmail.hashCode());
        final Object $userPhoneNumber = this.getUserPhoneNumber();
        result = result * PRIME + ($userPhoneNumber == null ? 43 : $userPhoneNumber.hashCode());
        final Object $userStatus = this.getUserStatus();
        result = result * PRIME + ($userStatus == null ? 43 : $userStatus.hashCode());
        return result;
    }

    public String toString() {
        return "User(firstName=" + this.getFirstName() + ", lastName=" + this.getLastName() + ", userName=" + this.getUserName() + ", userEmail=" + this.getUserEmail() + ", userPhoneNumber=" + this.getUserPhoneNumber() + ", userStatus=" + this.getUserStatus() + ")";
    }
}
