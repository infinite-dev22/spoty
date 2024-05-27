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
import lombok.*;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long id;
    private UserProfile userProfile;
    private Tenant tenant;
    private EmploymentStatus employmentStatus;
    private Designation designation;
    private Department department;
    private String workShift;
    private Date joiningDate;
    private String salary;
    private ArrayList<Role> roles;
    private String email;
    private ArrayList<Branch> branches;
    private boolean active;
    private boolean locked;
    private boolean accessAllBranches;

    public String getName() {
        return userProfile.getFirstName() + " " + userProfile.getOtherName() + " " + userProfile.getLastName();
    }

    public String getEmploymentStatusName() {
        return employmentStatus.getName();
    }

    public String getEmploymentStatusColor() {
        return employmentStatus.getColor();
    }

    public String getDepartmentName() {
        return department.getName();
    }

    public String getDesignationName() {
        return designation.getName();
    }
}
