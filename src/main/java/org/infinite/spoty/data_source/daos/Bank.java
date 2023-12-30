package org.infinite.spoty.data_source.daos;

import lombok.*;

import java.util.Date;

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
