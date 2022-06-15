package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.domain.Floor;
import sk.stuba.fei.uim.vsa.pr2.domain.ParkingHouse;
import sk.stuba.fei.uim.vsa.pr2.service.FloorService;
import sk.stuba.fei.uim.vsa.pr2.service.ParkingHouseService;
import sk.stuba.fei.uim.vsa.pr2.web.response.FloorDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.ParkingHouseDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.FloorResponseFactory;

import java.util.List;
import java.util.stream.Collectors;

@Path("/carparkfloors/{id}")
public class FloorResource {
    private static final String EMPTY_RESPONSE = "{}";
    private final FloorService floorService = new FloorService();
    private final ParkingHouseService parkingHouseService= new ParkingHouseService();
    private final FloorResponseFactory factory= new FloorResponseFactory();
    private final ObjectMapper json = new ObjectMapper();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getFloorByID(@PathParam("id") Long id){
        FloorDto floorDto;
        try {
            Floor carParkFloor = floorService.getCarParkFloor(id);
            floorDto = factory.transformToDto(carParkFloor);
        }catch (Exception e){
            return "{}";
        }

        try {
            return json.writeValueAsString(floorDto);
        } catch (JsonProcessingException e) {
            return EMPTY_RESPONSE;
        }
    }
}
