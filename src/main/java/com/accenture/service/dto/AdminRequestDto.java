package com.accenture.service.dto;

public record AdminRequestDto(
        String email,
        String password,
        String nom,
        String prenom,
        String fonction,
        Boolean desactive
) {
}
