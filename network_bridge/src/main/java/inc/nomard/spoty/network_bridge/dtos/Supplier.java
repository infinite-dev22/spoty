package inc.nomard.spoty.network_bridge.dtos;

import inc.nomard.spoty.network_bridge.dtos.hrm.employee.Employee;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
public class Supplier {
    private Long id;
    private String firstName;
    private String otherName;
    private String lastName;
    private String email;
    private String phone;
    private String taxNumber;
    private String address;
    private String city;
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
