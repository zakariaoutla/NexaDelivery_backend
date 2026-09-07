package org.laicose.nexadelivery.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.laicose.nexadelivery.Enum.DeliveryStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String pickupAddress;
    private String dropAddress;
    private String description;
    private DeliveryStatus deliveryStatus;
    private String tarckingCode;
    private String clientName;
    private String clientPhone;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "merchent_id")
    private Merchant merchant;


}
