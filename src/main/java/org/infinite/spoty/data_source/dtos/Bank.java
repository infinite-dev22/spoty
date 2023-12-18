package org.infinite.spoty.data_source.dtos;

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
    private Date createdAt;
    private User createdBy;
    private Date updatedAt;
    private User updatedBy;
}
