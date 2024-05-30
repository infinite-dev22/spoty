package inc.nomard.spoty.network_bridge.dtos;

import lombok.*;
import lombok.extern.java.Log;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Log
public class Discount {
    private Long id;
    private String name;
    private double percentage;
}