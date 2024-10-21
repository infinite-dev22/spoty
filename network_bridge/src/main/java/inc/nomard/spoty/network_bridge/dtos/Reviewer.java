package inc.nomard.spoty.network_bridge.dtos;

import inc.nomard.spoty.network_bridge.dtos.hrm.employee.Employee;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reviewer {
    private Long id;
    private Employee employee;
    private Integer level;
    private LocalDateTime createdAt;
    private Employee createdBy;
    private LocalDateTime updatedAt;
    private Employee updatedBy;
}