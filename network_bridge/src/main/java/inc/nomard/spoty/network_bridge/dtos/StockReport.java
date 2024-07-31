package inc.nomard.spoty.network_bridge.dtos;


import java.time.*;
import java.util.*;
import lombok.*;
import lombok.extern.java.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
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
