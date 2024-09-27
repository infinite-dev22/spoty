package inc.nomard.spoty.network_bridge.dtos;


import lombok.*;
import lombok.extern.java.Log;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
public class Role {
    private Long id;
    private String name;
    private String label;
    private String description;
    private List<Permission> permissions;
}
