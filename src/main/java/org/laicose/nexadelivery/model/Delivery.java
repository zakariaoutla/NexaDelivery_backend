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
    private Long id;
    private String pickupAddress;
    private String dropAddress;
    private String description;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;
    @Column(unique = true)
    private String trackingCode;
    private String clientName;
    private String clientPhone;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @OneToOne(mappedBy = "delivery")
    private Rating rating;
}
