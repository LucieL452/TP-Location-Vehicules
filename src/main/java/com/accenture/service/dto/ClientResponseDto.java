package com.accenture.service.dto;

import com.accenture.model.Permis;
import com.accenture.repository.entity.Adresse;

import java.time.LocalDate;


public record ClientResponseDto(
                                String email,
                                String password,
                                String nom,
                                String prenom,
                                AdresseDto adresse,
                                LocalDate dateNaissance,
                                Permis permis) {
}
