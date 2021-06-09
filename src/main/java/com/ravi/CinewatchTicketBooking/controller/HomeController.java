package com.ravi.CinewatchTicketBooking.controller;

import com.google.common.collect.Lists;
import com.paytm.pg.merchant.PaytmChecksum;
import com.ravi.CinewatchTicketBooking.model.*;
import com.ravi.CinewatchTicketBooking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Controller
public class HomeController {

    @Autowired
    RefundService refundService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    BookingService bookingService;

    @Autowired
    private PaytmModel paytmModel;

    @Autowired
    private Environment env;

    @Autowired
    SeatingService seatingService;

    @Autowired
    UserService userService;

    @Autowired
    MovieService movieService;

    @GetMapping("/")
    public String home(Model model) {
        List<List<Movie>> movieLists = Lists.partition(movieService.getAllMovies(),5);
        model.addAttribute("movies",movieLists.get(0));
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/process_register")
    public String processRegister(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole("USER");
        userService.save(user);
        return "login";
    }

    @PostMapping("/bookingProceed")
    public String bookingProceed(Principal principal, Booking booking,Model model) {
        User user = userService.findByEmail(principal.getName());
        booking.setUserId(String.valueOf(user.getUser_id()));
        Seating seating = seatingService.findBookedSeats(booking.getMovieTitle(),booking.getBookingDate(),booking.getTime(),booking.getTheatre());
        movieService.addSeats(model,seating);
        model.addAttribute("booking",booking);
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
        Date date = new Date();
        String s = formatter.format(date).trim();
        Long id = Long.valueOf(s);
        booking.setBookingId(String.valueOf(id));
        bookingService.save(booking);
        SeatInput seatInput = new SeatInput();
        model.addAttribute("seatInput", seatInput);
        model.addAttribute("poster", movieService.getMovieByTitle(booking.getMovieTitle()).getPoster());
        return "booking_proceed";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/allMovies")
    public String listMoviePage(Principal principal,Model model) {

        List<List<Movie>> movieLists = Lists.partition(movieService.getAllMovies(),5);
        model.addAttribute("movieLists",movieLists);
        return "all_movies";
    }

    @GetMapping("/book")
    public String book(@RequestParam String title,Model model) {
        Movie movie = movieService.getMovieByTitle(title);
        Booking booking = new Booking();
        model.addAttribute("movie",movie);
        model.addAttribute("booking",booking);
        model.addAttribute("flag",false);

        List<String> theatreList = Arrays.asList("Maya Cineplex", "PVR", "Inox");
        List<String> timingList = Arrays.asList("9", "12", "3");
        model.addAttribute("theatreList",theatreList);
        model.addAttribute("timingList",timingList);
        model.addAttribute("currentDate", LocalDate.now());
        return "book";
    }

    @GetMapping("/myBookings")
    public String myBookings(Principal principal, Model model) {
        User user = userService.findByEmail(principal.getName());
        List<Booking> booking = bookingService.findByUserId(String.valueOf(user.getUser_id()));
        List<PaymentModel> paymentModels = new ArrayList<>();
        for (Booking booking1: booking) {
            String bookingId = booking1.getBookingId();
            PaymentModel paymentModel = paymentService.findById(bookingId);
            if (paymentModel!=null) {
                paymentModels.add(paymentModel);
            }
            else {
                bookingService.delete(booking1);
            }
        }
        model.addAttribute("paymentModels",paymentModels);
        return "all_reports";
    }

    @PostMapping("/checkout")
    public ModelAndView checkout(Principal principal,SeatInput seatInput,Model model) throws Exception {
        String[] str = seatInput.getSeats().split(",");
        Booking booking = bookingService.findById(String.valueOf(seatInput.getBookingId()));
        Seating seating = seatingService.findBookedSeats(booking.getMovieTitle(),booking.getBookingDate(),booking.getTime(),booking.getTheatre());
        boolean flag = false;
        StringBuilder booked = new StringBuilder();
        for (String value : str) {
            if (seating!=null && Arrays.asList(seating.getSeats()).contains(value)) {
                flag = true;
                booked.append(" ").append(value);
            }
        }
        if (flag) {
            ModelAndView modelAndView = new ModelAndView("book");
            modelAndView.addObject("flag", true);
            modelAndView.addObject("booked",booked);
            modelAndView.addObject("movie",movieService.getMovieByTitle(booking.getMovieTitle()));
            List<String> theatreList = Arrays.asList("Maya Cineplex", "PVR", "Inox");
            List<String> timingList = Arrays.asList("9", "12", "3");
            modelAndView.addObject("theatreList",theatreList);
            modelAndView.addObject("timingList",timingList);
            modelAndView.addObject("currentDate", LocalDate.now());
            bookingService.delete(booking);
            booking = new Booking();
            modelAndView.addObject("booking",booking);
            return modelAndView;
        }
        if (seating!=null) {
            String[] str1 = seating.getSeats();
            String[] str2 = new String[str1.length+str.length];
            System.arraycopy(str, 0, str2, 0, str.length);
            System.arraycopy(str1, 0, str2, str.length, str1.length);
            seating.setSeats(str2);
        }
        else {
            Seating seating1 = new Seating();
            seating1.setMovieTitle(booking.getMovieTitle());
            seating1.setBookingDate(booking.getBookingDate());
            seating1.setTiming(booking.getTime());
            seating1.setTheatre(booking.getTheatre());
            seating1.setSeats(str);
            seating = seating1;
        }
        booking.setSeatNumber(str);
        booking.setSeats(str.length);
        int amount = 0;
        for (String s:str) {
            if (s.contains("A")||s.contains("B")||s.contains("a")||s.contains("b"))
                amount += 100;
            else if (s.contains("C")||s.contains("D")||s.contains("c")||s.contains("d"))
                amount += 200;
        }
        booking.setTransactionAmount(String.valueOf(amount));
        bookingService.save(booking);
        seatingService.save(seating);

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

    @GetMapping("/reportOne")
    public String reportOne(@RequestParam String id, Principal principal, Model model) {
        User user = userService.findByEmail(principal.getName());
        Booking booking = bookingService.findById(id);
        Movie movie = movieService.getMovieByTitle(booking.getMovieTitle());
        model.addAttribute("movie",movie);
        model.addAttribute("booking",booking);
        String[] date = booking.getBookingDate().toString().split(" ");
        model.addAttribute("date",date[0]);
        model.addAttribute("seats",Arrays.toString(booking.getSeatNumber()));
        return "report_one";
    }

    @GetMapping("/aboutUs")
    public String aboutUs() {
        return "aboutUs";
    }

    @GetMapping("/polling")
    public ModelAndView polling() {
        return new ModelAndView("redirect:" + "https://us7.list-manage.com/survey?u=c5e861ff5174330b8927502ae&id=38b3d9d111");
    }

    @GetMapping("cancelBooking")
    public String cancelBooking(@RequestParam String bookingId, Principal principal,Model model) {
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
        return myBookings(principal, model);
    }

    private boolean validateCheckSum(TreeMap<String, String> parameters, String paytmChecksum) throws Exception {
        return PaytmChecksum.verifySignature(parameters,paytmModel.getMerchantKey(), paytmChecksum);
    }


    private String getCheckSum(TreeMap<String, String> parameters) throws Exception {
        return PaytmChecksum.generateSignature(parameters,paytmModel.getMerchantKey());
    }

}
