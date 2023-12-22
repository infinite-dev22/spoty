package org.infinite.spoty.data_source.daos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Attendance {
    private Long id;
    private Branch branch;
    private String employeeName;
    private Date date;
    private Date checkIn;
    private Date checkOut;
    private String totalDuration;
    private Date createdAt;
    private User createdBy;
    private Date updatedAt;
    private User updatedBy;
}
