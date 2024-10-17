package inc.nomard.spoty.network_bridge.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Permission {
    private Long id;
    private String name;
    private String label;
    private String description;
    private Boolean active;
}
