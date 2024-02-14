package org.infinite.spoty.data_source.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryAdvance {
    private Long id;
    private ArrayList<Branch> branches;
    private String employeeName;
    private Double amount;
    private Double releaseAmount;
    private String salaryMonth;
    private Date date;
}
