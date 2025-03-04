package com.accenture.controller;


import com.accenture.service.MotoService;
import com.accenture.service.dto.MotoRequestDto;
import com.accenture.service.dto.MotoResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/moto")
public class MotoController {

    final MotoService motoService;

    public MotoController(MotoService motoService) {
        this.motoService = motoService;
    }


    /**
     * Méthode qui permet d'ajouter une moto
     * @param motoRequestDto
     * @return
     */
    @PostMapping
    ResponseEntity<Void> ajouter(@RequestBody @Valid MotoRequestDto motoRequestDto){
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
     * @return
     */
    @GetMapping
    List<MotoResponseDto> motos(){
        return motoService.trouverToutes();
    }

    /**
     * Méthode qui retourne une moto par son id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    ResponseEntity<MotoResponseDto> moto(@PathVariable("id") int id){

        MotoResponseDto trouve = motoService.trouver(id);
        return ResponseEntity.ok(trouve);

    }

    /**
     * Méthode qui supprime une moto
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    ResponseEntity<MotoResponseDto> suppr(@PathVariable("id") int id){

        motoService.supprimer(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    /**
     * Méthode qui permet la modification partielle d'une moto
     * @param id
     * @param motoRequestDto
     * @return
     */
    @PatchMapping("/{id}")
    ResponseEntity<MotoResponseDto> modifier(@RequestParam int id, @RequestBody MotoRequestDto motoRequestDto){
        MotoResponseDto reponse = motoService.modifier(id, motoRequestDto);
        return ResponseEntity.ok(reponse);
    }



}
