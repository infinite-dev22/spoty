package inc.nomard.spoty.network_bridge.dtos.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockAlertModel {
    private String name;
    private Long totalQuantity;
    private Double costPrice;
}