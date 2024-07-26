package inc.nomard.spoty.network_bridge.dtos;


import java.time.*;
import lombok.*;
import lombok.extern.java.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
public class Tenant {
    private Long id;
    private String name;
    private LocalDateTime subscriptionEndDate;
    @Builder.Default
    private boolean trial = false;
    @Builder.Default
    private boolean canTry = true;
    private LocalDateTime trialEndDate;
    @Builder.Default
    private boolean newTenancy = true;
}