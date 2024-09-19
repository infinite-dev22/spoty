package inc.nomard.spoty.network_bridge.models;

import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import java.io.*;
import java.time.*;
import lombok.*;
import lombok.extern.java.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
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
