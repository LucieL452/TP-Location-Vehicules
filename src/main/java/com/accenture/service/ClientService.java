package com.accenture.service;

import com.accenture.exception.ClientException;
import com.accenture.service.dto.ClientRequestDto;
import com.accenture.service.dto.ClientResponseDto;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface ClientService {

    List<ClientResponseDto> trouverTous();

    ClientResponseDto trouver(String email) throws ClientException;

    ClientResponseDto recuperer(String email, String password) throws ClientException;

    ClientResponseDto ajouterClient(ClientRequestDto clientRequestDto) throws ClientException;

    ClientResponseDto modifierClient(String email, String password, ClientRequestDto clientRequestDto) throws ClientException, EntityNotFoundException;

    void supprimerClient(String email, String password) throws ClientException, EntityNotFoundException;
}
