package com.accenture.service;

import com.accenture.exception.ClientException;
import com.accenture.service.dto.ClientRequestDto;
import com.accenture.service.dto.ClientResponseDto;

import java.util.List;

public interface ClientService {

    List<ClientResponseDto> trouverTous();

    ClientResponseDto ajouterClient(ClientRequestDto clientRequestDto) throws ClientException;
}
