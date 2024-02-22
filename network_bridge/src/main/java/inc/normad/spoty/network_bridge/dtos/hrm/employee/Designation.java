package inc.normad.spoty.network_bridge.dtos.hrm.employee;

import inc.normad.spoty.network_bridge.dtos.Branch;
import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Designation {
    private Long id;
    private ArrayList<Branch> branches;
    private String name;
    private String description;
}
