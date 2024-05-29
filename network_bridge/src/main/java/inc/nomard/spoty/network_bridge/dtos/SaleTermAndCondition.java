package inc.nomard.spoty.network_bridge.dtos;

import lombok.*;

import java.util.ArrayList;
import lombok.extern.slf4j.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Slf4j
public class SaleTermAndCondition {
    private Long id;
    private String name;
    private ArrayList<Branch> branches;
    private boolean active;
}
