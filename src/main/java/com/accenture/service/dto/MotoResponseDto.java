package com.accenture.service.dto;

import com.accenture.model.PermisMoto;
import com.accenture.model.Transmission;
import com.accenture.model.TypeMoto;
import jakarta.validation.constraints.NotNull;

public record MotoResponseDto(
        int id,
        String marque,
        String modele,
        String couleur,
        Integer nbreCylindres,
        Integer poids,
        Integer cylindree,
        Integer puissanceKW,
        Integer hauteurSelle,
        Transmission transmission,
        TypeMoto typeMoto,
        PermisMoto permis) {
}
