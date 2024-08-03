package inc.nomard.spoty.network_bridge.dtos;

import lombok.*;
import lombok.extern.java.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
public class Currency {
    private Long id;
    private String code;
    private String name;
    private String symbol;
}
