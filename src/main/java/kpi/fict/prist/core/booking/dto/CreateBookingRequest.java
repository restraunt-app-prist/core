package kpi.fict.prist.core.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateBookingRequest {
    private LocalDateTime bookingDateTime;
    private Integer numberOfGuests;
    private String notes;
}
