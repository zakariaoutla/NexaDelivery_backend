package org.laicose.nexadelivery.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.laicose.nexadelivery.Enum.DriverStatus;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Driver extends User {

    @Enumerated(EnumType.STRING)
    private DriverStatus driverStatus;
    private double averageRating;


    @OneToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @OneToMany(mappedBy = "driver")
    private List<Delivery> deliveries;
}


