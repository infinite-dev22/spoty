package inc.nomard.spoty.network_bridge.dtos.transfers;

import inc.nomard.spoty.network_bridge.dtos.*;
import java.time.*;

import java.time.*;
import java.util.*;
import lombok.*;
import lombok.extern.java.*;

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
