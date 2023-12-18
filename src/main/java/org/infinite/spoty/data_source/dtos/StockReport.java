package org.infinite.spoty.data_source.dtos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockReport {
    private Long id;
    private Branch branch;
    private String productName;
    private Double salePrice;
    private Double purchasePrice;
    private Integer purchasedQuantity;
    private Integer soldQuantity;
    private Double availableStock;
    private String saleStockValue;  // Value of current stock at sale price.(name = "purchase_stock_value")
    private String purchaseStockValue;  // Value of current stock at purchase price.
    private Date createdAt;
    private User createdBy;
    private Date updatedAt;
    private User updatedBy;
}
