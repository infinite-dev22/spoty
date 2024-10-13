package inc.nomard.spoty.network_bridge.dtos;


import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
public class StockReport {
    private Long id;
    private ArrayList<Branch> branches;
    private String productName;
    private Double salePrice;
    private Double purchasePrice;
    private Integer purchasedQuantity;
    private Integer soldQuantity;
    private Double availableStock;
    private String saleStockValue;  // Value of current stock at sale price.(name = "purchase_stock_value")
    private String purchaseStockValue;  // Value of current stock at purchase price.
}
