package sk.stuba.fei.uim.vsa.pr2.web.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ReservationDto extends Dto{
    private Long parkingSpot;
    private Long Car;
    private double prices;
    private String start;
    private String end;
    private boolean usingCoupon;
}
