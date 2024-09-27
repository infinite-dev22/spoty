package inc.nomard.spoty.network_bridge.dtos;

import lombok.*;
import lombok.extern.java.Log;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
public class Permission {
    private Long id;
    private String name;
    private String label;
    private String description;
    private Role roles;
}
