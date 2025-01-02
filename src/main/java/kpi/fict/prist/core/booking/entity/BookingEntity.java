package kpi.fict.prist.core.booking.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document("booking")
@NoArgsConstructor
@AllArgsConstructor
public class BookingEntity {

    @Id
    private String id;
    private String userId;
    private LocalDateTime bookingDateTime; // Date and time of the booking
    private Integer numberOfGuests;
    private String notes; // Optional notes
    private BookingStatus status;

    public enum BookingStatus {
        CONFIRMED,
        CANCELLED
    }
}
