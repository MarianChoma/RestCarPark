package sk.stuba.fei.uim.vsa.pr2.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.persistence.annotations.CascadeOnDelete;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "CAR")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String model;
    private String colour;

    @Column(unique = true)
    private String vrp;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Customer owner;

    @OneToMany(mappedBy = "car")
    @CascadeOnDelete
    List<Reservation> reservations;


}

