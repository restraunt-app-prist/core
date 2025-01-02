package kpi.fict.prist.core.booking.service;

import kpi.fict.prist.core.booking.dto.CreateBookingRequest;
import kpi.fict.prist.core.booking.dto.BookingResponse;
import kpi.fict.prist.core.booking.entity.BookingEntity;
import kpi.fict.prist.core.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingResponse createBooking(CreateBookingRequest request, String externalUserId) {
        BookingEntity booking = BookingEntity.builder()
                .userId(externalUserId)
                .bookingDateTime(request.getBookingDateTime())
                .numberOfGuests(request.getNumberOfGuests())
                .notes(request.getNotes())
                .status(BookingEntity.BookingStatus.CONFIRMED)
                .build();

        BookingEntity savedBooking = bookingRepository.save(booking);

        return mapToResponse(savedBooking);
    }

    public List<BookingResponse> getBookingsByUserId(String userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getBookingsByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        return bookingRepository.findByBookingDateTimeBetween(startOfDay, endOfDay).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public void cancelBooking(String bookingId) {
        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        booking.setStatus(BookingEntity.BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    private BookingResponse mapToResponse(BookingEntity booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .userId(booking.getUserId())
                .bookingDateTime(booking.getBookingDateTime())
                .numberOfGuests(booking.getNumberOfGuests())
                .notes(booking.getNotes())
                .status(booking.getStatus().name())
                .build();
    }
}
