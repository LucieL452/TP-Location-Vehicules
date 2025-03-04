package com.accenture.controller;


import com.accenture.service.VoitureService;
import com.accenture.service.dto.VoitureRequestDto;
import com.accenture.service.dto.VoitureResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    List<VoitureResponseDto> voitures(){
        return voitureService.trouverToutes();
    }


    @GetMapping("/{id}")
    ResponseEntity<VoitureResponseDto> voiture(@PathVariable("id") int id){

        VoitureResponseDto trouve = voitureService.trouver(id);
        return ResponseEntity.ok(trouve);

    }


    @DeleteMapping("/{id}")
    ResponseEntity<VoitureResponseDto> suppr(@PathVariable("id") int id){

        voitureService.supprimer(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }


    @PatchMapping("/{id}")
    ResponseEntity<VoitureResponseDto> modifier(@RequestParam int id, @RequestBody VoitureRequestDto voitureRequestDto){
        VoitureResponseDto reponse = voitureService.modifier(id, voitureRequestDto);
        return ResponseEntity.ok(reponse);
    }


}
