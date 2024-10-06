package inc.nomard.spoty.network_bridge.models;

import inc.nomard.spoty.network_bridge.dtos.Branch;
import inc.nomard.spoty.network_bridge.dtos.Role;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.Department;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.Designation;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.EmploymentStatus;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
public class UserModel implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String otherName;
    private String email;
    private String phone;
    private String salary;
    private Role role;
    private LocalDate dateOfBirth;
    private Branch branch;
    @Builder.Default
    private boolean active = true;
    @Builder.Default
    private boolean locked = false;
    private String avatar;
    private String password;
    private String confirmPassword;
    private Department department;
    private Designation designation;
    private EmploymentStatus employmentStatus;
}
