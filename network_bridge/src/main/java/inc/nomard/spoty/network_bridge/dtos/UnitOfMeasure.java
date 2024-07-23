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
public class UnitOfMeasure {
    private Long id;
    private String name;
    private String shortName;
    private UnitOfMeasure baseUnit;
    private String operator;
    private double operatorValue;

    public String getBaseUnitName() {
        return (Objects.nonNull(baseUnit)) ? baseUnit.getName() : "";
    }
}
