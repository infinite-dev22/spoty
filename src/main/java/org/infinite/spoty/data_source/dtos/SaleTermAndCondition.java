package org.infinite.spoty.data_source.dtos;

import lombok.*;

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
}
