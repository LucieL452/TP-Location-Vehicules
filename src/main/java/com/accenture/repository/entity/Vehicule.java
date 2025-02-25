package com.accenture.repository.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;
    private String marque;
    private String modele;
    private String couleur;



}
