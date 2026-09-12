package org.laicose.nexadelivery.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Merchant extends User {

    @Column(name = "business_name")
    private String businessName;

    @OneToMany(mappedBy = "merchant")
    private List<CollectionPoint> collectionPoints;
    @OneToMany(mappedBy = "merchant")
    private List<Delivery> deliveries;
}
