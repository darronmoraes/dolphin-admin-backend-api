package in.nineteen96.dolphin.service.db;

import in.nineteen96.dolphin.entity.Booking;
import in.nineteen96.dolphin.repository.BookingRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookingService implements BookingDbService {

    @Autowired
    private BookingRepo bookingRepo;

    @Override
    public Booking save(Booking entity) {
        log.info("saving booking entity");
        return bookingRepo.save(entity);
    }

    @Override
    public Booking update(Booking entity) {
        log.info("updating booking entity");
        return save(entity);
    }

    @Override
    public void delete(Booking entity) {
        log.info("deleting booking entity");
        save(entity);
    }

    public String getSerialNumber() {
        log.info("fetching serial number");
        return bookingRepo.getNextSerialNumber();
    }
}
