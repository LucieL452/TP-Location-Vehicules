package com.accenture.repository.entity;


import com.accenture.model.Permis;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Classe qui gère les utilisateurs connectés
 */

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class UtilisateurConnecte {

    @Id
    private String email;
    private String password;
    private String nom;
    private String prenom;


}
