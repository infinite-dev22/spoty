package inc.nomard.spoty.network_bridge.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PageableModel {
    /**
     * pageNumber : 0
     * paged : true
     * unpaged : false
     * offset : 0
     * sort : {"unsorted":false,"sorted":true,"empty":false}
     * pageSize : 5
     */

    private int pageNumber;
    private boolean paged;
    private boolean unpaged;
    private int offset;
    private SortModel sort;
    private int pageSize;
}