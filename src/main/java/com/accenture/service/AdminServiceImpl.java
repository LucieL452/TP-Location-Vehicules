package com.accenture.service;


import com.accenture.repository.AdminDao;
import com.accenture.repository.entity.Admin;
import com.accenture.service.dto.AdminRequestDto;
import com.accenture.service.dto.AdminResponseDto;
import com.accenture.service.mapper.AdminMapper;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Cette classe est là pour gérer les administrateurs
 */


@Service
public class AdminServiceImpl implements AdminService{

    private final AdminDao adminDao;
    private final AdminMapper adminMapper;


    public AdminServiceImpl(AdminDao adminDao, AdminMapper adminMapper) {
        this.adminDao = adminDao;
        this.adminMapper = adminMapper;

    }

    @Override
    public AdminResponseDto ajouterAdmin(AdminRequestDto adminRequestDto) {
        Admin admin = adminMapper.toAdmin(adminRequestDto);
        admin.setFonction("ROLE_ADMIN");

        Admin adminEnreg = adminDao.save(admin);
        return adminMapper.toAdminResponseDto(adminEnreg);
    }

    @Override
    public List<AdminResponseDto> listeAdmin() {
            List<Admin> listeAdmin = adminDao.findAll();
        return listeAdmin.stream().map(adminMapper::toAdminResponseDto)
                .toList();

    }



//    ====================================================================================================
//
//                                            METHODES PRIVEES
//
//    ====================================================================================================



}
