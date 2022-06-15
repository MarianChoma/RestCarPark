package sk.stuba.fei.uim.vsa.pr2.web.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class FloorDto extends Dto{
    private String identifier;
    private Long carPark;
    private List<Long> spots;
}
