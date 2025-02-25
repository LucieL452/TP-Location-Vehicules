package com.accenture.controller;


import com.accenture.service.ClientService;
import com.accenture.service.VoitureService;
import com.accenture.service.dto.ClientRequestDto;
import com.accenture.service.dto.ClientResponseDto;
import com.accenture.service.dto.VoitureRequestDto;
import com.accenture.service.dto.VoitureResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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
    ResponseEntity<Void> ajouterVoiture(@RequestBody @Valid VoitureRequestDto voitureRequestDto){
        VoitureResponseDto voitureEnreg = voitureService.ajouterVoiture(voitureRequestDto);
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
    List<VoitureResponseDto> voitures(){
        return voitureService.trouverToutes();
    }


    @GetMapping("/{id}")
    ResponseEntity<VoitureResponseDto> voiture(@PathVariable("id") int id){

        VoitureResponseDto trouve = voitureService.trouver(id);
        return ResponseEntity.ok(trouve);

    }


}
