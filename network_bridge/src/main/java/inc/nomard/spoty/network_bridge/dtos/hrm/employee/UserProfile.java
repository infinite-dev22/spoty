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
public class UserProfile {
    private Long id;
    private User user;
    private String firstName;
    private String lastName;
    private String otherName;
    private String phone;
    private String email;
    private String gender;
    private String dob;
    private String avatar;
    private Tenant tenant;

    public String getName() {
        var name = "";
        name = (Objects.nonNull(firstName) && !firstName.isBlank() && !firstName.isEmpty()) ? name.concat(firstName) : "";
        name = (Objects.nonNull(otherName) && !otherName.isBlank() && !otherName.isEmpty()) ? name.concat(" ").concat(otherName) : "";
        name = (Objects.nonNull(lastName) && !lastName.isBlank() && !lastName.isEmpty()) ? name.concat(" ").concat(lastName) : "";
        return name;
    }
}
