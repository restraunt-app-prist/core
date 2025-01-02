package kpi.fict.prist.core.booking.repository;

import kpi.fict.prist.core.booking.entity.BookingEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends MongoRepository<BookingEntity, String> {
    List<BookingEntity> findByUserId(String userId);

    List<BookingEntity> findByBookingDateTimeBetween(LocalDateTime start, LocalDateTime end);
}
