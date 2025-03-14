package in.nineteen96.dolphin.service.dto.output;

import in.nineteen96.dolphin.util.UserType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RegisterOutput {

    private String id;
    private String fullName;
    private String email;
    private Boolean active;
    private UserType type;
    private LocalDateTime createdAt;

}
