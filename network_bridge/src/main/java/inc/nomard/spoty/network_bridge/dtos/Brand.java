package inc.nomard.spoty.network_bridge.dtos;

import lombok.*;
import lombok.extern.java.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
public class Brand {
    private Long id;
    private String name;
    private String description;
    private String image;
}
