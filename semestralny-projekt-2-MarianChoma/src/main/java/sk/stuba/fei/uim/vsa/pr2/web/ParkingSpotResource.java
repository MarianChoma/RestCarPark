package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.domain.Floor;
import sk.stuba.fei.uim.vsa.pr2.domain.ParkingPlace;
import sk.stuba.fei.uim.vsa.pr2.service.FloorService;
import sk.stuba.fei.uim.vsa.pr2.service.SpotService;
import sk.stuba.fei.uim.vsa.pr2.web.response.FloorDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.SpotDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.SpotResponseFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Path("/parkingspots/{id}")
public class ParkingSpotResource {
    private final SpotService spotService= new SpotService();
    private final SpotResponseFactory responseFactory= new SpotResponseFactory();
    private final FloorService floorService= new FloorService();
    private final ObjectMapper json = new ObjectMapper();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getSpotById(@PathParam("id") Long id){
        SpotDto spotDto;
        try {
            ParkingPlace parkingPlace = spotService.getParkingSpot(id);
            spotDto= responseFactory.transformToDto(parkingPlace);
        }catch (Exception e){
            return "{}";
        }

        try {
            return json.writeValueAsString(spotDto);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteById(@PathParam(("id")) Long id){
        try {
            ParkingPlace spot = spotService.getParkingSpot(id);
            Floor f = spotService.getFloorBySpotId(id);
            spotService.deleteParkingSpot(id);

            List<ParkingPlace> parkingPlaces = f.getSpots();
            parkingPlaces.removeIf(s -> s.getId().equals(spot.getId()));
            f.setSpots(parkingPlaces);
            floorService.updateCarParkFloor(f, f.getIdentifier());

            return Response.status(204).build();
        }catch (Exception e){
            return Response.status(400).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatespot(@PathParam("id") Long id, String body){
        try {
            ParkingPlace spot = json.readValue(body, ParkingPlace.class);
            spot.setId(id);
            spot= spotService.updateParkingSpot(spot);
            SpotDto spotDto=responseFactory.transformToDto(spot);

            return Response.status(200)
                    .entity(json.writeValueAsString(spotDto))
                    .build();
        } catch (Exception e) {
            return Response.status(400).build();
        }
    }

}
