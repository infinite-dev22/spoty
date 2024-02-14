package org.infinite.spoty.data_source.dtos;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleTermAndCondition {
    private Long id;
    private String name;
    private ArrayList<Branch> branches;
    private boolean active;
}
