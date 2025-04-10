package com.livraison.livraison.model;

import com.livraison.livraison.model.enums.CommandeStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tracking")
public class Tracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "commande_id", nullable = false)
    private Commande commande;

    @ManyToOne
    @JoinColumn(name = "livreur_id",nullable = false)
    private Livreur livreur;

    @Enumerated(EnumType.STRING)
    private CommandeStatus status;

    private Double latitude;
    private Double longitude;
    private LocalDateTime timestamp;

    private String notes;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}
