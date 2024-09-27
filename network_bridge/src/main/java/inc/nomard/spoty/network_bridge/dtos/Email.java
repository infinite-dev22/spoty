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
public class Email {
    private Long id;
    private ArrayList<Branch> branches;
    private String name;
    private String description;
    private String usage;
    private String template;
}
