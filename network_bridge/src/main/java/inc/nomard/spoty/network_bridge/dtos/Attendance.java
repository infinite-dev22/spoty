package inc.nomard.spoty.network_bridge.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import lombok.extern.slf4j.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Slf4j
public class Attendance {
    private Long id;
    private ArrayList<Branch> branches;
    private String employeeName;
    private Date date;
    private Date checkIn;
    private Date checkOut;
    private String totalDuration;
}
