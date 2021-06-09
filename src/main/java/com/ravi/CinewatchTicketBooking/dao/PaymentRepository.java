package com.ravi.CinewatchTicketBooking.dao;

import com.ravi.CinewatchTicketBooking.model.PaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentModel, String> {
}
