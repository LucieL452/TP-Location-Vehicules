package com.accenture;


import com.accenture.exception.VoitureException;
import com.accenture.exception.VoitureException;
import com.accenture.model.*;
import com.accenture.repository.ClientDao;
import com.accenture.repository.VoitureDao;
import com.accenture.repository.entity.Adresse;
import com.accenture.repository.entity.Client;
import com.accenture.repository.entity.Voiture;
import com.accenture.service.ClientServiceImpl;
import com.accenture.service.VoitureServiceImpl;
import com.accenture.service.dto.AdresseDto;
import com.accenture.service.dto.VoitureRequestDto;
import com.accenture.service.dto.ClientResponseDto;
import com.accenture.service.dto.VoitureResponseDto;
import com.accenture.service.mapper.ClientMapper;
import com.accenture.service.mapper.VoitureMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)

public class VoitureServiceImplTest {


    @Mock
    VoitureDao daoMock;

    @Mock
    VoitureMapper mapperMock;

    @InjectMocks
    VoitureServiceImpl service;

    @BeforeEach()
    void init(){
        daoMock = Mockito.mock(VoitureDao.class);
        mapperMock = Mockito.mock(VoitureMapper.class);
        service = new VoitureServiceImpl(daoMock, mapperMock);

    }


//    ==================================================================================================================
//                                            TESTS POUR FIND
//    ==================================================================================================================


    @DisplayName("""
            Test de la méthode trouverToutes qui doit renvoyer une liste de VoitureResponseDto correspondant aux voitures en base""")
    @Test
    void testTrouverToutes(){
        Client Client1 = creerPremierClient();
        Client Client2 = creerSecondClient();

        ClientResponseDto clientResponseDtoClient1 = creerPremierClientResponseDto();
        ClientResponseDto clientResponseDtoClient2 = creerSecondClientResponseDto();

        List<Client> clients = List.of(Client1, Client2);


        List<ClientResponseDto> dtos = List.of(clientResponseDtoClient1,clientResponseDtoClient2);

        Mockito.when(daoMock.findAll()).thenReturn(clients);
        Mockito.when(mapperMock.toVoitureResponseDto(Client1)).thenReturn(clientResponseDtoClient1);
        Mockito.when(mapperMock.toVoitureResponseDto((Client2)).thenReturn(clientResponseDtoClient2);
        assertEquals(dtos, service.trouverToutes());

    }

    @DisplayName("""
            Test de la méthode trouver qui doit renvoyer une exception lorsque la voiture n'existe pas en base""")
    @Test
    void testTrouverExistePas(){
        //simulation que objet n'existe pas en base
        Mockito.when(daoMock.findById(76)).thenReturn(Optional.empty());

        //verifier que la méthode trouver(whatever@gmail.com) renvoie bien une exception
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.trouver(76));
        assertEquals("id non présent", ex.getMessage());

    }

    @DisplayName("""
            Test de la méthode trouver qui doit renvoyer un clientResponseDto lorsque la voiture existe en base""")
    @Test
    void testTrouverExiste(){
        //simulation qu'une voiture existe en base
        Client client =  creerPremierClient();
        Optional<Client> optionalClient = Optional.of(client);
        Mockito.when(daoMock.findById(1)).thenReturn(optionalClient);
        ClientResponseDto dto = creerPremierClientResponseDto();
        Mockito.when(mapperMock.toVoitureResponseDto(voiture)).thenReturn(dto);


        assertSame(dto, service.trouver(1));

    }


//    ==================================================================================================================
//                                            TESTS POUR LA METHODE AJOUTER
//    ==================================================================================================================


    @DisplayName("""
            Si ajouter(null) exception levée""")
    @Test
    void testAjouter(){
        assertThrows(VoitureException.class, () -> service.ajouterVoiture(null));
    }

    @DisplayName("""
            Si ajouter(VoitureRequestDto avec marque null) exception levée""")
    @Test
    void testAjouterMarque(){
        VoitureRequestDto dto = new VoitureRequestDto(null, "Grecale","rose",5, Carburant.Hybride, TypeVoiture.Berline, NbrePortes.Cinq, Transmission.AUTOMATIQUE,true,3);
        assertThrows(VoitureException.class, () -> service.ajouterVoiture(dto));
    }

    @DisplayName("""
            Si ajouter(VoitureRequestDto avec modele null) exception levée""")
    @Test
    void testAjouterSansModele(){
        VoitureRequestDto dto = new VoitureRequestDto("Maserati", null,"rose",5, Carburant.Hybride, TypeVoiture.Berline, NbrePortes.Cinq, Transmission.AUTOMATIQUE,true,3);;
        assertThrows(VoitureException.class, () -> service.ajouterVoiture(dto));
    }


    @DisplayName("""
            Si ajouter(VoitureRequestDto avec couleur null) exception levée""")
    @Test
    void testAjouterSansCouleur(){
        VoitureRequestDto dto = new VoitureRequestDto("Maserati", "Grecale",null,5, Carburant.Hybride, TypeVoiture.Berline, NbrePortes.Cinq, Transmission.AUTOMATIQUE,true,3);
        assertThrows(VoitureException.class, () -> service.ajouterVoiture(dto));
    }


    @DisplayName("""
            Si ajouter(VoitureRequestDto nbreDePlaces est different) exception levée""")
    @Test
    void testAjouterSansNbrePlaces(){
        VoitureRequestDto dto = new VoitureRequestDto("Maserati", "Grecale","rose",3, Carburant.Hybride, TypeVoiture.Berline, NbrePortes.Cinq, Transmission.AUTOMATIQUE,true,3);
        assertThrows(VoitureException.class, () -> service.ajouterVoiture(dto));
    }


    @DisplayName("""
            Si ajouter(VoitureRequestDto avec carburant null) exception levée""")
    @Test
    void testAjouterSansCarburant(){
        VoitureRequestDto dto = new VoitureRequestDto("Maserati", "Grecale","rose",5, null, TypeVoiture.Berline, NbrePortes.Cinq, Transmission.AUTOMATIQUE,true,3);
        assertThrows(VoitureException.class, () -> service.ajouterVoiture(dto));
    }


    @DisplayName("""
            Si ajouter(VoitureRequestDto avec typeVoiture null) exception levée""")
    @Test
    void testAjouterSansTypeVoiture(){
        VoitureRequestDto dto = new VoitureRequestDto("Maserati", "Grecale","rose",5, Carburant.Hybride, null, NbrePortes.Cinq, Transmission.AUTOMATIQUE,true,3);;
        assertThrows(VoitureException.class, () -> service.ajouterVoiture(dto));
    }

    @DisplayName("""
            Si ajouter(VoitureRequestDto nbreDePlaces est different) exception levée""")
    @Test
    void testAjouterSansNbrePortes(){
        VoitureRequestDto dto = new VoitureRequestDto("Maserati", "Grecale","rose",3, Carburant.Hybride, TypeVoiture.Berline, null, Transmission.AUTOMATIQUE,true,3);
        assertThrows(VoitureException.class, () -> service.ajouterVoiture(dto));
    }


    @DisplayName("""
            Si ajouter(VoitureRequestDto avec carburant null) exception levée""")
    @Test
    void testAjouterSansTransmission(){
        VoitureRequestDto dto = new VoitureRequestDto("Maserati", "Grecale","rose",5, Carburant.Hybride, TypeVoiture.Berline, NbrePortes.Cinq, null,true,3);
        assertThrows(VoitureException.class, () -> service.ajouterVoiture(dto));
    }


    @DisplayName("""
            Si ajouter(VoitureRequestDto avec nom null) exception levée""")
    @Test
    void testAjouterSansClim(){
        VoitureRequestDto dto = new VoitureRequestDto("Maserati", "Grecale","rose",5, Carburant.Hybride, TypeVoiture.Berline, NbrePortes.Cinq, Transmission.AUTOMATIQUE,null,3);
        assertThrows(VoitureException.class, () -> service.ajouterVoiture(dto));
    }


    @DisplayName("""
            Si ajouter(VoitureRequestDto avec NbreBagages null) exception levée""")
    @Test
    void testAjouterSansNbrBagages(){
        VoitureRequestDto dto = new VoitureRequestDto("Maserati", "Grecale","rose",5, Carburant.Hybride, TypeVoiture.Berline, NbrePortes.Cinq, Transmission.AUTOMATIQUE,true,8);
        assertThrows(VoitureException.class, () -> service.ajouterVoiture(dto));
    }


    @DisplayName("""
            Si la méthode Ajouter est Ok, alors on appelle un save et c'est un VoitureResponseDto qui est renvoyé""")
    @Test
    void testAjouterOK(){
        VoitureRequestDto requestDto = new VoitureRequestDto("Maserati", "Grecale","rose",5, Carburant.Hybride, TypeVoiture.Berline, NbrePortes.Cinq, Transmission.AUTOMATIQUE,true,8);
        Voiture voitureAvantEnreg = creerPremierClient();
        voitureAvantEnreg.setId(0);

        Voiture voitureApresEnreg = creerPremierClient();
        VoitureResponseDto responseDto = creerPremierClientResponseDto();

        Mockito.when(mapperMock.toVoiture(requestDto)).thenReturn(voitureApresEnreg);
        Mockito.when(daoMock.save(voitureAvantEnreg)).thenReturn(voitureApresEnreg);
        Mockito.when(mapperMock.toVoitureResponseDto(voitureApresEnreg)).thenReturn(responseDto);

        assertSame(responseDto, service.ajouterVoiture(requestDto));
        Mockito.verify(daoMock, Mockito.times(1)).save(voitureAvantEnreg);
    }






//    =======================================================================================================
//
//
//                                                  METHODES PRIVEES
//
//
//    =======================================================================================================






    private static Client creerPremiereVoiture(){

        Voiture voiture = new Voiture();
        voiture.setNom("Verstappen");
        voiture.setPrenom("Max");
        voiture.setPassword("Cc89&lizdu");
        voiture.setEmail("max.verstappen@gmail.com");
        voiture.setDateNaissance(LocalDate.of(1993,5,12));
        voiture.setDateInscription(LocalDate.now());
        voiture.setAdresse(new Adresse("8 rue de la vitesse","1008","Amsterdam"));
        voiture.setPermis(null);
        return voiture;

    }

    private static Client creerSecondeVoiture(){

        Voiture voiture = new Voiture();
        voiture.setModele();
        voiture.setPrenom("Charles");
        voiture.setPassword("Cc89&lizdu");
        voiture.setEmail("charles.leclerc@gmail.com");
        voiture.setDateNaissance(LocalDate.of(1996,5,12));
        voiture.setDateInscription(LocalDate.now());
        voiture.setAdresse(new Adresse("12 rue vroom vroom","98000","Monaco"));
        voiture.setPermis(List.of(Permis.AM));
        return voiture;

    }

    private static VoitureResponseDto creerPremiereVoitureResponseDto(){
        return new VoitureResponseDto(1, "Maserati","Grecale","rose",5, Carburant.Hybride, TypeVoiture.Berline,Transmission.AUTOMATIQUE,NbrePortes.Cinq,true,3,List.of(Permis.B));
    }

    private static ClientResponseDto creerSecondClientResponseDto(){
        return new ClientResponseDto("charles.leclerc@gmail.com","Leclerc","Charles",new AdresseDto("12 rue vroom vroom","98000","Monaco"),LocalDate.of(1996,5,12),List.of(Permis.AM));
    }



}
