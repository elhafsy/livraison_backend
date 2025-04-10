package com.livraison.livraison.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClientDTO {

    private String address;
    private String phoneNumber;
    private String city;
    private String postalCode;
    private String defaultPaymentMethod;
}
