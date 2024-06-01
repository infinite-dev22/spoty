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

package inc.nomard.spoty.network_bridge.dtos.hrm.employee;

import inc.nomard.spoty.network_bridge.dtos.*;
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
