package inc.nomard.spoty.network_bridge.dtos;

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
    private Date date;
    private Date checkIn;
    private Date checkOut;
    private String totalDuration;
}
