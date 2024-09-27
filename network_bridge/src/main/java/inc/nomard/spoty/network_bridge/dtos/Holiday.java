package inc.nomard.spoty.network_bridge.dtos;


import lombok.*;
import lombok.extern.java.Log;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
public class Holiday {
    private long id;
    private String title;
    private Company company;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String description;
}
