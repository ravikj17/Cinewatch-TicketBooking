package com.ravi.CinewatchTicketBooking.controller;

import com.paytm.pg.merchant.PaytmChecksum;
import com.ravi.CinewatchTicketBooking.dao.PaymentRepository;
import com.ravi.CinewatchTicketBooking.dao.UserRepository;
import com.ravi.CinewatchTicketBooking.model.*;
import com.ravi.CinewatchTicketBooking.service.BookingService;
import com.ravi.CinewatchTicketBooking.service.SeatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/payment")
public class PaytmController {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    BookingService bookingService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SeatingService seatingService;

    @Autowired
    private PaytmModel paytmModel;

    @GetMapping("/")
    public String home() {
        return "paytm";
    }

    @PostMapping("/pgredirect")
    public ModelAndView getRedirect(Booking booking, Principal principal) throws Exception {

        ModelAndView modelAndView = new ModelAndView("redirect:" + paytmModel.getPaytmUrl());
        User user = userRepository.findByEmail(principal.getName());
        TreeMap<String, String> parameters = new TreeMap<>();
        booking.setUserId(String.valueOf(user.getUser_id()));
        String orderId = String.valueOf(new Random().nextInt(99999999));
        paytmModel.getDetails().forEach(parameters::put);
        parameters.put("MOBILE_NO", user.getMobileNumber());
        parameters.put("EMAIL", user.getEmail());
        parameters.put("ORDER_ID", orderId);
        parameters.put("TXN_AMOUNT", String.valueOf(70));
        parameters.put("CUST_ID", String.valueOf(user.getUser_id()));
        String checkSum = getCheckSum(parameters);
        parameters.put("CHECKSUMHASH", checkSum);
        modelAndView.addAllObjects(parameters);
        return modelAndView;
    }


    @PostMapping("/pgresponse")
    public String getResponseRedirect(HttpServletRequest request , Model model) {

        Map<String, String[]> mapData = request.getParameterMap();
        TreeMap<String, String> parameters = new TreeMap<>();
        mapData.forEach((key, val) -> parameters.put(key, val[0]));
        String paytmChecksum = "";
        if (mapData.containsKey("CHECKSUMHASH")) {
            paytmChecksum = mapData.get("CHECKSUMHASH")[0];
        }
        String result;
        PaymentModel paymentModel = new PaymentModel();
        paymentModel.setORDERID(parameters.get("ORDERID"));
        paymentModel.setPAYMENTMODE(parameters.get("PAYMENTMODE"));
        paymentModel.setRESPCODE(parameters.get("RESPCODE"));
        paymentModel.setSTATUS(parameters.get("STATUS"));
        paymentModel.setTXNAMOUNT(parameters.get("TXNAMOUNT"));
        paymentModel.setTXNDATE(parameters.get("TXNDATE"));
        paymentModel.setTXNID(parameters.get("TXNID"));
        boolean isValidateChecksum = false;
        System.out.println("RESULT : "+parameters);
        try {
            isValidateChecksum = validateCheckSum(parameters, paytmChecksum);
                if (parameters.get("RESPCODE").equals("01")) {
                    result = "Payment Successful";
                    paymentRepository.save(paymentModel);
                } else {
                    Booking booking = bookingService.findById(paymentModel.getORDERID());
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
                    result = "Payment Failed";
                }
        } catch (Exception e) {
            result = e.toString();
        }
        model.addAttribute("result",result);
        parameters.remove("CHECKSUMHASH");
        model.addAttribute("parameters",parameters);
        return "report";
    }

    private boolean validateCheckSum(TreeMap<String, String> parameters, String paytmChecksum) throws Exception {
        return PaytmChecksum.verifySignature(parameters,paytmModel.getMerchantKey(), paytmChecksum);
    }


    private String getCheckSum(TreeMap<String, String> parameters) throws Exception {
        return PaytmChecksum.generateSignature(parameters,paytmModel.getMerchantKey());
    }

}
