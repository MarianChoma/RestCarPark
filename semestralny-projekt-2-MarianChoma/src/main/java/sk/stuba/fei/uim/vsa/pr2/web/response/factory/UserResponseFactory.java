package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.domain.Car;
import sk.stuba.fei.uim.vsa.pr2.domain.Coupon;
import sk.stuba.fei.uim.vsa.pr2.domain.Customer;
import sk.stuba.fei.uim.vsa.pr2.domain.Floor;
import sk.stuba.fei.uim.vsa.pr2.web.response.UserDto;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class UserResponseFactory implements ResponseFactory<Customer, UserDto>{
    @Override
    public UserDto transformToDto(Customer entity) {
        UserDto userDto= new UserDto();
        userDto.setFirstName(entity.getFirstName());
        userDto.setLastName(entity.getLastName());
        userDto.setEmail(entity.getEmail());
        if(entity.getCars()==null){
            userDto.setCars(new ArrayList<>());
        }else{
            userDto.setCars(entity.getCars().stream().map(Car::getId).collect(Collectors.toList()));

        }
        if(entity.getCoupons()==null){
            userDto.setCoupons(new ArrayList<>());
        }
        else {
            userDto.setCoupons(entity.getCoupons().stream().map(Coupon::getId).collect(Collectors.toList()));
        }
        return userDto;
    }

    @Override
    public Customer transformToEntity(UserDto dto) {
        return null;
    }
}
