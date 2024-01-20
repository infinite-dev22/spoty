package org.infinite.spoty.data_source.dtos;

import lombok.*;

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
