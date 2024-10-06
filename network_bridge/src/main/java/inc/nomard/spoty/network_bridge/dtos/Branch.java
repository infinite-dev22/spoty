package inc.nomard.spoty.network_bridge.dtos;

import lombok.*;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
public class Branch {
    private Long id;
    private String name;
    private String city;
    private String phone;
    private String email;
    private String town;
}
