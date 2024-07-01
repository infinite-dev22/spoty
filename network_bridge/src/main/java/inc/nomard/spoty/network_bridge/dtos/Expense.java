package inc.nomard.spoty.network_bridge.dtos;

import java.io.*;
import java.text.*;
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
    private Date date;
    private String ref;
    private String name;
    private ExpenseCategory expenseCategory;
    private ArrayList<Branch> branches;
    private String details;
    private double amount;

    public String getExpenseCategoryName() {
        return expenseCategory.getName();
    }

    public String getLocaleDate() {
        return DateFormat.getDateInstance().format(date);
    }
}
