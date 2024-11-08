package inc.nomard.spoty.network_bridge.dtos;


import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Email {
    private Long id;
    private ArrayList<Branch> branches;
    private String name;
    private String description;
    private String usage;
    private String template;
}
