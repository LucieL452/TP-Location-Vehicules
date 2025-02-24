package com.accenture.repository.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Classe qui concerne l'adresse des clients
 **/

@Data
@Entity
@NoArgsConstructor
public class Adresse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nomDeRue;
    private String codePostal;
    private String ville;

    public Adresse(String nomDeRue, String codePostal, String ville) {
        this.nomDeRue = nomDeRue;
        this.codePostal = codePostal;
        this.ville = ville;
    }
}
