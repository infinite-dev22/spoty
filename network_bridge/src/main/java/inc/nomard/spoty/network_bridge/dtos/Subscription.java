package inc.nomard.spoty.network_bridge.dtos;

import lombok.*;
import lombok.extern.log4j.Log4j2;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
@ToString
public class Subscription {
    private Boolean canTry;
    private Boolean blockAccess;
    private Boolean showTrialSoonEnds;
    private Boolean showSubscriptionWarning;
    private Long timeLeft;
    private String message;
}
