package com.accenture.repository.entity;


import com.accenture.model.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "VOITURES")
public class Voiture extends Vehicule{

    private Integer nbreDePlaces;
    private Carburant carburant;
    private TypeVoiture typeVoiture;
    private NbrePortes nbrePortes;
//    @Enumerated(EnumType.STRING)
    private Transmission transmission;
    private Boolean climatisation;
    private Integer nbrBagages;
    private Permis permis;
    private Integer kilometrage;
    private Integer tarifBase;
    private Boolean horsParc;
    private Boolean actif;



}
