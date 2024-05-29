package inc.nomard.spoty.network_bridge.dtos;

import lombok.*;

import java.util.ArrayList;
import lombok.extern.slf4j.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Slf4j
public class Email {
    private Long id;
    private ArrayList<Branch> branches;
    private String name;
    private String description;
    private String usage;
    private String template;
}
