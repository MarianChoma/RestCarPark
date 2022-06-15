package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.domain.Reservation;
import sk.stuba.fei.uim.vsa.pr2.web.response.ReservationDto;

import java.text.SimpleDateFormat;

public class ReservationResponseFactory implements ResponseFactory<Reservation, ReservationDto>{
    @Override
    public ReservationDto transformToDto(Reservation entity) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        ReservationDto reservationDto= new ReservationDto();
        reservationDto.setCar(entity.getCar().getId());
        if(entity.getEnd()!=null){
            reservationDto.setEnd(format.format(entity.getEnd()));
        }
        reservationDto.setPrices(entity.getPrices());
        if(entity.getStart()!=null) {
            reservationDto.setStart(format.format(entity.getStart()));
        }
        reservationDto.setUsingCoupon(entity.isUsingCoupon());
        reservationDto.setParkingSpot(entity.getParkingSpot().getId());
        return reservationDto;
    }

    @Override
    public Reservation transformToEntity(ReservationDto dto) {
        return null;
    }
}
