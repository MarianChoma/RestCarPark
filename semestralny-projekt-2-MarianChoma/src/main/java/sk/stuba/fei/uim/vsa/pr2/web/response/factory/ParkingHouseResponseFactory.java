package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.domain.Floor;
import sk.stuba.fei.uim.vsa.pr2.domain.ParkingHouse;
import sk.stuba.fei.uim.vsa.pr2.web.response.ParkingHouseDto;

import java.util.stream.Collectors;

public class ParkingHouseResponseFactory implements ResponseFactory<ParkingHouse, ParkingHouseDto>{

    @Override
    public ParkingHouseDto transformToDto(ParkingHouse entity) {
        ParkingHouseDto dto = new ParkingHouseDto();
        dto.setName(entity.getName());
        dto.setAddress(entity.getAddress());
        dto.setPrices(entity.getPrices());
        dto.setFloors(entity.getFloors().stream().map(Floor::getId).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public ParkingHouse transformToEntity(ParkingHouseDto dto) {
        ParkingHouse parkingHouse= new ParkingHouse();
        parkingHouse.setName(dto.getName());
        parkingHouse.setAddress(dto.getAddress());
        parkingHouse.setPrices(dto.getPrices());
        //parkingHouse.setFloors(dto.getFloorsId().stream().map(f->));
        return parkingHouse;
    }
}
