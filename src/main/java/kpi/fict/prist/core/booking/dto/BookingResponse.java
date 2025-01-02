package kpi.fict.prist.core.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingResponse {
    private String id;
    private String userId;
    private LocalDateTime bookingDateTime;
    private Integer numberOfGuests;
    private String notes;
    private String status;
}
