package inc.nomard.spoty.network_bridge.models;

import inc.nomard.spoty.network_bridge.dtos.Role;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.Employee;
import lombok.*;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
public class LoginResponseModel {
    @Builder.Default
    private int status = 0;
    @Builder.Default
    private String token = "";
    @Builder.Default
    private String message = "";
    @Builder.Default
    private String body = "";
    private boolean trial;
    private boolean canTry;
    private boolean newTenancy;
    private boolean activeTenancy;
    private boolean activeTenancyWarning;
    private boolean inActiveTenancyWarning;
    private Employee user;
    private Role role;
}
