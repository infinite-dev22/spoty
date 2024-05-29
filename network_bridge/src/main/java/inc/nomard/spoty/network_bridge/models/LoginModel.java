package inc.nomard.spoty.network_bridge.models;

import lombok.*;
import lombok.extern.slf4j.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Slf4j
public class LoginModel {
    private String email;
    private String password;
}
