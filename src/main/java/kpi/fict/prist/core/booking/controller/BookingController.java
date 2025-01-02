package kpi.fict.prist.core.booking.controller;

import kpi.fict.prist.core.booking.dto.CreateBookingRequest;
import kpi.fict.prist.core.booking.dto.BookingResponse;
import kpi.fict.prist.core.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@AuthenticationPrincipal Jwt jwt, @RequestBody CreateBookingRequest request) {
        String userExternalId = jwt.getSubject();

        BookingResponse response = bookingService.createBooking(request, userExternalId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingResponse>> getBookingsByUserId(@AuthenticationPrincipal Jwt jwt, @PathVariable String userId) {
        String userExternalId = "me".equals(userId) ? jwt.getSubject() : userId;
        List<BookingResponse> responses = bookingService.getBookingsByUserId(userExternalId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<BookingResponse>> getBookingsByDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<BookingResponse> responses = bookingService.getBookingsByDate(localDate);
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable String id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }
}
