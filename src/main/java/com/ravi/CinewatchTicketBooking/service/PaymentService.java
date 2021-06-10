package com.ravi.CinewatchTicketBooking.service;

import com.ravi.CinewatchTicketBooking.model.Booking;
import com.ravi.CinewatchTicketBooking.model.PaymentModel;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

public interface PaymentService {
    PaymentModel findById(String bookingId);

    void delete(PaymentModel paymentModel);

    ModelAndView getPaymentPage(Principal principal, Booking booking) throws Exception;
}
