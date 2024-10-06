package inc.nomard.spoty.network_bridge.models;

import lombok.*;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
public class SearchModel {
    private String search;
}
