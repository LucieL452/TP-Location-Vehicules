package com.accenture.service.dto;

import jakarta.validation.constraints.NotBlank;

public record AdresseDto(

        @NotBlank(message = "Le nom de la rue est obligatoire")
        String nomDeRue,

        @NotBlank(message = "Le code postal est obligatoire")
        String codePostal,

        @NotBlank(message = "La ville est obligatoire")
        String ville) {
}
