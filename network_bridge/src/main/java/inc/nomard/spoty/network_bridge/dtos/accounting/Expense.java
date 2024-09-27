package inc.nomard.spoty.network_bridge.dtos.accounting;

import inc.nomard.spoty.network_bridge.dtos.Branch;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.Employee;
import lombok.*;
import lombok.extern.java.Log;

import java.io.Serializable;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

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
    private Employee createdBy;
    private LocalDateTime createdAt;
    private Employee updatedBy;
    private LocalDateTime updatedAt;

    public String getAccountName() {
        return account.getAccountName();
    }

    public String getLocaleDate() {
        return DateFormat.getDateInstance().format(date);
    }
}
