package com.ravi.CinewatchTicketBooking.service.Impl;

import com.ravi.CinewatchTicketBooking.dao.PaymentRepository;
import com.ravi.CinewatchTicketBooking.model.PaymentModel;
import com.ravi.CinewatchTicketBooking.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentRepository paymentRepository;


    @Override
    public PaymentModel findById(String bookingId) {
        Optional<PaymentModel> paymentModel = paymentRepository.findById(bookingId);
        return paymentModel.orElse(null);
    }

    @Override
    public void delete(PaymentModel paymentModel) {
        paymentRepository.delete(paymentModel);
    }
}
