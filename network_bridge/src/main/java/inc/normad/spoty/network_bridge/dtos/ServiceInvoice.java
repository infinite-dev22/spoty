package inc.normad.spoty.network_bridge.dtos;

import lombok.*;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceInvoice {
    private Long id;
    private ArrayList<Branch> branches;
    private String customerName;
    private Date date;
    private String description;

    public String getLocaleDate() {
        return DateFormat.getDateInstance().format(date);
    }
}
