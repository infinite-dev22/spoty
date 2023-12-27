package org.infinite.spoty.data_source.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class APIResponseModel {
    @Builder.Default
    private int status = 0;
    @Builder.Default
    private String token = "";
    @Builder.Default
    private String message = "";
    @Builder.Default
    private String body = "";
}
