package sk.stuba.fei.uim.vsa.pr2.web.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UserDto extends Dto{
    private String firstName;
    private String lastName;
    private String email;
    private List<Long> cars;
    private List<Long> coupons;
}
