package com.accenture.controller;

import com.accenture.exception.ClientException;
import com.accenture.repository.entity.Client;
import com.accenture.repository.entity.UtilisateurConnecte;
import com.accenture.service.ClientService;
import com.accenture.service.dto.ClientRequestDto;
import com.accenture.service.dto.ClientResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
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
     *
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







}
