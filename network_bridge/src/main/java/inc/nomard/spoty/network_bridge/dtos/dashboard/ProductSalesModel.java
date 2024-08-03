package inc.nomard.spoty.network_bridge.dtos.dashboard;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSalesModel {
    private String name;
    private Long totalQuantity;
    private Double salePrice;
    private Double costPrice;
}