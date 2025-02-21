package com.accenture.repository.entity;


import com.accenture.model.Permis;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@Table(name = "CLIENTS")
public class Client extends UtilisateurConnecte{

    @OneToOne(cascade = CascadeType.ALL)
    private Adresse adresse;
    private LocalDate dateNaissance;

    @CreationTimestamp
    private LocalDate dateInscription;

    @Enumerated(EnumType.STRING)
    private Permis permis;
}
