package inc.nomard.spoty.network_bridge.dtos;


import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
public class Email {
    private Long id;
    private ArrayList<Branch> branches;
    private String name;
    private String description;
    private String usage;
    private String template;
}
