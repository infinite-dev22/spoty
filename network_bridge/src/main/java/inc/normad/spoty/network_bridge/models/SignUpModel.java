package inc.normad.spoty.network_bridge.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpModel {
    private String firstName;
    private String lastName;
    private String otherName;
    private String email;
    private String password;
    private String password2;
}
