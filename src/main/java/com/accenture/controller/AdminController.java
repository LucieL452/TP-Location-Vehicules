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
     * Récupère la liste de tous les administrateurs enregistrés en base de données.
     *
     * <p>Cette méthode retourne une liste d'objets {@link AdminResponseDto} représentant
     * les administrateurs disponibles. Si l'accès est autorisé, la liste est retournée avec
     * un statut HTTP 200 (OK).</p>
     *
     * @return Une liste de {@link AdminResponseDto} contenant les informations des administrateurs.
     */
    @GetMapping
    @Operation(summary = "Voir les administrateurs", description = "Voir la liste de tous les administrateurs en base.")
    @ApiResponse(responseCode = "201", description = "Accès authorisé")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    List<AdminResponseDto> tous(){
        return adminService.listeAdmin();
    }


    /**
     * Ajoute un administrateur dans la base de données.
     *
     * <p>Cette méthode reçoit un objet {@link AdminRequestDto} contenant les informations
     * de l'administrateur à ajouter. Si les données sont valides, l'administrateur est
     * enregistré et une réponse avec le statut HTTP 201 (Created) est retournée.
     * En cas de données invalides, une réponse HTTP 400 (Bad Request) est envoyée.</p>
     *
     * @param adminRequestDto Objet contenant les informations de l'administrateur à ajouter.
     * @return {@link ResponseEntity} avec le statut {@code 201 Created} si l'ajout est réussi.
     *         En cas d'erreur de validation, retourne {@code 400 Bad Request}.
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
