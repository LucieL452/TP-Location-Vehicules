package com.accenture.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AdminRequestDto(


        @Email(message = "L'adresse email doit être valide")
        String email,

        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[&#@-_§]).{8,16}$",
                message = "Le mot de passe doit faire entre 8 et 16 caractères et contenir au moins une majuscule, une minuscule, un chiffre et un caractère spécial ( & # @ - _ §)")
        String password,

        @NotBlank(message = "Le nom est obligatoire")
        String nom,

        @NotBlank(message = "Le prenom est obligatoire")
        String prenom,


        String fonction,
        Boolean desactive
) {
}
