package inc.nomard.spoty.network_bridge.dtos;

import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import java.time.*;
import lombok.*;
import lombok.extern.java.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
public class Tax {
    private Long id;
    private String name;
    private double percentage;
    private User createdBy;
    private LocalDateTime createdAt;
    private User updatedBy;
    private LocalDateTime updatedAt;
}
