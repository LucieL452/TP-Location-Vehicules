package com.accenture.service;

import com.accenture.exception.MotoException;
import com.accenture.service.dto.MotoRequestDto;
import com.accenture.service.dto.MotoResponseDto;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface MotoService {
    MotoResponseDto ajouter(MotoRequestDto motoRequestDto) throws MotoException;

    List<MotoResponseDto> trouverToutes();

    MotoResponseDto trouver(int id) throws MotoException;

    void supprimer(int id) throws MotoException, EntityNotFoundException;

    MotoResponseDto modifier(int id, MotoRequestDto motoRequestDto) throws MotoException, EntityNotFoundException;
}
