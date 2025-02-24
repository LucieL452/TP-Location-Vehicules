package com.accenture.service.dto;

import com.accenture.model.Permis;
import com.accenture.repository.entity.Adresse;

import java.time.LocalDate;
import java.util.List;


public record ClientResponseDto(
                                String email,
                                String nom,
                                String prenom,
                                AdresseDto adresse,
                                LocalDate dateNaissance,
                                List<Permis> permis) {
}
