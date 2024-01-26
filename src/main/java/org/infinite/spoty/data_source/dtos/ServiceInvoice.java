package org.infinite.spoty.data_source.dtos;

import lombok.*;

import java.text.DateFormat;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceInvoice {
    private Long id;
    private Branch branch;
    private String customerName;
    private Date date;
    private String description;

    public String getBranchName() {
        return branch.getName();
    }

    public String getLocaleDate() {
        return DateFormat.getDateInstance().format(date);
    }
}