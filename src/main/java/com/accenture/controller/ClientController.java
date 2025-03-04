package com.accenture.controller;

import com.accenture.service.ClientService;
import com.accenture.service.dto.ClientRequestDto;
import com.accenture.service.dto.ClientResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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
    List<ClientResponseDto> clients(){
        return clientService.trouverTous();
    }


    /**
     *Méthode qui permet d'ajouter un client
     * @param clientRequestDto
     * @return
     */
    @PostMapping
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
    ResponseEntity<ClientResponseDto> modifier(@RequestParam String email, @RequestParam String password, @RequestBody ClientRequestDto clientRequestDto){
        ClientResponseDto reponse = clientService.modifier(email, password, clientRequestDto);
        return ResponseEntity.ok(reponse);
    }










}
