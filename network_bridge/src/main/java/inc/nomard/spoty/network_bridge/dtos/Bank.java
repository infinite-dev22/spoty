package inc.nomard.spoty.network_bridge.dtos;

import java.util.*;
import lombok.*;
import lombok.extern.java.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Log
public class Bank {
    private Long id;
    private String bankName;
    private String accountName;
    private String accountNumber;
    private String balance;
    private String logo;
}
