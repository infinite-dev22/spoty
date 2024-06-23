package inc.nomard.spoty.network_bridge.dtos.dashboard;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSalesModel {
    private String name;
    private String phone;
    private String email;
    private Double totalSales;
}
