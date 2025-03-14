package in.nineteen96.dolphin.service.dto.model;

import in.nineteen96.dolphin.entity.User;
import in.nineteen96.dolphin.util.BookingStatus;
import in.nineteen96.dolphin.util.CommissionStatus;
import in.nineteen96.dolphin.util.PaymentMode;
import in.nineteen96.dolphin.util.VehicleType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDTO {

    private String id;

    private String serialNumber;

    private Integer passengers;

    private Double perPassengerAmount;

    private Double totalAmount;

    private PaymentMode paymentMode;

    private String customerGSTN;

    private String vehicleLastFourDigits;

    private VehicleType vehicleType;

    private String vehicleName;

    private Double commissionAmount;

    private CommissionStatus commissionStatus;

    private String commissionPaidContactName;

    private String commissionPaidContactNumber;

    private Boolean commissioned;

    private String issuedBy;

    private BookingStatus bookingStatus;

    private LocalDateTime bookedOn;

}
