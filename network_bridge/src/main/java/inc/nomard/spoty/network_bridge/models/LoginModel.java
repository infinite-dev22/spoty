package inc.nomard.spoty.network_bridge.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginModel {
    private String email;
    private String password;
}
