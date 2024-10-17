package inc.nomard.spoty.network_bridge.models;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupModel implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String otherName;
    private String email;
    private String phone;
    private String password;
    private String confirmPassword;
}
