package com.livraison.livraison.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewMessageRequest {
    @NotNull
    private Long receiverId;

    @NotNull
    @NotBlank
    private String content;
}
