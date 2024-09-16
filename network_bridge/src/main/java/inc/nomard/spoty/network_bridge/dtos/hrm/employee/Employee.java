package inc.nomard.spoty.network_bridge.dtos.hrm.employee;

import inc.nomard.spoty.network_bridge.dtos.*;
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
public class Employee {
    private Long id;
    private Branch branch;
    private Tenant tenant;
    private Department department;
    private Designation designation;
    private EmploymentStatus employmentStatus;
    private Role role;
    private String firstName;
    private String lastName;
    private String otherName;
    private String phone;
    private String avatar;
    private String email;
    private String salary;
    private String workShift;
    @Builder.Default
    private boolean active = true;
    @Builder.Default
    private boolean locked = false;
    private LocalDateTime createdAt;
    private Employee createdBy;
    private LocalDateTime updatedAt;
    private Employee updatedBy;

    public String getName() {
        return this.getFirstName() + " " + this.getOtherName() + " " + this.getLastName();
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

    public String getIsActive() {
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
