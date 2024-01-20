package org.infinite.spoty.data_source.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Service {
    private Long id;
    private Branch branch;
    private String name;
    private String charge;
    private String vat;
    private String description;
}
