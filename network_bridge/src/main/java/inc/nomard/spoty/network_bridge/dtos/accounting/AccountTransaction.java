package inc.nomard.spoty.network_bridge.dtos.accounting;


import lombok.*;
import lombok.extern.java.Log;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
@ToString
public class AccountTransaction {
    private Long id;
    private Account account;
    private LocalDateTime transactionDate;
    private Double credit;
    private Double debit;
    private Double amount;
    private String note;
    private String transactionType;

    public String getAccountName() {
        return Objects.nonNull(account) ? account.getAccountName() : "Null";
    }
}
