package org.laicose.nexadelivery.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.laicose.nexadelivery.Enum.VehicleType;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private VehicleType type;
    private Double capacityKg;

    @OneToOne(mappedBy = "vehicle")
    private Driver driver;
}
