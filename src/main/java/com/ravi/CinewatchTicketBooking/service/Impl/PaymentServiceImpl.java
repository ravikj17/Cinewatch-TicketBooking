package com.ravi.CinewatchTicketBooking.service.Impl;

import com.paytm.pg.merchant.PaytmChecksum;
import com.ravi.CinewatchTicketBooking.dao.PaymentRepository;
import com.ravi.CinewatchTicketBooking.model.Booking;
import com.ravi.CinewatchTicketBooking.model.PaymentModel;
import com.ravi.CinewatchTicketBooking.model.PaytmModel;
import com.ravi.CinewatchTicketBooking.model.User;
import com.ravi.CinewatchTicketBooking.service.PaymentService;
import com.ravi.CinewatchTicketBooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Optional;
import java.util.TreeMap;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    private PaytmModel paytmModel;

    @Autowired
    UserService userService;

    @Override
    public PaymentModel findById(String bookingId) {
        Optional<PaymentModel> paymentModel = paymentRepository.findById(bookingId);
        return paymentModel.orElse(null);
    }

    @Override
    public void delete(PaymentModel paymentModel) {
        paymentRepository.delete(paymentModel);
    }

    @Override
    public ModelAndView getPaymentPage(Principal principal, Booking booking) throws Exception {
        ModelAndView modelAndView = new ModelAndView("redirect:" + paytmModel.getPaytmUrl());
        User user = userService.findByEmail(principal.getName());
        TreeMap<String, String> parameters = new TreeMap<>();
        paytmModel.getDetails().forEach(parameters::put);
        parameters.put("MOBILE_NO", user.getMobileNumber());
        parameters.put("EMAIL", user.getEmail());
        parameters.put("ORDER_ID", String.valueOf(booking.getBookingId()));
        parameters.put("TXN_AMOUNT", booking.getTransactionAmount());
        parameters.put("CUST_ID", String.valueOf(user.getUser_id()));
        String checkSum = getCheckSum(parameters);
        parameters.put("CHECKSUMHASH", checkSum);
        modelAndView.addAllObjects(parameters);
        return modelAndView;
    }

    private boolean validateCheckSum(TreeMap<String, String> parameters, String paytmChecksum) throws Exception {
        return PaytmChecksum.verifySignature(parameters,paytmModel.getMerchantKey(), paytmChecksum);
    }


    private String getCheckSum(TreeMap<String, String> parameters) throws Exception {
        return PaytmChecksum.generateSignature(parameters,paytmModel.getMerchantKey());
    }

}
