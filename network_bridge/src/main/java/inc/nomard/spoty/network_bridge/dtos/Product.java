package inc.nomard.spoty.network_bridge.dtos;

import inc.nomard.spoty.network_bridge.dtos.hrm.employee.Employee;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private Employee createdBy;
    private LocalDateTime createdAt;
    private Employee updatedBy;
    private LocalDateTime updatedAt;

    public String getBrandName() {
        return brand.getName();
    }

    public String getCategoryName() {
        return category.getName();
    }
}
