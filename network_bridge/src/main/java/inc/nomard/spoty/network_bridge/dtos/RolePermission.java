package inc.nomard.spoty.network_bridge.dtos;

import java.io.*;
import lombok.*;
import lombok.extern.java.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Log
public class RolePermission implements Serializable {
    private long id;
    private Role role;
    private Permission permission;
}
