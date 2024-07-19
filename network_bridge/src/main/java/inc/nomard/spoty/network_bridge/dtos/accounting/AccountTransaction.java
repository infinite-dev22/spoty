package inc.nomard.spoty.network_bridge.dtos.accounting;

import java.util.*;
import lombok.*;
import lombok.extern.java.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
public class AccountTransaction {
    private Long id;
    private Account account;
    private Date transactionDate;
    private Double credit;
    private Double debit;
    private Double amount;
    private String note;
    private String transactionType;

    public String getAccountName() {
        return Objects.nonNull(account) ? account.getAccountName() : "Null";
    }
}
