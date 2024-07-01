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
public class Department {
    private Long id;
    private String name;
    private User manager;
    private String description;
    private ArrayList<Branch> branches;
    private Department parentDepartment;
    private String location;
    private boolean active;
}
