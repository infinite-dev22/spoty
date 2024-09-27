package inc.nomard.spoty.network_bridge.dtos.accounting;

import lombok.*;
import lombok.extern.java.Log;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Log
public class Account {
    private Long id;
    private String accountName;
    private String accountNumber;
    private Double credit;
    private Double debit;
    private Double balance;
    private String description;
}
