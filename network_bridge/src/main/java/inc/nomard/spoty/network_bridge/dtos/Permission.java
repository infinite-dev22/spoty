package inc.nomard.spoty.network_bridge.dtos;

import lombok.*;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
public class Permission {
    private Long id;
    private String name;
    private String label;
    private String description;
    private Role roles;
}
