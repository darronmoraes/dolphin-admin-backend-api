package in.nineteen96.dolphin.service.dto.output;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginOutput {

    private String token;

}
