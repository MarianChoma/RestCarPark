package sk.stuba.fei.uim.vsa.pr2.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.persistence.annotations.CascadeOnDelete;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "CAR_PARK")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
//@NamedQuery(name=ParkingHouse.DELETE_FROM_TABLE, query = "DELETE FROM CAR_PARK WHERE id=?1")
public class ParkingHouse {
    //public static final String DELETE_FROM_TABLE="ParkingHouse.deleteFromTable";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private Integer prices;

    @OneToMany(mappedBy = "carPark")
    @CascadeOnDelete
    private List<Floor> floors;

    public ParkingHouse(String name, String address, Integer pricePerHour) {
        this.name = name;
        this.address = address;
        this.prices = pricePerHour;
    }
}
