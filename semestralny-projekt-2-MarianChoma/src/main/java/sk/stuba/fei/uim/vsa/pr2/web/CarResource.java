package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.domain.Car;
import sk.stuba.fei.uim.vsa.pr2.domain.Customer;
import sk.stuba.fei.uim.vsa.pr2.domain.ParkingHouse;
import sk.stuba.fei.uim.vsa.pr2.service.CarService;
import sk.stuba.fei.uim.vsa.pr2.service.UserService;
import sk.stuba.fei.uim.vsa.pr2.web.response.CarDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.ParkingHouseDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.UserDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.CarResponseFactory;

import java.util.List;
import java.util.stream.Collectors;

@Path("/cars")
public class CarResource {
    private final CarService carService = new CarService();
    private final UserService userService = new UserService();
    private final CarResponseFactory carResponseFactory = new CarResponseFactory();
    private final ObjectMapper json = new ObjectMapper();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCar(String body) {
        try {

            Car car = json.readValue(body, Car.class);
            Customer customer = userService.getUseById(car.getOwner().getEmail());
            if(customer==null){
                customer=userService.createUser(car.getOwner());
            }
            car.setOwner(customer);
            carService.updateCar(car);
            car = carService.createCar(car);
            List<Car> cars= customer.getCars();
            cars.add(car);
            customer.setCars(cars);
            userService.updateUser(customer);
            CarDto carDto = carResponseFactory.transformToDto(car);
            return Response.status(201)
                    .entity(json.writeValueAsString(carDto))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCarById(@PathParam("id") Long id) {
        CarDto carDto;
        try {
            Car car = carService.getCarById(id);
            carDto = carResponseFactory.transformToDto(car);
        }catch (Exception e){
            return "{}";
        }
        try {
            return json.writeValueAsString(carDto);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getCars(@QueryParam("user") @DefaultValue("0") Long userId, @QueryParam("vrp") @DefaultValue("") String vrp) {
        List<CarDto> carDtos=null;
        if (userId.equals(0L) && vrp.isEmpty()) {
            List<Car> cars = carService.getCars();
            carDtos = cars.stream().map(carResponseFactory::transformToDto).collect(Collectors.toList());
        }
        if(!userId.equals(0L)){
            try {
                List<Car> cars = carService.getCars(userId);
                carDtos = cars.stream().map(carResponseFactory::transformToDto).collect(Collectors.toList());
            }catch (Exception e){
                return "{}";
            }
        }
        if(!vrp.isEmpty()){
            try {
                List<Car> cars = carService.getCars(vrp);
                carDtos = cars.stream().map(carResponseFactory::transformToDto).collect(Collectors.toList());
            }catch (Exception e){
                return "{}";
            }
        }
        try {
            return json.writeValueAsString(carDtos);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCar(@PathParam("id") Long id) {
        Car car;
        try {
            car = carService.getCarById(id);
            Customer customer=userService.getUseById(car.getOwner().getId());
            List<Car> cars = customer.getCars();
            cars.removeIf(c -> c.getId().equals(car.getId()));
            customer.setCars(cars);
            userService.updateUser(customer);
            carService.deleteCar(id);
            return Response.status(204).build();
        }catch (Exception e){
            return Response.status(400).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCar(@PathParam("id") Long id, String body){
        try {
            Car car = json.readValue(body, Car.class);
            car.setId(id);
            try {
                carService.updateCar(car);
            }catch (Exception e){
                return Response.status(400).build();
            }
            CarDto carDto= carResponseFactory.transformToDto(car);
            return Response.status(200)
                    .entity(json.writeValueAsString(carDto))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }

}
