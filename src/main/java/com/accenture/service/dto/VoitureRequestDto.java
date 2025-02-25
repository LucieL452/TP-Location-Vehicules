package com.accenture.service.dto;

import com.accenture.model.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record VoitureRequestDto(
        @NotBlank(message = "La marque est obligatoire")
        String marque,

        @NotBlank(message = "Le modèle est obligatoire")
        String modele,

        @NotBlank(message = "Le modèle est obligatoire")
        String couleur,

        @NotNull (message = "Le nombre de places est obligatoire")
        Integer nbreDePlaces,

        @NotNull (message = "Le carburant est obligatoire")
        Carburant carburant,

        @NotNull (message = "Le type de voiture est obligatoire")
        TypeVoiture typeVoiture,

        @NotNull (message = "Le nombre de portes est obligatoire")
        NbrePortes nbrePortes,

        @NotNull (message = "La transmission (automatique ou manuelle) est obligatoire")
        Transmission transmission,

        @NotNull (message = "La climatisation est obligatoire")
        Boolean climatisation,

        @NotNull (message = "Le nombre de bagages est obligatoire")
        Integer nbrBagages
) {
}
