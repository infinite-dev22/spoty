package inc.nomard.spoty.network_bridge.dtos.response;

import java.util.*;
import lombok.*;

@Setter
@Getter
public class ResponseModel<T> {
    /**
     * totalPages : 82
     * totalElements : 407
     * pageable : {"pageNumber":0,"paged":true,"unpaged":false,"offset":0,"sort":{"unsorted":false,"sorted":true,"empty":false},"pageSize":5}
     * numberOfElements : 5
     * first : true
     * last : false
     * size : 5
     * content : [{"id":481,"name":"Test","description":"Test here","createdAt":"2024-08-08T22:36:29.981393","createdBy":{"id":1,"userProfile":{"id":1,"firstName":"Jonathan","lastName":"Mwigo","otherName":"Mark","phone":"+256761728929","avatar":null,"createdAt":null,"createdBy":null,"updatedAt":null,"updatedBy":null},"email":"mwigojm@gmail.com","active":true,"createdAt":null,"createdBy":null,"updatedAt":null,"updatedBy":null},"updatedAt":null,"updatedBy":null},{"id":480,"name":"Music","description":"Modi minima dicta ea. Recusandae explicabo molestiae suscipit odio consequatur eveniet.","createdAt":"2024-08-06T10:06:52.604187","createdBy":{"id":1,"userProfile":{"id":1,"firstName":"Jonathan","lastName":"Mwigo","otherName":"Mark","phone":"+256761728929","avatar":null,"createdAt":null,"createdBy":null,"updatedAt":null,"updatedBy":null},"email":"mwigojm@gmail.com","active":true,"createdAt":null,"createdBy":null,"updatedAt":null,"updatedBy":null},"updatedAt":null,"updatedBy":null},{"id":479,"name":"Clothing","description":"Velit dolore quibusdam. Voluptate voluptatum possimus sint. Voluptatibus et natus ullam veritatis a. Quas et neque.","createdAt":"2024-08-06T10:06:52.462387","createdBy":{"id":1,"userProfile":{"id":1,"firstName":"Jonathan","lastName":"Mwigo","otherName":"Mark","phone":"+256761728929","avatar":null,"createdAt":null,"createdBy":null,"updatedAt":null,"updatedBy":null},"email":"mwigojm@gmail.com","active":true,"createdAt":null,"createdBy":null,"updatedAt":null,"updatedBy":null},"updatedAt":null,"updatedBy":null},{"id":478,"name":"Books","description":"Rerum nostrum quos. Corrupti sint in enim iste quia ut nobis fugit totam. Nihil nam perferendis rerum sapiente quod tempore aspernatur id. Debitis sequi officia esse voluptatem. Ut veniam magnam doloribus.","createdAt":"2024-08-06T10:06:52.334205","createdBy":{"id":1,"userProfile":{"id":1,"firstName":"Jonathan","lastName":"Mwigo","otherName":"Mark","phone":"+256761728929","avatar":null,"createdAt":null,"createdBy":null,"updatedAt":null,"updatedBy":null},"email":"mwigojm@gmail.com","active":true,"createdAt":null,"createdBy":null,"updatedAt":null,"updatedBy":null},"updatedAt":null,"updatedBy":null},{"id":477,"name":"Sports","description":"Fuga est voluptatem. Exercitationem pariatur a repellendus voluptas ut illo. Velit aut explicabo. Dolorem maiores inventore temporibus iure labore aliquid voluptas ducimus. Voluptate est eligendi dolorum exercitationem velit.","createdAt":"2024-08-06T10:06:52.203056","createdBy":{"id":1,"userProfile":{"id":1,"firstName":"Jonathan","lastName":"Mwigo","otherName":"Mark","phone":"+256761728929","avatar":null,"createdAt":null,"createdBy":null,"updatedAt":null,"updatedBy":null},"email":"mwigojm@gmail.com","active":true,"createdAt":null,"createdBy":null,"updatedAt":null,"updatedBy":null},"updatedAt":null,"updatedBy":null}]
     * number : 0
     * sort : {"unsorted":false,"sorted":true,"empty":false}
     * empty : false
     */

    private int totalPages;
    private int totalElements;
    private PageableModel pageable;
    private int numberOfElements;
    private boolean first;
    private boolean last;
    private int size;
    private int number;
    private SortModel sort;
    private boolean empty;
    private ArrayList<T> content;
}
