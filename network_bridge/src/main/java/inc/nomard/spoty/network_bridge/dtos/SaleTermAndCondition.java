package inc.nomard.spoty.network_bridge.dtos;

import lombok.*;
import lombok.extern.java.Log;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Log
public class SaleTermAndCondition {
    private Long id;
    private String name;
    private ArrayList<Branch> branches;
    private boolean active;
}
