package inc.nomard.spoty.network_bridge.dtos;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Bank {
    private Long id;
    private ArrayList<Branch> branches;
    private String bankName;
    private String accountName;
    private String accountNumber;
    private String balance;
    private String logo;
}
