package in.nineteen96.dolphin.service.dto.input;

import in.nineteen96.dolphin.util.UserType;
import lombok.Data;

@Data
public class RegisterInput {

    private String fullName;
    private String email;
    private String password;
    private UserType type;

}
