package com.accenture.repository.entity;


import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Classe qui gère l'administrateur
 */


@Data
@Entity
@NoArgsConstructor
public class Admin extends UtilisateurConnecte{

    private String fonction;

}
