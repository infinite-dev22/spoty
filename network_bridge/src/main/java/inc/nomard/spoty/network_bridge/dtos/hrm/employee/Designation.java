package inc.nomard.spoty.network_bridge.dtos.hrm.employee;

import inc.nomard.spoty.network_bridge.dtos.Branch;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
public class Designation {
    private Long id;
    private ArrayList<Branch> branches;
    private String name;
    private String description;
    private Employee createdBy;
    private LocalDateTime createdAt;
    private Employee updatedBy;
    private LocalDateTime updatedAt;
}
