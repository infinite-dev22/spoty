package inc.nomard.spoty.network_bridge.dtos;


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
public class Attendance {
    private Long id;
    private ArrayList<Branch> branches;
    private String employeeName;
    private LocalDateTime date;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String totalDuration;
}
