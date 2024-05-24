package inc.nomard.spoty.network_bridge.models;

import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private boolean newTenancy;
    private boolean activeTenancy;
    private User user;
    private Role role;
}
