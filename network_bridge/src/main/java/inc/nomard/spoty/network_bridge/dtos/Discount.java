package inc.nomard.spoty.network_bridge.dtos;

import lombok.*;
import lombok.extern.slf4j.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Slf4j
public class Discount {
    private Long id;
    private String name;
    private double percentage;
}
