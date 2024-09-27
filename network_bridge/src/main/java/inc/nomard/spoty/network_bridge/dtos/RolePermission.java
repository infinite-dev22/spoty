package inc.nomard.spoty.network_bridge.dtos;

import lombok.*;
import lombok.extern.java.Log;

import java.io.Serializable;

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
