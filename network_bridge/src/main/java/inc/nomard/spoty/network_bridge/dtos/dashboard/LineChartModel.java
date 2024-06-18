package inc.nomard.spoty.network_bridge.dtos.dashboard;

import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LineChartModel {
    private String period;
    private BigDecimal totalValue;
}
