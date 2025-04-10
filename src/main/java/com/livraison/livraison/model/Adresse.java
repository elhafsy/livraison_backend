package com.livraison.livraison.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "adresses")
public class Adresse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    private String street;
    private String city;
    private String postalCode;
    private String country;
    private Double latitude;
    private Double longitude;
    private String instruction;
    private boolean isDefault;
}
