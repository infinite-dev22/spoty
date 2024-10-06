package inc.nomard.spoty.network_bridge.dtos;


import inc.nomard.spoty.network_bridge.dtos.hrm.employee.Employee;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
public class UnitOfMeasure {
    private Long id;
    private String name;
    private String shortName;
    private UnitOfMeasure baseUnit;
    private String operator;
    private double operatorValue;
    private Employee createdBy;
    private LocalDateTime createdAt;
    private Employee updatedBy;
    private LocalDateTime updatedAt;

    public String getBaseUnitName() {
        return (Objects.nonNull(baseUnit)) ? baseUnit.getName() : "";
    }
}
