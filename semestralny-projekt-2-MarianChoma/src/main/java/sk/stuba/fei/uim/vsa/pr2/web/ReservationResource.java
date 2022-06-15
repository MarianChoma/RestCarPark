package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.domain.Car;
import sk.stuba.fei.uim.vsa.pr2.domain.Customer;
import sk.stuba.fei.uim.vsa.pr2.domain.Reservation;
import sk.stuba.fei.uim.vsa.pr2.service.CarService;
import sk.stuba.fei.uim.vsa.pr2.service.ReservationService;
import sk.stuba.fei.uim.vsa.pr2.web.response.ReservationDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.UserDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.ReservationResponseFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/reservations")
public class ReservationResource {
    private final ReservationService reservationService = new ReservationService();
    private final CarService carService = new CarService();
    private final ReservationResponseFactory responseFactory = new ReservationResponseFactory();
    private final ObjectMapper json = new ObjectMapper();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response CreateReservation(String body) {
        try {
            Reservation reservation = json.readValue(body, Reservation.class);
            reservation= reservationService.createReservation(reservation);
            Car car = carService.getCarById(reservation.getCar().getId());
            List<Reservation> reservationList = new ArrayList<>();
            reservationList.add(reservation);
            car.setReservations(reservationList);
            carService.updateCar(car);
            ReservationDto reservationDto= responseFactory.transformToDto(reservation);
            return Response.status(201)
                    .entity(json.writeValueAsString(reservationDto))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getReservations(@QueryParam("user") @DefaultValue("0") Long userId, @QueryParam("spot") @DefaultValue("0") Long spotId, @QueryParam("date") @DefaultValue("") String date){
        List<Reservation> reservations=null;
        try {
            if(userId.equals(0L)) {
                reservations = reservationService.getReservations();
            }
            else{
                reservations = reservationService.getMyReservations(userId);
            }
            List<ReservationDto> reservationDtos = reservations.stream().map(responseFactory::transformToDto).collect(Collectors.toList());
            return json.writeValueAsString(reservationDtos);
        } catch (Exception e) {
            return "{}";
        }
    }
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getReservation(@PathParam("id") Long id){
        try {
            Reservation reservation = reservationService.getReservation(id);
            ReservationDto reservationDto = responseFactory.transformToDto(reservation);
            return json.writeValueAsString(reservationDto);
        }catch (Exception e){
            return "{}";
        }
    }

    @POST
    @Path("/{id}/end")
    @Produces(MediaType.APPLICATION_JSON)
    public String endReservation(@PathParam("id") Long id){
        try {
            Reservation reservation = (Reservation) reservationService.endReservation(id);

            ReservationDto reservationDto = responseFactory.transformToDto(reservation);
            return json.writeValueAsString(reservationDto);
        } catch (Exception e) {
            return "{}";
        }
    }
}
