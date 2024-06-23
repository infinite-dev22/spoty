package inc.nomard.spoty.network_bridge.dtos.dashboard;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LineChartModel {
    private String period;
    private Double totalValue;
}
