package inc.nomard.spoty.network_bridge.dtos;

import lombok.*;
import lombok.extern.java.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Log
public class Branch {
    private Long id;
    private String name;
    private String city;
    private String phone;
    private String email;
    private String town;
}
