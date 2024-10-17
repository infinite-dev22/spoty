package inc.nomard.spoty.network_bridge.dtos.payments;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoMoModel {
    private String amount;
    private String email;
    private String phoneNumber;
    private String fullName;
    private String clientIp;
    private String deviceFingerPrint;
    private String planName;
}