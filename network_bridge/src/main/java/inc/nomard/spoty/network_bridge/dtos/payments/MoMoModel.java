package inc.nomard.spoty.network_bridge.dtos.payments;

import lombok.*;
import lombok.extern.slf4j.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Slf4j
public class MoMoModel {
    private String amount;
    private String email;
    private String phoneNumber;
    private String fullName;
    private String clientIp;
    private String deviceFingerPrint;
    private String planName;
}