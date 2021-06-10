package com.ravi.CinewatchTicketBooking.controller;

import com.google.common.collect.Lists;
import com.ravi.CinewatchTicketBooking.model.*;
import com.ravi.CinewatchTicketBooking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    PaymentService paymentService;

    @Autowired
    BookingService bookingService;

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
    public ModelAndView checkout(Principal principal,SeatInput seatInput) throws Exception {
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
            return movieService.bookingFirstPage(true,booked,booking);
        }
        seating = seatingService.getSeats(seating,booking,str);
        booking.setSeatNumber(str);
        booking.setSeats(str.length);
        int amount = bookingService.getAmount(str);
        booking.setTransactionAmount(String.valueOf(amount));
        bookingService.save(booking);
        seatingService.save(seating);

        return paymentService.getPaymentPage(principal,booking);

    }

    @GetMapping("/reportOne")
    public String reportOne(@RequestParam String id, Model model) {
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
        bookingService.cancelBooking(bookingId,model);
        return myBookings(principal, model);
    }


}
