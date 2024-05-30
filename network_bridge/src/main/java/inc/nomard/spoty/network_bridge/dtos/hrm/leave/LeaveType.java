package inc.nomard.spoty.network_bridge.dtos.hrm.leave;

import inc.nomard.spoty.network_bridge.dtos.Branch;
import lombok.*;
import lombok.extern.java.Log;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Log
public class LeaveType {
    private Long id;
    private ArrayList<Branch> branches;
    private String name;
    private String color;
    private String description;
}
