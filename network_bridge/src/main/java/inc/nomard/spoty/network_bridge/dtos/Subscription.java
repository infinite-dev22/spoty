package inc.nomard.spoty.network_bridge.dtos;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import lombok.extern.java.Log;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
@ToString
public class Subscription {
    private Boolean canTry;
    private Boolean blockAccess;
    private Boolean showTrialSoonEnds;
    private Boolean showSubscriptionWarning;
    private Long timeLeft;
    private String message;
}
