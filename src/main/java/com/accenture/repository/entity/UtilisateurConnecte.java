package com.accenture.repository.entity;


import com.accenture.model.Permis;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class UtilisateurConnecte {

    @Id
    private String email;
    private String password;
    private String nom;
    private String prenom;




}
