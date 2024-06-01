package inc.nomard.spoty.network_bridge.dtos;

import java.util.*;
import lombok.*;
import lombok.extern.java.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Log
public class SaleTermAndCondition {
    private Long id;
    private String name;
    private ArrayList<Branch> branches;
    private boolean active;
}
