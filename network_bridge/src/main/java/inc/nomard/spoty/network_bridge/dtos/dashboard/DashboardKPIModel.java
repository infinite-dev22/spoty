package inc.nomard.spoty.network_bridge.dtos.dashboard;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardKPIModel {
    private String name;
    private Number value;
}
