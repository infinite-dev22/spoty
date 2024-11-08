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
    private Boolean reviewAdjustments = false;
    @Builder.Default
    private Boolean reviewRequisitions = false;
    @Builder.Default
    private Boolean reviewTransfers = false;
    @Builder.Default
    private Boolean reviewStockIns = false;
    @Builder.Default
    private Boolean reviewQuotations = false;
    @Builder.Default
    private Boolean reviewPurchases = false;
    @Builder.Default
    private Boolean reviewSales = false;
    @Builder.Default
    private Boolean reviewSaleReturns = false;
    @Builder.Default
    private Boolean reviewPurchaseReturns = false;
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
