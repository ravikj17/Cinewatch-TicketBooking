//package com.ravi.CinewatchTicketBooking.Authentication;
//
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseCookie;
//import org.springframework.web.filter.GenericFilterBean;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.time.Duration;
//
//public class SameSiteFilter extends GenericFilterBean {
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletResponse resp = (HttpServletResponse) servletResponse;
//        ResponseCookie responseCookie = ResponseCookie.from("myCookie", "myCookieValue") // key & value
//				.httpOnly(false)		// prohibit js reading
//                .secure(true)		// also transmit under http
//                .domain("localhost")// domain name
//                .path("/")			// path
//                .maxAge(Duration.ofHours(1))	// Expires in 1 hour
//                .sameSite("None")// In most cases, third-party cookies are not sent, except for Get requests that navigate to the target URL
//                .build();
//        resp.setHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//}
