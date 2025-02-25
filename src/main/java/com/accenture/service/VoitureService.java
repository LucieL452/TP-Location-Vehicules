package com.accenture.service;

import com.accenture.exception.VoitureException;
import com.accenture.service.dto.VoitureRequestDto;
import com.accenture.service.dto.VoitureResponseDto;

import java.util.List;

public interface VoitureService {


    VoitureResponseDto ajouterVoiture(VoitureRequestDto voitureRequestDto) throws VoitureException;

    List<VoitureResponseDto> trouverToutes();

    VoitureResponseDto trouver(int id) throws VoitureException;
}
