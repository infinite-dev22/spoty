package org.infinite.spoty.data_source.dtos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceInvoice {
    private Long id;
    private Branch branch;
    private String customerName;
    private Date date;
    private String description;
}
