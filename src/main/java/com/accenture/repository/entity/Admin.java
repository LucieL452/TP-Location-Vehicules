package com.accenture.repository.entity;


import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Admin extends UtilisateurConnecte{

    private String fonction;

}
