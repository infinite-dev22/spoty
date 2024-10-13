package inc.nomard.spoty.network_bridge.dtos;

import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
public class RolePermission implements Serializable {
    private long id;
    private Role role;
    private Permission permission;
}
