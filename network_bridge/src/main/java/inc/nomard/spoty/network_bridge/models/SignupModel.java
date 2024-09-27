package inc.nomard.spoty.network_bridge.models;

import lombok.*;
import lombok.extern.java.Log;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
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
