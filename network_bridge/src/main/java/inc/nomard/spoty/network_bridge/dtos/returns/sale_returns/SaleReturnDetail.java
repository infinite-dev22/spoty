package inc.nomard.spoty.network_bridge.dtos.returns.sale_returns;

import inc.nomard.spoty.network_bridge.dtos.Product;
import inc.nomard.spoty.network_bridge.dtos.sales.SaleMaster;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleReturnDetail {
    private Long id;
    private String ref;
    private SaleMaster sale;
    private Product product;
    private String serialNumber;
    private double price;
    private double netTax;
    private String taxType;
    private double discount;
    private String discountType;
    private double total;
    private double subTotalPrice;
    private int quantity;

    public String getProductName() {
        return product.getName();
    }
}
