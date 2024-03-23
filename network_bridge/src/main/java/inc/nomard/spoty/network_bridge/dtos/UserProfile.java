/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

package inc.nomard.spoty.network_bridge.dtos;

import inc.nomard.spoty.network_bridge.dtos.hrm.employee.User;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfile {
    private Long id;
    private User user;
    private String firstName;
    private String lastName;
    private String otherName;
    private String phone;
    private String email;
    private String avatar;

    public boolean isActive() {
        if (Objects.nonNull(user))
            return user.isActive();
        else return false;
    }

    public String getName() {
        var name = "";
        name = (Objects.nonNull(firstName) && !firstName.isBlank() && !firstName.isEmpty()) ? name.concat(firstName) : "";
        name = (Objects.nonNull(otherName) && !otherName.isBlank() && !otherName.isEmpty()) ? name.concat(" ").concat(otherName) : "";
        name = (Objects.nonNull(lastName) && !lastName.isBlank() && !lastName.isEmpty()) ? name.concat(" ").concat(lastName) : "";
        return name;
    }
}
