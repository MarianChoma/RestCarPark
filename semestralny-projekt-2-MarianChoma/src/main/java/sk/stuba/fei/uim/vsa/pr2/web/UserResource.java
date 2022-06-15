package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.domain.*;
import sk.stuba.fei.uim.vsa.pr2.service.CarService;
import sk.stuba.fei.uim.vsa.pr2.service.CouponService;
import sk.stuba.fei.uim.vsa.pr2.service.UserService;
import sk.stuba.fei.uim.vsa.pr2.web.response.FloorDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.UserDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.UserResponseFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/users")
public class UserResource {
    private final UserService userService = new UserService();
    private final CarService carService = new CarService();
    private final UserResponseFactory userResponseFactory = new UserResponseFactory();
    private final CouponService couponService =  new CouponService();
    private final ObjectMapper json = new ObjectMapper();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getUsers(@QueryParam("email") @DefaultValue("") String email) {
        if (email.isEmpty()) {
            List<Customer> customerList = userService.getUsers();
            List<UserDto> customers = customerList.stream().map(userResponseFactory::transformToDto).collect(Collectors.toList());
            try {
                return json.writeValueAsString(customers);
            } catch (JsonProcessingException e) {
                return "{}";
            }
        } else {
            UserDto userDto;
            try {
                Customer c = userService.getUseById(email);
                userDto = userResponseFactory.transformToDto(c);
            }catch (Exception e){
                return "{}";
            }
            try {
                return json.writeValueAsString(userDto);
            } catch (JsonProcessingException e) {
                return "{}";
            }
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUsersById(@PathParam("id") Long id) {
        UserDto userDto;
        try {
            Customer c = userService.getUseById(id);
            userDto = userResponseFactory.transformToDto(c);
        }catch (Exception e){
            return "{}";
        }
        try {
            return json.writeValueAsString(userDto);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(String body) {
        try {
            Customer user = json.readValue(body, Customer.class);
            List<Car> cars = user.getCars();
            List<Coupon> coupons = user.getCoupons();
            user.setCars(new ArrayList<>());
            user.setCoupons(new ArrayList<>());
            userService.createUser(user);
            if(cars!=null){
                for(Car car : cars){
                    car.setOwner(user);
                    carService.createCar(car);
                }
                user.setCars(cars);
            }
            if(coupons!=null){
                for(Coupon coupon:coupons ){
                    coupon.setUser(user);
                    couponService.createCoupon(coupon);
                }
                user.setCoupons(coupons);
            }

            UserDto userDto = userResponseFactory.transformToDto(user);
            return Response.status(201)
                    .entity(json.writeValueAsString(userDto))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") Long id, String body){
        try {
            Customer c = json.readValue(body, Customer.class);
            c.setId(id);
            try {
                c = userService.updateUser(c);
            }catch (Exception e){
                return Response.status(400).build();
            }
            UserDto userDto=userResponseFactory.transformToDto(c);

            return Response.status(200)
                    .entity(json.writeValueAsString(userDto))
                    .build();
        } catch (IOException e) {
            return Response.noContent().build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        userService.deleteUser(id);
        return Response.status(204).build();
    }
}
