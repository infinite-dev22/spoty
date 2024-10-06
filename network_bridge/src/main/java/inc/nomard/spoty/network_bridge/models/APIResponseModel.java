package inc.nomard.spoty.network_bridge.models;

import lombok.*;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
public class APIResponseModel {
    @Builder.Default
    private int status = 0;
    @Builder.Default
    private String token = "";
    @Builder.Default
    private String message = "";
    @Builder.Default
    private String body = "";
    private boolean trial;
    private boolean newTenancy;
    private boolean activeTenancy;
}
