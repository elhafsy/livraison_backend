package com.livraison.livraison.model;

import com.livraison.livraison.model.enums.CommandeStatus;
import com.livraison.livraison.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "commandes")
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id",nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "livreur_id")
    private Livreur livreur;

    @Enumerated(EnumType.STRING)
    private CommandeStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime confirmedAt;

    private LocalDateTime assignedAt;

    private LocalDateTime pickedUpAt;

    private LocalDateTime deliveredAt;

    private LocalDateTime cancelledAt;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private List<CommandeItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private List<Paiement> paiements = new ArrayList<>();

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private List<Tracking> trackingEvents = new ArrayList<>();

    @OneToOne(mappedBy = "commande", cascade = CascadeType.ALL)
    private Rating rating;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status = CommandeStatus.CREATED;
        paymentStatus = PaymentStatus.PENDING;
    }



}
