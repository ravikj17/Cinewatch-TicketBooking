package com.ravi.CinewatchTicketBooking.service;

import com.ravi.CinewatchTicketBooking.model.User;

import java.util.List;

public interface UserService {

     User findByEmail(String email);

     User findById(Long id);

     List<User> userList();

     void save(User user);

    void delete(User user);
}
