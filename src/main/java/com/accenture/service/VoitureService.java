package com.accenture.service;

import com.accenture.exception.VoitureException;
import com.accenture.service.dto.VoitureRequestDto;
import com.accenture.service.dto.VoitureResponseDto;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface VoitureService {


    VoitureResponseDto ajouter(VoitureRequestDto voitureRequestDto) throws VoitureException;

    List<VoitureResponseDto> trouverToutes();

    VoitureResponseDto trouver(int id) throws VoitureException;

    void supprimer(int id) throws VoitureException, EntityNotFoundException;

    VoitureResponseDto modifier(int id, VoitureRequestDto voitureRequestDto) throws VoitureException, EntityNotFoundException;
}
