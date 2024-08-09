package inc.nomard.spoty.network_bridge.dtos.response;

import lombok.*;

@Setter
@Getter
public class SortModel {
    /**
     * unsorted : false
     * sorted : true
     * empty : false
     */

    private boolean unsorted;
    private boolean sorted;
    private boolean empty;

}