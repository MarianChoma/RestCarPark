package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.domain.ParkingPlace;
import sk.stuba.fei.uim.vsa.pr2.web.response.SpotDto;

public class SpotResponseFactory implements ResponseFactory<ParkingPlace, SpotDto>{
    @Override
    public SpotDto transformToDto(ParkingPlace entity) {
        SpotDto spotDto= new SpotDto();
        spotDto.setIdentifier(entity.getIdentifier());
        spotDto.setCarPark(entity.getCarPark().getId());
        spotDto.setCarParkFloor(entity.getCarParkFloor().getIdentifier());
        spotDto.setFree(entity.isFree());
        //spotDto.setReservations(entity);
        return spotDto;
    }

    @Override
    public ParkingPlace transformToEntity(SpotDto dto) {
        return null;
    }
}
