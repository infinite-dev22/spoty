package inc.nomard.spoty.network_bridge.dtos;


import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
public class Role {
    private Long id;
    private String name;
    private String label;
    private String description;
    private List<Permission> permissions;
}
