package inc.nomard.spoty.network_bridge.dtos.returns.purchase_returns;

import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.purchases.*;
import lombok.*;
import lombok.extern.java.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Log
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
