package com.livraison.livraison.model;

import com.livraison.livraison.model.enums.PaiementStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "paiements")
public class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "commande_id",nullable = false)
    private Commande commande;

    private BigDecimal amount;

    private String paymentMethod;

    private String transactionId;

    @Enumerated(EnumType.STRING)
    private PaiementStatus status;

    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

}
