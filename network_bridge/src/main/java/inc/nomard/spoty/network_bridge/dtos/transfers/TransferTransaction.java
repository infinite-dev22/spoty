package inc.nomard.spoty.network_bridge.dtos.transfers;

import inc.nomard.spoty.network_bridge.dtos.Branch;
import inc.nomard.spoty.network_bridge.dtos.Product;
import lombok.*;
import lombok.extern.java.Log;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
public class TransferTransaction {
    private Long id;
    private Branch fromBranch;
    private Branch toBranch;
    private Product product;
    private TransferDetail transferDetail;
    private LocalDateTime date;
    private long transferQuantity;
}
