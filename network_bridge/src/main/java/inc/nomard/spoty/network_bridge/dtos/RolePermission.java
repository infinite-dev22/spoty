package inc.nomard.spoty.network_bridge.dtos;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RolePermission implements Serializable {
    private long id;
    private Role role;
    private Permission permission;
}
