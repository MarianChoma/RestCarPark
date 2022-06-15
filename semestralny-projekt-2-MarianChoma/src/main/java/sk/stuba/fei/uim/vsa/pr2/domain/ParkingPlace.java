package sk.stuba.fei.uim.vsa.pr2.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PARKING_SPOT")
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"CARPARKFLOOR_ID", "IDENTIFIER"})
})
public class ParkingPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String identifier;

    private boolean free = true;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Floor carParkFloor;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ParkingHouse carPark;

}
