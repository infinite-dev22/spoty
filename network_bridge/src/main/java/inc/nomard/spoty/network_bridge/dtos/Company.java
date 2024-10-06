package inc.nomard.spoty.network_bridge.dtos;

import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.util.Currency;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
public class Company {
    private Long id;
    private String name;
    private String website;
    private String phone;
    private String email;
    private String postalAddress;
    private String physicalAddress;
    private String tagLine;
    private String logo;
    private boolean logoOnReports;
    private boolean logoOnEmails;
    private boolean logoOnReceipts;
    private String twitter;
    private String facebook;
    private String linkedin;
    private Currency defaultCurrency;
}
