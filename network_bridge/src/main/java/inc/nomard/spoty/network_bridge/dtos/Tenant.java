package inc.nomard.spoty.network_bridge.dtos;


import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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