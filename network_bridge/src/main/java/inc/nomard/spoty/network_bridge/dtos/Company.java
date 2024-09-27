package inc.nomard.spoty.network_bridge.dtos;

import lombok.*;
import lombok.extern.java.Log;

import java.util.Currency;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
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
