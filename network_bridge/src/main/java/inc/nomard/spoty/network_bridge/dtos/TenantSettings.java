package inc.nomard.spoty.network_bridge.dtos;

import inc.nomard.spoty.network_bridge.dtos.hrm.employee.Employee;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TenantSettings {
    private Long id;
    private String name;
    private String websiteLink;
    private String phoneNumber;
    private String email;
    private String supportEmail;
    private String infoEmail;
    private String hrEmail;
    private String salesEmail;
    private String postalAddress;
    private String physicalAddress;
    private String tagLine;
    private Boolean reportLogo;
    private Boolean emailLogo;
    private Boolean receiptLogo;
    private String twitter;
    private String facebook;
    private String linkedIn;
    @Builder.Default
    private Boolean review = false;
    @Builder.Default
    private Boolean approveAdjustments = false;
    @Builder.Default
    private Boolean approveRequisitions = false;
    @Builder.Default
    private Boolean approveTransfers = false;
    @Builder.Default
    private Boolean approveStockIns = false;
    @Builder.Default
    private Boolean approveQuotations = false;
    @Builder.Default
    private Boolean approvePurchases = false;
    @Builder.Default
    private Boolean approveSales = false;
    @Builder.Default
    private Boolean approveSaleReturns = false;
    @Builder.Default
    private Boolean approvePurchaseReturns = false;
    @Builder.Default
    private Integer approvalLevels = 0;
    private List<Reviewer> reviewers;
    private Currency defaultCurrency;
    private String logo;
    private Tenant tenant;
    private LocalDateTime createdAt;
    private Employee createdBy;
    private LocalDateTime updatedAt;
    private Employee updatedBy;
}
