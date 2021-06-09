package com.ravi.CinewatchTicketBooking.service.Impl;

import com.ravi.CinewatchTicketBooking.dao.RefundRepository;
import com.ravi.CinewatchTicketBooking.model.RefundModel;
import com.ravi.CinewatchTicketBooking.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefundServiceImpl implements RefundService {

    @Autowired
    RefundRepository refundRepository;


    @Override
    public void save(RefundModel refundModel) {
        refundRepository.save(refundModel);
    }
}
