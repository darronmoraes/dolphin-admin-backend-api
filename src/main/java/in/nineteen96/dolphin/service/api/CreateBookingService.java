package in.nineteen96.dolphin.service.api;

import in.nineteen96.dolphin.entity.Booking;
import in.nineteen96.dolphin.entity.User;
import in.nineteen96.dolphin.service.business.DolphinSystemAbstract;
import in.nineteen96.dolphin.service.db.BookingService;
import in.nineteen96.dolphin.service.db.UserService;
import in.nineteen96.dolphin.service.dto.input.CreateBookingInput;
import in.nineteen96.dolphin.service.dto.model.BookingDTO;
import in.nineteen96.dolphin.service.dto.output.CreateBookingOutput;
import in.nineteen96.dolphin.util.BookingStatus;
import in.nineteen96.dolphin.util.CommissionStatus;
import in.nineteen96.dolphin.util.Constant;
import in.nineteen96.dolphin.util.VehicleType;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
@Transactional
@Slf4j
public class CreateBookingService extends DolphinSystemAbstract {

    private final BookingService bookingService;
    private final UserService userService;

    public CreateBookingService(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    public CreateBookingOutput invokeCreateBookingService(CreateBookingInput input) {
        log.info("attempt to book dolphin ride");

        Booking newBooking = null;

        // check if issuing user is verified or present
        User user = userService.findByEmail(getEmail());

        if (input.isCommissioned()) {
            newBooking = processCommissionedBooking(input);
        } else {
            newBooking = processRegularBooking(input);
        }

        newBooking.setIssuedBy(user);

        newBooking = bookingService.save(newBooking);

        BookingDTO dto = mapToDTO(newBooking);

        log.info("completed create booking service");
        return CreateBookingOutput.builder()
                .status(HttpStatus.CREATED)
                .success(true)
                .timestamp(Instant.now())
                .content(dto)
                .build();
    }

    private Booking processCommissionedBooking(CreateBookingInput input) {
        log.info("processing commissioned booking...");
        Booking booking = new Booking();

        booking.setPassengers(input.getPassengers());
        booking.setPerPassengerAmount(input.getPerPassengerAmount());
        booking.setTotalAmount(input.getTotalAmount());
        booking.setSerialNumber(bookingService.getSerialNumber());
        booking.setPaymentMode(input.getPaymentMode());
        booking.setCustomerGSTN(input.getCustomerGSTN());

        if (input.isCommissioned() && (VehicleType.bus.equals(input.getVehicleType()) || VehicleType.cab.equals(input.getVehicleType())
                || VehicleType.mini_bus.equals(input.getVehicleType()))) {
            log.info("deducting commission on this booking");
            booking.setVehicleType(input.getVehicleType());
            booking.setVehicleLastFourDigits(input.getVehicleLastFourDigits());
            booking.setVehicleName(input.getVehicleName());

            booking.setCommissioned(Constant.COMMISSION_BOOKING);
            booking.setCommissionStatus(CommissionStatus.pending);
            booking.setCommissionAmount(getCommissionAmount(input.getPassengers()));
        }

        booking.setBookingStatus(BookingStatus.booked);
        booking.setDeleted(false);

        log.info("completed processing commissioned booking");
        return booking;
    }

    private Booking processRegularBooking(CreateBookingInput input) {
        log.info("processing regular booking...");
        Booking booking = new Booking();

        booking.setPassengers(input.getPassengers());
        booking.setPerPassengerAmount(input.getPerPassengerAmount());
        booking.setTotalAmount(input.getTotalAmount());
        booking.setSerialNumber(bookingService.getSerialNumber());
        booking.setPaymentMode(input.getPaymentMode());
        booking.setCustomerGSTN(input.getCustomerGSTN());

        if (!input.isCommissioned() && (VehicleType.car.equals(input.getVehicleType()) || VehicleType.two_wheeler.equals(input.getVehicleType()))) {
            log.info("no commission on this booking");
            booking.setVehicleType(input.getVehicleType());
            booking.setVehicleLastFourDigits(input.getVehicleLastFourDigits());

            booking.setCommissioned(Constant.REGULAR_BOOKING);
            booking.setCommissionStatus(CommissionStatus.unavailable);
            booking.setCommissionAmount(0.0);
        }

        booking.setBookingStatus(BookingStatus.booked);
        booking.setDeleted(false);

        log.info("completed processing regular booking");
        return booking;
    }

    private Double getCommissionAmount(int passengers) {
        return Constant.COMMISSION_AMOUNT * passengers;
    }

    private BookingDTO mapToDTO(Booking booking) {
        return BookingDTO.builder()
                .id(booking.getId())
                .serialNumber(booking.getSerialNumber())
                .passengers(booking.getPassengers())
                .perPassengerAmount(booking.getPerPassengerAmount())
                .totalAmount(booking.getTotalAmount())
                .paymentMode(booking.getPaymentMode())
                .customerGSTN(booking.getCustomerGSTN() != null ? booking.getCustomerGSTN() : null)
                .vehicleLastFourDigits(booking.getVehicleLastFourDigits())
                .vehicleType(booking.getVehicleType())
                .vehicleName(booking.getVehicleName() != null ? booking.getVehicleName() : null)
                .commissioned(booking.getCommissioned())
                .commissionAmount(booking.getCommissionAmount() != null ? booking.getCommissionAmount() : null)
                .commissionStatus(booking.getCommissionStatus())
                .issuedBy(booking.getIssuedBy().getId())
                .bookingStatus(booking.getBookingStatus())
                .bookedOn(booking.getBookedOn())
                .build();
    }
}
