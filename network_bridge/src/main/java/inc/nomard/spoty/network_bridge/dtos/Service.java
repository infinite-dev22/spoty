package inc.nomard.spoty.network_bridge.dtos;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Service {
    private Long id;
    private ArrayList<Branch> branches;
    private String name;
    private String charge;
    private String vat;
    private String description;
}