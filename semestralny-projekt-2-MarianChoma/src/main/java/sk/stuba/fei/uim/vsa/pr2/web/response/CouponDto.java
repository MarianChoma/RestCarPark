package sk.stuba.fei.uim.vsa.pr2.web.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CouponDto extends Dto{
    private String name;
    private double discount;
}
