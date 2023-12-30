package org.infinite.spoty.data_source.daos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Salary {
    private Long id;
    private Branch branch;
    private String salaryMonth;
    private String employeeName;
    private Double salary;
}
