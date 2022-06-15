package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.domain.Coupon;
import sk.stuba.fei.uim.vsa.pr2.domain.Customer;
import sk.stuba.fei.uim.vsa.pr2.service.CouponService;
import sk.stuba.fei.uim.vsa.pr2.service.UserService;
import sk.stuba.fei.uim.vsa.pr2.web.response.CouponDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.UserDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.CouponResponseFactory;

import java.util.List;
import java.util.stream.Collectors;

@Path("/coupons")
public class CouponResorce {
    private final CouponResponseFactory couponFactory = new CouponResponseFactory();
    private final CouponService couponService = new CouponService();
    private final UserService userService = new UserService();
    private final ObjectMapper json = new ObjectMapper();
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getCoupons(@QueryParam("user") @DefaultValue("0") Long userId){
        List<CouponDto> couponDtos;
        List<Coupon> coupons;
        if(userId.equals(0L)){
            coupons = couponService.getCoupons();
        }
        else{
            try {
                coupons = couponService.getCoupons(userId);
            }catch (Exception e){
                return "{}";
            }
        }
        couponDtos = coupons.stream().map(couponFactory::transformToDto).collect(Collectors.toList());
        try {
            return json.writeValueAsString(couponDtos);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCoupon(String body){
        try {
            Coupon coupon = json.readValue(body, Coupon.class);
            coupon = couponService.createCoupon(coupon);
            CouponDto couponDto = couponFactory.transformToDto(coupon);
            return Response.status(201)
                    .entity(json.writeValueAsString(couponDto))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCoupon(@PathParam("id") Long id){
        CouponDto couponDto;
        try {
            Coupon coupon = couponService.getCoupon(id);
            couponDto = couponFactory.transformToDto(coupon);
        }catch (Exception e){
            return "{}";
        }

        try {
            return json.writeValueAsString(couponDto);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCoupon(@PathParam("id") Long id) {
        Coupon coupon=couponService.getCoupon(id);
        if(coupon.getUser()!=null) {
            Customer user = userService.getUseById(coupon.getUser().getId());
            List<Coupon> coupons = user.getCoupons();
            coupons.removeIf(c -> c.getId().equals(coupon.getId()));
            user.setCoupons(coupons);
            userService.updateUser(user);
        }
        couponService.deleteCoupon(id);
        return Response.status(204).build();
    }

    @POST
    @Path("/{id}/give/{userId}")
    public String giveCouponToUsre(@PathParam("id") Long couponId, @PathParam("userId") Long userId){
        try {
            CouponDto couponDto;
            try {
                Coupon coupon = couponService.giveCouponToUser(couponId,userId);
                Customer user = userService.getUseById(userId);
                List<Coupon> coupons = user.getCoupons();
                coupons.add(coupon);
                user.setCoupons(coupons);
                userService.updateUser(user);
                couponDto = couponFactory.transformToDto(coupon);
            }catch (Exception e){
                return "{}";
            }

            return json.writeValueAsString(couponDto);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}
