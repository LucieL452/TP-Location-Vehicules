package com.accenture.repository.entity;


import com.accenture.model.PermisMoto;
import com.accenture.model.Transmission;
import com.accenture.model.TypeMoto;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Moto extends Vehicule{

    private Integer nbreCylindres;
    private Integer poids;
    private Integer cylindree;
    private Integer puissanceKW;
    private Integer hauteurSelle;
    private Transmission transmission;
    private TypeMoto typeMoto;
    private PermisMoto permisMoto;
    private Integer kilometrage;
    private Integer tarifBase;
    private Boolean horsParc;
    private Boolean actif;






}
