package org.infinite.spoty.data_source.daos;

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
}
