package com.ravi.CinewatchTicketBooking.service;

import com.ravi.CinewatchTicketBooking.model.PaymentModel;

public interface PaymentService {
    PaymentModel findById(String bookingId);

    void delete(PaymentModel paymentModel);
}
