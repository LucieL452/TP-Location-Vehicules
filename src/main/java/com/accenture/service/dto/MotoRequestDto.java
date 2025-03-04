package com.accenture.service.dto;

import com.accenture.model.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MotoRequestDto(

        @NotBlank(message = "La marque est obligatoire")
        String marque,

        @NotBlank(message = "Le modèle est obligatoire")
        String modele,

        @NotBlank(message = "Le modèle est obligatoire")
        String couleur,

        @NotNull (message = "Le nombre de cylindre est obligatoire")
        Integer nbreCylindres,

        @NotNull (message = "Le poids est obligatoire")
        Integer poids,

        @NotNull (message = "Le cylindrée est obligatoire")
        Integer cylindree,

        @NotNull (message = "La puissance en kW est obligatoire")
        Integer puissanceKW,

        @NotNull (message = "La hauteur de selle est obligatoire")
        Integer hauteurSelle,

        @NotNull (message = "La transmission est obligatoire")
        Transmission transmission,

        @NotNull (message = "Le type de moto est obligatoire")
        TypeMoto typeMoto
) {

}
