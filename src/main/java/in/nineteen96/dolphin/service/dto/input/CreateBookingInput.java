package in.nineteen96.dolphin.service.dto.input;

import in.nineteen96.dolphin.util.PaymentMode;
import in.nineteen96.dolphin.util.VehicleType;
import lombok.Data;

@Data
public class CreateBookingInput {

    private int passengers;
    private double perPassengerAmount;
    private double totalAmount;
    private PaymentMode paymentMode;
    private String customerGSTN;
    private String vehicleLastFourDigits;
    private VehicleType vehicleType;
    private String vehicleName;
    private boolean commissioned;
}
