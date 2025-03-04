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

    ClientResponseDto ajouter(ClientRequestDto clientRequestDto) throws ClientException;

    ClientResponseDto modifier(String email, String password, ClientRequestDto clientRequestDto) throws ClientException, EntityNotFoundException;

    void supprimer(String email, String password) throws ClientException, EntityNotFoundException;
}
