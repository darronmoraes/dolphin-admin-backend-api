package in.nineteen96.dolphin.entity;

import in.nineteen96.dolphin.util.BookingStatus;
import in.nineteen96.dolphin.util.CommissionStatus;
import in.nineteen96.dolphin.util.PaymentMode;
import in.nineteen96.dolphin.util.VehicleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator
    private String id;

    @Column(name = "serial_number", nullable = false, unique = true)
    private String serialNumber;

    @Column(name = "passenger", nullable = false)
    @Min(value = 1, message = "passengers must be greater than 0")
    private Integer passengers;

    @Column(name = "per_passenger_amount", nullable = false)
    private Double perPassengerAmount;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode", nullable = false)
    private PaymentMode paymentMode;

    @Column(name = "customer_gstn")
    private String customerGSTN;

    @Column(name = "vehicle_last_four_digits")
    private String vehicleLastFourDigits;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleType vehicleType;

    @Column(name = "vehicle_name")
    private String vehicleName;

    @Column(name = "commission_amount")
    private Double commissionAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "commission_status")
    private CommissionStatus commissionStatus;

    @Column(name = "commission_paid_contact_name")
    private String commissionPaidContactName;

    @Column(name = "commission_paid_contact_number")
    private String commissionPaidContactNumber;

    private Boolean commissioned;

    @ManyToOne
    @JoinColumn(name = "issued_by", referencedColumnName = "id", nullable = false)
    private User issuedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status", nullable = false)
    private BookingStatus bookingStatus;

    @CreationTimestamp
    private LocalDateTime bookedOn;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Boolean deleted;

    private LocalDateTime deletedOn;

    @ManyToOne
    @JoinColumn(name = "deleted_by", referencedColumnName = "id")
    private User deletedBy;

}
