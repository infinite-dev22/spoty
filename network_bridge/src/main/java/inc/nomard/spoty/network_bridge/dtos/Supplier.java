package inc.nomard.spoty.network_bridge.dtos;

import lombok.*;
import lombok.extern.java.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
public class Supplier {
    private Long id;
    private String name;
    private String code;
    private String email;
    private String phone;
    private String taxNumber;
    private String address;
    private String city;
    private String country;
    private String imageUrl;
}
