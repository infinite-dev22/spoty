package inc.nomard.spoty.network_bridge.dtos;


import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
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