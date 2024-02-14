package org.infinite.spoty.data_source.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Attendance {
    private Long id;
    private ArrayList<Branch> branches;
    private String employeeName;
    private Date date;
    private Date checkIn;
    private Date checkOut;
    private String totalDuration;
}
