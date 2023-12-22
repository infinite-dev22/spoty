package org.infinite.spoty.data_source.models;

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
