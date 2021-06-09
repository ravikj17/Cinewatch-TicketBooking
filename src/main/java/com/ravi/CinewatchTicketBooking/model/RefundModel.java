package com.ravi.CinewatchTicketBooking.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "refund")
public class RefundModel {

    @Id
    String ORDERID;
    String STATUS;
    String RESPCODE;
    String PAYMENTMODE;
    String TXNAMOUNT;
    String TXNDATE;
    String TXNID;

}
