package in.nineteen96.dolphin.service.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import in.nineteen96.dolphin.service.dto.model.BookingDTO;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@Builder
public class CreateBookingOutput {

    private HttpStatus status;
    private Boolean success;
    private BookingDTO content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant timestamp;

}
