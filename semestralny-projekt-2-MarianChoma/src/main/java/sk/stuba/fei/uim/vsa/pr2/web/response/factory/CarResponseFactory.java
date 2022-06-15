package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.domain.Car;
import sk.stuba.fei.uim.vsa.pr2.domain.ParkingPlace;
import sk.stuba.fei.uim.vsa.pr2.domain.Reservation;
import sk.stuba.fei.uim.vsa.pr2.web.response.CarDto;

import java.util.stream.Collectors;

public class CarResponseFactory implements ResponseFactory<Car, CarDto>{
    @Override
    public CarDto transformToDto(Car entity) {
        CarDto carDto= new CarDto();
        carDto.setBrand(entity.getBrand());
        carDto.setColour(entity.getColour());
        carDto.setVrp(entity.getVrp());
        carDto.setModel(entity.getModel());
        carDto.setOwner(entity.getOwner().getId());
        carDto.setReservations(entity.getReservations().stream().map(Reservation::getId).collect(Collectors.toList()));
        return carDto;
    }

    @Override
    public Car transformToEntity(CarDto dto) {
        return null;
    }
}
