package inc.nomard.spoty.network_bridge.dtos;

import lombok.*;
import lombok.extern.java.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Log
public class Product {
    private long id;
    private UnitOfMeasure unit;
    private ProductCategory category;
    private Brand brand;
    private String barcodeType;
    private String name;
    private long quantity;
    private double costPrice;
    private double salePrice;
    private Discount discount;
    private Tax tax;
    private long stockAlert;
    private String serialNumber;
    private String image;

    public String getBrandName() {
        return brand.getName();
    }

    public String getCategoryName() {
        return category.getName();
    }
}
