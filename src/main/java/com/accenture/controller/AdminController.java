package com.accenture.controller;


import com.accenture.service.AdminService;
import com.accenture.service.dto.AdminRequestDto;
import com.accenture.service.dto.AdminResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Schema
@Tag(name = "Gestion des administrateurs", description = "API pour la gestion des administrateurs")
@RestController
@RequestMapping("/administrateurs")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Méthode qui retourne une liste de tous les administrateurs en base
     * @return
     */
    @GetMapping
    @Operation(summary = "Voir les administrateurs", description = "Voir la liste de tous les administrateurs en base.")
    @ApiResponse(responseCode = "201", description = "Accès authorisé")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    List<AdminResponseDto> tous(){
        return adminService.listeAdmin();
    }

    /**
     * Méthode qui permet d'ajouter un administrateur
     * @param adminRequestDto
     * @return
     */
    @PostMapping
    @Operation(summary = "Ajouter un administrateur", description = "Ajoute un administrateur en base.")
    @ApiResponse(responseCode = "201", description = "Ajouté avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<Void> ajouter(@RequestBody AdminRequestDto adminRequestDto){
        adminService.ajouterAdmin(adminRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }




}
