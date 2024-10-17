package inc.nomard.spoty.network_bridge.dtos;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Subscription {
    private Boolean canTry;
    private Boolean blockAccess;
    private Boolean showTrialSoonEnds;
    private Boolean showSubscriptionWarning;
    private Long timeLeft;
    private String message;
}
