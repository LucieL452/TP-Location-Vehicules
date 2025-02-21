package com.accenture.service.dto;

public record AdminResponseDto(

        String email,
        String password,
        String nom,
        String prenom
        ) {
}
