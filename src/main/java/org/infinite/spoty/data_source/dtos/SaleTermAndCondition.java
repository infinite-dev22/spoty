package org.infinite.spoty.data_source.dtos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleTermAndCondition {
    private Long id;
    private String name;
    private Branch branch;
    private boolean active;
    private Date createdAt;
    private User createdBy;
    private Date updatedAt;
    private User updatedBy;
}
