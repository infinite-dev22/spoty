package org.infinite.spoty.data_source.models;

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
