package inc.nomard.spoty.network_bridge.dtos;

import lombok.*;
import lombok.extern.java.Log;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Log
public class Tenant {
    private Long id;
    private String name;
    private Date subscriptionEndDate;
    @Builder.Default
    private boolean trial = false;
    @Builder.Default
    private boolean canTry = true;
    private Date trialEndDate;
    @Builder.Default
    private boolean newTenancy = true;
}