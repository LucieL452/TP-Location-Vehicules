package com.accenture.repository.entity;


import com.accenture.model.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Entity
@NoArgsConstructor
@Table(name = "VOITURES")
public class Voiture extends Vehicule{

    private int nbreDePlaces;
    private Carburant carburant;
    private TypeVoiture typeVoiture;
    private NbrePortes nbrePortes;
//    @Enumerated(EnumType.STRING)
    private Transmission transmission;
    private Boolean climatisation;
    private int nbrBagages;
    private List<Permis> permis;



}
