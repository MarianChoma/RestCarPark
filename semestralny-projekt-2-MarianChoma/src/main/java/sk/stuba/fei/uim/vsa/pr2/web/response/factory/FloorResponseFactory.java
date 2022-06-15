package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.domain.Floor;
import sk.stuba.fei.uim.vsa.pr2.domain.ParkingPlace;
import sk.stuba.fei.uim.vsa.pr2.web.response.FloorDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.ParkingHouseDto;

import java.util.stream.Collectors;

public class FloorResponseFactory implements ResponseFactory<Floor, FloorDto>{

    @Override
    public FloorDto transformToDto(Floor entity) {
        FloorDto floorDto= new FloorDto();
        floorDto.setIdentifier(entity.getIdentifier());
        floorDto.setCarPark(entity.getCarPark().getId());
        floorDto.setSpots(entity.getSpots().stream().map(ParkingPlace::getId).collect(Collectors.toList()));
        return floorDto;
    }

    @Override
    public Floor transformToEntity(FloorDto dto) {
        return null;
    }
}
