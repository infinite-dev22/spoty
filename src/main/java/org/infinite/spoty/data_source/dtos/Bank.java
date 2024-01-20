package org.infinite.spoty.data_source.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Bank {
    private Long id;
    private Branch branch;
    private String bankName;
    private String accountName;
    private String accountNumber;
    private String balance;
    private String logo;
}
