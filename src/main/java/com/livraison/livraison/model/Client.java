package com.livraison.livraison.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    private String address;

    private String phoneNumber;

    private String city;

    private String postalCode;

    private String defaultPaymentMethod;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Adresse> adresses = new ArrayList<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Commande> commandes = new ArrayList<>();

}
