package inc.nomard.spoty.network_bridge.models;

import lombok.*;
import lombok.extern.java.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Log
public class LoginModel {
    private String email;
    private String password;
}
