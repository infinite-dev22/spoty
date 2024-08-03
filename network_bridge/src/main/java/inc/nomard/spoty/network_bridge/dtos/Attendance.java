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
public class Attendance {
    private Long id;
    private ArrayList<Branch> branches;
    private String employeeName;
    private LocalDateTime date;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String totalDuration;
}
