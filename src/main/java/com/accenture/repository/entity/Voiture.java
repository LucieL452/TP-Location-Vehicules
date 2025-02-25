package com.accenture.repository.entity;


import com.accenture.model.Carburant;
import com.accenture.model.Permis;
import com.accenture.model.TypeVoiture;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
    private int nbrePortes;
    private String autoOuManuelle;
    private Boolean climatisation;
    private int nbrBagages;
    private List<Permis> permis;



}
