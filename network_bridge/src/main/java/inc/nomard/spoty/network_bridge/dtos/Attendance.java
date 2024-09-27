package inc.nomard.spoty.network_bridge.dtos;


import lombok.*;
import lombok.extern.java.Log;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
public class Attendance {
    private Long id;
    private ArrayList<Branch> branches;
    private String employeeName;
    private LocalDateTime date;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String totalDuration;
}
