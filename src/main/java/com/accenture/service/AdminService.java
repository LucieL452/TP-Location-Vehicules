package com.accenture.service;

import com.accenture.service.dto.AdminRequestDto;
import com.accenture.service.dto.AdminResponseDto;

import java.util.List;

public interface AdminService {



    AdminResponseDto ajouterAdmin(AdminRequestDto adminRequestDto);

    List<AdminResponseDto> listeAdmin();
}
