package inc.nomard.spoty.network_bridge.models;

import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
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
