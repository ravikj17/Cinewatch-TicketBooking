package com.ravi.CinewatchTicketBooking.dao;

import com.ravi.CinewatchTicketBooking.model.RefundModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<RefundModel,String> {
}
