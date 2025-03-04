package com.accenture.controller;

import com.accenture.service.ClientService;
import com.accenture.service.dto.ClientRequestDto;
import com.accenture.service.dto.ClientResponseDto;
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
@Tag(name = "Gestion des clients", description = "API pour la gestion des clients")
@RestController
@RequestMapping("/clients")
public class ClientController {

    final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Méthode qui retourne une liste de tous les clients
     * @return
     */
    @GetMapping
    @Operation(summary = "Voir les clients", description = "Voir la liste de tous les clients en base.")
    @ApiResponse(responseCode = "201", description = "Client ajouté avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    List<ClientResponseDto> clients(){
        return clientService.trouverTous();
    }


    /**
     *Méthode qui permet d'ajouter un client
     * @param clientRequestDto
     * @return
     */
    @PostMapping
    @Operation(summary = "Ajouter un client", description = "Ajoute un client en base.")
    @ApiResponse(responseCode = "201", description = "Ajouté avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<Void> ajouterClient(@RequestBody @Valid ClientRequestDto clientRequestDto){
        ClientResponseDto clientEnreg = clientService.ajouter(clientRequestDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(clientEnreg.email())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Méthode qui permet au client de récupérer les informations d'un compte si l'email et le password sont identiques à ce que nous avons en base
     * @param email
     * @param password
     * @return
     */
    @GetMapping("/infos")
    @Operation(summary = "Accéder aux infos d'un client", description = "Donne accès aux informations d'un client en base.")
    @ApiResponse(responseCode = "201", description = "Accès authorisé")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<ClientResponseDto> monClient(@RequestParam String email, @RequestParam String password){

        ClientResponseDto trouver = clientService.recuperer(email, password);

        return ResponseEntity.ok(trouver);

    }

    /**
     * Méthode qui permet la supression d'un client
     * @param email
     * @param password
     * @return
     */
    @DeleteMapping("/infos")
    @Operation(summary = "Supprimer un client", description = "Supprime un client de la base.")
    @ApiResponse(responseCode = "201", description = "Client supprimé avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<ClientResponseDto> suppr(@RequestParam String email, @RequestParam String password){


        clientService.supprimer(email, password);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    /**
     * Méthode qui permet la modification d'un client
     * @param email
     * @param password
     * @param clientRequestDto
     * @return
     */

    @PatchMapping("/infos")
    @Operation(summary = "Modifier un client", description = "Modifie un client de la base.")
    @ApiResponse(responseCode = "201", description = "Client modifié avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    ResponseEntity<ClientResponseDto> modifier(@RequestParam String email, @RequestParam String password, @RequestBody ClientRequestDto clientRequestDto){
        ClientResponseDto reponse = clientService.modifier(email, password, clientRequestDto);
        return ResponseEntity.ok(reponse);
    }










}
