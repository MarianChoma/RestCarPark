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
@Entity(name = "CAR_PARK_FLOOR")
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"CARPARK_ID", "IDENTIFIER"})
})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Floor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String identifier;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ParkingHouse carPark;

    @OneToMany(mappedBy = "carParkFloor")
    @CascadeOnDelete
    private List<ParkingPlace> spots;


    public Floor(String floorIdentifier) {
        this.identifier = floorIdentifier;
    }
}
