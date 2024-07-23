package inc.nomard.spoty.network_bridge.dtos;


import java.time.*;
import java.util.*;
import lombok.*;
import lombok.extern.java.*;

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
