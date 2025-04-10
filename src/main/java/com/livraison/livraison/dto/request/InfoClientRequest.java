package com.livraison.livraison.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class InfoClientRequest {
    @NotBlank(message = "L'adresse est obligatoire")
    @Size(max = 255, message = "L'adresse ne doit pas dépasser 255 caractères")
    private String address;

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Numéro de téléphone invalide")
    private String phoneNumber;

    @NotBlank(message = "La ville est obligatoire")
    @Size(max = 100, message = "Le nom de la ville ne doit pas dépasser 100 caractères")
    private String city;

    @NotBlank(message = "Le code postal est obligatoire")
    @Pattern(regexp = "^[0-9]{4,10}$", message = "Code postal invalide")
    private String postalCode;

    @NotBlank(message = "Le mode de paiement par défaut est obligatoire")
    private String defaultPaymentMethod;
}
