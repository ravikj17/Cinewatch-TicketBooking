package com.ravi.CinewatchTicketBooking.service.Impl;

import com.ravi.CinewatchTicketBooking.dao.BookingRepository;
import com.ravi.CinewatchTicketBooking.model.Booking;
import com.ravi.CinewatchTicketBooking.model.PaymentModel;
import com.ravi.CinewatchTicketBooking.model.RefundModel;
import com.ravi.CinewatchTicketBooking.model.Seating;
import com.ravi.CinewatchTicketBooking.service.BookingService;
import com.ravi.CinewatchTicketBooking.service.PaymentService;
import com.ravi.CinewatchTicketBooking.service.RefundService;
import com.ravi.CinewatchTicketBooking.service.SeatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    BookingService bookingService;

    @Autowired
    SeatingService seatingService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    RefundService refundService;

    @Override
    public Booking findById(String id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        return booking.orElse(null);
    }

    @Override
    public List<Booking> findByUserId(String userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public void save(Booking booking) {
        bookingRepository.save(booking);
    }

    @Override
    public void delete(Booking booking) {
        bookingRepository.delete(booking);
    }

    @Override
    public int getAmount(String[] str) {
        int amount = 0;
        for (String s:str) {
            if (s.contains("A")||s.contains("B")||s.contains("a")||s.contains("b"))
                amount += 100;
            else if (s.contains("C")||s.contains("D")||s.contains("c")||s.contains("d"))
                amount += 200;
        }
        return amount;
    }

    @Override
    public void cancelBooking(String bookingId, Model model) {
        PaymentModel paymentModel = paymentService.findById(bookingId);
        RefundModel refundModel = new RefundModel();
        refundModel.setORDERID(paymentModel.getORDERID());
        refundModel.setTXNID(paymentModel.getTXNID());
        refundModel.setTXNDATE(paymentModel.getTXNDATE());
        refundModel.setTXNAMOUNT(paymentModel.getTXNAMOUNT());
        refundModel.setPAYMENTMODE(paymentModel.getPAYMENTMODE());
        refundModel.setRESPCODE(paymentModel.getRESPCODE());
        Booking booking = bookingService.findById(bookingId);
        Seating seating = seatingService.findBookedSeats(booking.getMovieTitle(),booking.getBookingDate(),booking.getTime(),booking.getTheatre());
        String[] seats = booking.getSeatNumber();
        String[] str = seating.getSeats();
        List<String> list1 = Arrays.asList(seats);
        List<String> list2 = Arrays.asList(str);
        List<String> union = new ArrayList<>(list1);
        union.addAll(list2);
        List<String> intersection = new ArrayList<>(list1);
        intersection.retainAll(list2);
        union.removeAll(intersection);
        String[] s = new String[union.size()];
        seating.setSeats(union.toArray(s));
        seatingService.save(seating);
        bookingService.delete(booking);
        paymentService.delete(paymentModel);
        refundService.save(refundModel);
    }
}
