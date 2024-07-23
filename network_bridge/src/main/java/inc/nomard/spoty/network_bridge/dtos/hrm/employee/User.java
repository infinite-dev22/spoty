package inc.nomard.spoty.network_bridge.dtos.hrm.employee;

import inc.nomard.spoty.network_bridge.dtos.*;
import java.time.*;

import java.time.*;
import java.util.*;
import lombok.*;
import lombok.extern.java.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
public class User {
    private Long id;
    private UserProfile userProfile;
    private Tenant tenant;
    private EmploymentStatus employmentStatus;
    private Designation designation;
    private Department department;
    private Role role;
    private String workShift;
    private Date dateOfBirth;
    private String salary;
    private String email;
    private String avatar;
    private ArrayList<Branch> branches;
    private boolean active;
    private boolean locked;
    private boolean accessAllBranches;

    public String getName() {
        return userProfile.getFirstName() + " " + userProfile.getOtherName() + " " + userProfile.getLastName();
    }

    public String getEmploymentStatusName() {
        if (Objects.nonNull(employmentStatus)) {
            return employmentStatus.getName();
        } else {
            return "N/A";
        }
    }

    public String getEmploymentStatusColor() {
        if (Objects.nonNull(employmentStatus)) {
            return employmentStatus.getColor();
        } else {
            return "N/A";
        }
    }

    public String getDepartmentName() {
        if (Objects.nonNull(department)) {
            return department.getName();
        } else {
            return "N/A";
        }
    }

    public String getDesignationName() {
        if (Objects.nonNull(designation)) {
            return designation.getName();
        } else {
            return "N/A";
        }
    }

    public String getPhone() {
        return userProfile.getPhone();
    }

    public String getActive() {
        if (active) {
            return "Active";
        }
        return "Not Active";
    }

    public String getWorkShift() {
        if (Objects.nonNull(workShift)) {
            return workShift;
        }
        return "N/A";
    }

    public String getEmail() {
        if (Objects.nonNull(email)) {
            return email;
        }
        return "N/A";
    }
}
