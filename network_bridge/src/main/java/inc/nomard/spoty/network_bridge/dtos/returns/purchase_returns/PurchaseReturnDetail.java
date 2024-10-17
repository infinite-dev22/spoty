package inc.nomard.spoty.network_bridge.dtos.returns.purchase_returns;

import inc.nomard.spoty.network_bridge.dtos.Product;
import inc.nomard.spoty.network_bridge.dtos.purchases.PurchaseMaster;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseReturnDetail {
    private Long id;
    private double cost;
    private PurchaseMaster purchase;
    private double netTax;
    private String taxType;
    private double discount;
    private String discountType;
    private Product product;
    private String serialNumber;
    private double subTotalPrice;
    private double price;
    private double total;
    private int quantity;

    public String getProductName() {
        return product.getName();
    }
}
