package in.nineteen96.dolphin.controller;

import in.nineteen96.dolphin.service.api.CreateBookingService;
import in.nineteen96.dolphin.service.dto.input.CreateBookingInput;
import in.nineteen96.dolphin.service.dto.output.CreateBookingOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/bookings")
@RestController
public class BookingController {

    @Autowired
    private CreateBookingService createBookingService;

    @PostMapping
    public ResponseEntity<CreateBookingOutput> createBooking(@RequestBody CreateBookingInput input) {
        return new ResponseEntity<>(createBookingService.invokeCreateBookingService(input), HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public ResponseEntity<String> createBooking() {
        return new ResponseEntity<>("Get", HttpStatus.OK);
    }

}
