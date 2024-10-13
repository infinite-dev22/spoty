package inc.nomard.spoty.network_bridge.dtos;


import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
public class Holiday {
    private long id;
    private String title;
    private Company company;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String description;
}
