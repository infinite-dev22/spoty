package inc.nomard.spoty.network_bridge.dtos.accounting;

import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import java.io.*;
import java.text.*;
import java.time.*;
import java.util.*;
import lombok.*;
import lombok.extern.java.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
public class Expense implements Serializable {
    private Long id;
    private LocalDate date;
    private String ref;
    private String name;
    private Account account;
    private ArrayList<Branch> branches;
    private String note;
    private double amount;
    private User createdBy;
    private LocalDateTime createdAt;
    private User updatedBy;
    private LocalDateTime updatedAt;

    public String getAccountName() {
        return account.getAccountName();
    }

    public String getLocaleDate() {
        return DateFormat.getDateInstance().format(date);
    }
}
