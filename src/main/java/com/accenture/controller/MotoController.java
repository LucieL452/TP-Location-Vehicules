package com.accenture.controller;


import com.accenture.service.MotoService;
import com.accenture.service.dto.MotoRequestDto;
import com.accenture.service.dto.MotoResponseDto;
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
@Tag(name = "Gestion des motos", description = "API pour la gestion des motos")
@RestController
@RequestMapping("/moto")
public class MotoController {

    final MotoService motoService;

    public MotoController(MotoService motoService) {
        this.motoService = motoService;
    }


    /**
     * Méthode qui permet d'ajouter une moto
     *
     * @param motoRequestDto
     * @return
     */
    @PostMapping
    @Operation(summary = "Ajouter une moto", description = "Ajoute une moto en base.")
    @ApiResponse(responseCode = "201", description = "Ajouté avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<Void> ajouter(@RequestBody @Valid MotoRequestDto motoRequestDto) {
        MotoResponseDto motoEnreg = motoService.ajouter(motoRequestDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(motoEnreg.id())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Méthode qui retourne une liste de toutes les motos du parc
     *
     * @return
     */
    @GetMapping
    @Operation(summary = "Voir les motos", description = "Voir la liste de toutes les motos en base.")
    @ApiResponse(responseCode = "201", description = "Accès authorisé")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    List<MotoResponseDto> motos() {
        return motoService.trouverToutes();
    }

    /**
     * Méthode qui retourne une moto par son id
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "Accéder aux infos d'une moto", description = "Voir toutes les informations sur une moto en base. ")
    @ApiResponse(responseCode = "201", description = "Accès authorisé")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<MotoResponseDto> moto(@PathVariable("id") int id) {

        MotoResponseDto trouve = motoService.trouver(id);
        return ResponseEntity.ok(trouve);

    }

    /**
     * Méthode qui supprime une moto
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une moto", description = "Supprime une moto en base.")
    @ApiResponse(responseCode = "201", description = "Moto supprimée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<MotoResponseDto> suppr(@PathVariable("id") int id) {

        motoService.supprimer(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    /**
     * Méthode qui permet la modification partielle d'une moto
     *
     * @param id
     * @param motoRequestDto
     * @return
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Modifier une moto", description = "Modifie une moto de la base.")
    @ApiResponse(responseCode = "201", description = "Moto modifiée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<MotoResponseDto> modifier(@RequestParam int id, @RequestBody MotoRequestDto motoRequestDto) {
        MotoResponseDto reponse = motoService.modifier(id, motoRequestDto);
        return ResponseEntity.ok(reponse);
    }


}
