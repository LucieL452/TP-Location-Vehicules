package com.accenture.repository.entity;


import com.accenture.model.Permis;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

/**
 * Classe qui gère les clients
 */


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CLIENTS")
public class Client extends UtilisateurConnecte{

    @OneToOne(cascade = CascadeType.ALL)
    private Adresse adresse;
    private LocalDate dateNaissance;

    @CreationTimestamp
    private LocalDate dateInscription;

    private List<Permis> permis;

    public Client(String email, String password, String nom, String prenom, Adresse adresse, LocalDate dateNaissance, LocalDate dateInscription, List<Permis> permis) {
        super(email, password, nom, prenom);
        this.adresse = adresse;
        this.dateNaissance = dateNaissance;
        this.dateInscription = dateInscription;
        this.permis = permis;
    }
}
