package inc.nomard.spoty.network_bridge.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tax {
    private Long id;
    private String name;
    private double percentage;
}
