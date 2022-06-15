package sk.stuba.fei.uim.vsa.pr2.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "RESERVATION")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private ParkingPlace parkingSpot;

    @ManyToOne
    private Car car;

    private double prices=0;
    private Date start;
    private Date end;
    private boolean usingCoupon=false;

}
