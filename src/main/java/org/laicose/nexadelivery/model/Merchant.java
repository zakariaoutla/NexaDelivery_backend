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

    private String collectionAddress;


    @OneToMany(mappedBy = "merchant")
    private List<Delivery> deliveries;
}
