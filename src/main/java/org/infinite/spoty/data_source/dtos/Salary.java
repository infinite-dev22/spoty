package org.infinite.spoty.data_source.dtos;

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
    private Date createdAt;
    private User createdBy;
    private Date updatedAt;
    private User updatedBy;
}
