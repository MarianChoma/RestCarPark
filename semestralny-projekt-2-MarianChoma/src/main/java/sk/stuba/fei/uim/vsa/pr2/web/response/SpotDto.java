package sk.stuba.fei.uim.vsa.pr2.web.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SpotDto extends Dto{
    private String identifier;
    private String carParkFloor;
    private Long carPark;
    private boolean free;
    private List<Long> reservations;
}
