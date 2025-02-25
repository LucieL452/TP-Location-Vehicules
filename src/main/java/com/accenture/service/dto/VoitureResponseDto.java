package com.accenture.service.dto;

import com.accenture.model.*;

import java.util.List;

public record VoitureResponseDto(
        int id,
        String marque,
        String modele,
        String couleur,
        int nbreDePlaces,
        Carburant carburant,
        TypeVoiture typeVoiture,
        Transmission transmission,
        NbrePortes nbrePortes,
        Boolean climatisation,
        int nbrBagages,
        List<Permis> permis
) {
}
