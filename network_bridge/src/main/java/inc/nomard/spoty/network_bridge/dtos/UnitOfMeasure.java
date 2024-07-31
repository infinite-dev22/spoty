package inc.nomard.spoty.network_bridge.dtos;


import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
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
public class UnitOfMeasure {
    private Long id;
    private String name;
    private String shortName;
    private UnitOfMeasure baseUnit;
    private String operator;
    private double operatorValue;
    private User createdBy;
    private LocalDateTime createdAt;
    private User updatedBy;
    private LocalDateTime updatedAt;

    public String getBaseUnitName() {
        return (Objects.nonNull(baseUnit)) ? baseUnit.getName() : "";
    }
}
