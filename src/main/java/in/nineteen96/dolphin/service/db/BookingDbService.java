package in.nineteen96.dolphin.service.db;

import in.nineteen96.dolphin.entity.Booking;

public interface BookingDbService {

    Booking save(Booking entity);

    Booking update(Booking entity);

    void delete(Booking entity);
}
