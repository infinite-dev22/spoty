package inc.nomard.spoty.network_bridge.dtos.accounting;

import lombok.*;
import lombok.extern.java.*;

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
    private String credit;
    private String debit;
    private String balance;
    private String description;
}
