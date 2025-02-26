package com.accenture.service.dto;

import com.accenture.model.*;

import java.util.List;

public record VoitureResponseDto(
        int id,
        String marque,
        String modele,
        String couleur,
        Integer nbreDePlaces,
        Carburant carburant,
        TypeVoiture typeVoiture,
        Transmission transmission,
        NbrePortes nbrePortes,
        Boolean climatisation,
        Integer nbrBagages,
        Permis permis
) {
}
