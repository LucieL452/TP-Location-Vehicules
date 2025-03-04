package com.accenture.controller;


import com.accenture.service.VoitureService;
import com.accenture.service.dto.VoitureRequestDto;
import com.accenture.service.dto.VoitureResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Schema
@Tag(name = "Gestion des voitures", description = "API pour la gestion des voitures")
@RestController
@RequestMapping("/voitures")
public class VoitureController {

    final VoitureService voitureService;

    public VoitureController(VoitureService voitureService) {
        this.voitureService = voitureService;
    }

    /**
     *Méthode qui permet d'ajouter une voiture
     * @param voitureRequestDto
     * @return
     */
    @PostMapping
    @Operation(summary = "Ajouter une voiture", description = "Ajoute une voiture en base.")
    @ApiResponse(responseCode = "201", description = "Ajouté avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<Void> ajouter(@RequestBody @Valid VoitureRequestDto voitureRequestDto){
        VoitureResponseDto voitureEnreg = voitureService.ajouter(voitureRequestDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(voitureEnreg.id())
                .toUri();

        return ResponseEntity.created(location).build();
    }


    /**
     * Méthode qui retourne une liste de toutes les voitures du parc
     * @return
     */
    @GetMapping
    @Operation(summary = "Voir les voitures", description = "Voir la liste de toutes les voitures en base.")
    @ApiResponse(responseCode = "201", description = "Accès authorisé")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    List<VoitureResponseDto> voitures(){
        return voitureService.trouverToutes();
    }

    /**
     * Méthode qui retourne une voiture en utilisant son id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "Accéder aux infos d'une voiture", description = "Voir toutes les informations sur une voiture en base. ")
    @ApiResponse(responseCode = "201", description = "Accès authorisé")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<VoitureResponseDto> voiture(@PathVariable("id") int id){

        VoitureResponseDto trouve = voitureService.trouver(id);
        return ResponseEntity.ok(trouve);

    }

    /**
     * Méthode qui supprime une voiture
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une voiture", description = "Supprime une voiture en base.")
    @ApiResponse(responseCode = "201", description = "Voiture supprimée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<VoitureResponseDto> suppr(@PathVariable("id") int id){

        voitureService.supprimer(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }


    /**
     * Méthode qui permet la modification partielle d'une voiture
     * @param id
     * @param voitureRequestDto
     * @return
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Modifier une voiture", description = "Modifie une voiture de la base.")
    @ApiResponse(responseCode = "201", description = "Voiture modifiée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<VoitureResponseDto> modifier(@RequestParam int id, @RequestBody VoitureRequestDto voitureRequestDto){
        VoitureResponseDto reponse = voitureService.modifier(id, voitureRequestDto);
        return ResponseEntity.ok(reponse);
    }


}
