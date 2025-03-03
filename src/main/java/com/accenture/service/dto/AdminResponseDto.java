package com.accenture.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AdminResponseDto(

        String email,

        String password,

        String nom,

        String prenom,

        String fonction,
        Boolean desactive
        ) {
}
