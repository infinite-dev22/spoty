package inc.nomard.spoty.network_bridge.dtos.payments;

import lombok.*;
import lombok.extern.java.Log;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
public class MoMoModel {
    private String amount;
    private String email;
    private String phoneNumber;
    private String fullName;
    private String clientIp;
    private String deviceFingerPrint;
    private String planName;
}