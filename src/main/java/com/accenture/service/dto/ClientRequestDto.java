package com.accenture.service.dto;

import com.accenture.model.Permis;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

public record ClientRequestDto(

        @Email(message = "L'adresse email doit être valide")
        String email,

        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[&#@-_§]).{8,16}$",
        message = "Le mot de passe doit faire entre 8 et 16 caractères et contenir au moins une majuscule, une minuscule, un chiffre et un caractère spécial ( & # @ - _ §)")
        String password,

        @NotBlank(message = "Le nom est obligatoire")
        String nom,

        @NotBlank(message = "Le prenom est obligatoire")
        String prenom,

        @NotNull(message = "L'adresse est obligatoire")
        AdresseDto adresse,

        @NotNull(message = "La date de naissance est obligatoire")
        @Past(message = "La date de naissance doit être dans le passé")
        LocalDate dateNaissance,

        List<Permis> permis) {
}
