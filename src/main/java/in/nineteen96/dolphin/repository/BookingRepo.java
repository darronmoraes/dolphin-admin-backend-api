package in.nineteen96.dolphin.repository;

import in.nineteen96.dolphin.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepo extends JpaRepository<Booking, String> {

    @Query(value = "SELECT get_next_serial_number()", nativeQuery = true)
    String getNextSerialNumber();

}
