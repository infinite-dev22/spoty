package inc.nomard.spoty.network_bridge.dtos;

import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import java.time.*;
import java.util.Objects;

import lombok.*;
import lombok.extern.java.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
public class Customer {
    private Long id;
    private String firstName;
    private String otherName;
    private String lastName;
    private String code;
    private String email;
    private String phone;
    private String city;
    private String address;
    private String taxNumber;
    private String country;
    private String imageUrl;
    private Employee createdBy;
    private LocalDateTime createdAt;
    private Employee updatedBy;
    private LocalDateTime updatedAt;

    public String getName() {
        if (Objects.isNull(this.getOtherName())) {
            return this.getFirstName() + " " + this.getLastName();
        }
        return this.getFirstName() + " " + this.getOtherName() + " " + this.getLastName();
    }
}
