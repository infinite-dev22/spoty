package inc.nomard.spoty.network_bridge.dtos.hrm.employee;

import inc.nomard.spoty.network_bridge.dtos.*;
import java.util.*;
import lombok.*;
import lombok.extern.java.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Log
public class EmploymentStatus {
    private Long id;
    private String name;
    private String color;
    private String description;
    private ArrayList<Branch> branches;
    private boolean active;
}
