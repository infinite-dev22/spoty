package inc.nomard.spoty.network_bridge.dtos.hrm.employee;

import lombok.*;
import lombok.extern.java.Log;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
public class Department {
    private Long id;
    private String name;
    private String description;
    private Employee createdBy;
    private LocalDateTime createdAt;
    private Employee updatedBy;
    private LocalDateTime updatedAt;
}
