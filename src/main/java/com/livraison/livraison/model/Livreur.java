package com.livraison.livraison.model;

import com.livraison.livraison.model.enums.LivreurStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "livreurs")
public class Livreur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    private String vehicleType;

    private String licenseNumber;

    @Enumerated(EnumType.STRING)
    private LivreurStatus currentStatus;

    private Double currentLocationLat;

    private Double currentLocationLng;

    @OneToMany(mappedBy = "livreur")
    private List<Commande> commandes = new ArrayList<>();


}
