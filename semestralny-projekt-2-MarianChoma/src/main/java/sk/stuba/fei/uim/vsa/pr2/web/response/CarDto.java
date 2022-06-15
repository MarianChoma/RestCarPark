package sk.stuba.fei.uim.vsa.pr2.web.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CarDto extends Dto{
    private String brand;
    private String model;
    private String vrp;
    private String colour;
    private Long owner;
    private List<Long> reservations;
}
