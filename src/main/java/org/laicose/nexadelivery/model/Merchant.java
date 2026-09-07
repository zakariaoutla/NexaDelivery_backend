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

    @Enumerated(EnumType.STRING)
    private String collectionAddress;


    @OneToMany
    private List<Delivery> deliveries;
}
