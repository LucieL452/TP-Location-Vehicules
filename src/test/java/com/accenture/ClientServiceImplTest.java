package com.accenture;


import com.accenture.exception.ClientException;
import com.accenture.model.Permis;
import com.accenture.repository.ClientDao;
import com.accenture.repository.entity.Adresse;
import com.accenture.repository.entity.Client;
import com.accenture.service.ClientServiceImpl;
import com.accenture.service.dto.AdresseDto;
import com.accenture.service.dto.ClientRequestDto;
import com.accenture.service.dto.ClientResponseDto;
import com.accenture.service.mapper.ClientMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)

public class ClientServiceImplTest {

    @Mock
    ClientDao daoMock;

    @Mock
    ClientMapper mapperMock;

    @InjectMocks
    ClientServiceImpl service;

    @BeforeEach()
    void init(){
        daoMock = Mockito.mock(ClientDao.class);
        mapperMock = Mockito.mock(ClientMapper.class);
        service = new ClientServiceImpl(daoMock, mapperMock);

    }

    @DisplayName("""
            Test de la méthode trouverTous qui doit renvoyer une liste de ClientResponseDto correspondant aux clients existants en base""")
    @Test
    void testTrouverTous(){
        Client Client1 = creerPremierClient();
        Client Client2 = creerSecondClient();

        ClientResponseDto clientResponseDtoClient1 = creerPremierClientResponseDto();
        ClientResponseDto clientResponseDtoClient2 = creerSecondClientResponseDto();

        List<Client> clients = List.of(Client1, Client2);


        List<ClientResponseDto> dtos = List.of(clientResponseDtoClient1,clientResponseDtoClient2);

        Mockito.when(daoMock.findAll()).thenReturn(clients);
        Mockito.when(mapperMock.toClientResponseDto(Client1)).thenReturn(clientResponseDtoClient1);
        Mockito.when(mapperMock.toClientResponseDto(Client2)).thenReturn(clientResponseDtoClient2);
        assertEquals(dtos, service.trouverTous());

    }

    @DisplayName("""
            Test de la méthode trouver qui doit renvoyer une exception lorsque le client n'existe pas en base""")
    @Test
    void testTrouverExistePas(){
        //simulation que objet n'existe pas en base
        Mockito.when(daoMock.findById("whatever@gmail.com")).thenReturn(Optional.empty());

        //verifier que la méthode trouver(whatever@gmail.com) renvoie bien une exception
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.trouver("whatever@gmail.com"));
        assertEquals("id non présent", ex.getMessage());

    }

    @DisplayName("""
            Test de la méthode trouver qui doit renvoyer un clientResponseDto lorsque le client existe en base""")
    @Test
    void testTrouverExiste(){
        //simulation qu'un client existe en base
        Client client =  creerPremierClient();
        Optional<Client> optionalClient = Optional.of(client);
        Mockito.when(daoMock.findById("max.verstappen@gmail.com")).thenReturn(optionalClient);
        ClientResponseDto dto = creerPremierClientResponseDto();
        Mockito.when(mapperMock.toClientResponseDto(client)).thenReturn(dto);


        assertSame(dto, service.trouver("max.verstappen@gmail.com"));

    }

    @DisplayName("""
            Si ajouter(null) exception levée""")
    @Test
    void testAjouter(){
        assertThrows(ClientException.class, () -> service.ajouterClient(null));
    }

    @DisplayName("""
            Si ajouter(ClientRequestDto avec email null) exception levée""")
    @Test
    void testAjouterSansEmail(){
        ClientRequestDto dto = new ClientRequestDto(null, "Cc89&lizdu","Verstappen","Max",new AdresseDto("8 rue de la vitesse","1008","Amsterdam"), LocalDate.of(1993,5,12), null);
        assertThrows(ClientException.class, () -> service.ajouterClient(dto));
    }

    @DisplayName("""
            Si ajouter(ClientRequestDto avec nom null) exception levée""")
    @Test
    void testAjouterSansNom(){
        ClientRequestDto dto = new ClientRequestDto("max.verstapper@gmail.com", "Cc89&lizdu",null,"Max", new AdresseDto("8 rue de la vitesse","1008","Amsterdam"), LocalDate.of(1993,5,12), null);
        assertThrows(ClientException.class, () -> service.ajouterClient(dto));
    }


    @DisplayName("""
            Si ajouter(ClientRequestDto avec prenom null) exception levée""")
    @Test
    void testAjouterSansPrenom(){
        ClientRequestDto dto = new ClientRequestDto("max.verstapper@gmail.com", "Cc89&lizdu","Verstappen",null, new AdresseDto("8 rue de la vitesse","1008","Amsterdam"), LocalDate.of(1993,5,12), null);
        assertThrows(ClientException.class, () -> service.ajouterClient(dto));
    }


    @DisplayName("""
            Si ajouter(ClientRequestDto avec date de naissance null) exception levée""")
    @Test
    void testAjouterDateNaissance(){
        ClientRequestDto dto = new ClientRequestDto("max.verstapper@gmail.com", "Cc89&lizdu","Verstappen","Max", new AdresseDto("8 rue de la vitesse","1008","Amsterdam"), null, null);
        assertThrows(ClientException.class, () -> service.ajouterClient(dto));
    }


    @DisplayName("""
            Si ajouter(ClientRequestDto avec nom null) exception levée""")
    @Test
    void testAjouterSansMotDePasse(){
        ClientRequestDto dto = new ClientRequestDto("max.verstapper@gmail.com", null,"Verstappen","Max", new AdresseDto("8 rue de la vitesse","1008","Amsterdam"), LocalDate.of(1993,5,12), null);
        assertThrows(ClientException.class, () -> service.ajouterClient(dto));
    }


    @DisplayName("""
            Si ajouter(ClientRequestDto avec nom null) exception levée""")
    @Test
    void testAjouterSansAdresse(){
        ClientRequestDto dto = new ClientRequestDto("max.verstapper@gmail.com", "Cc89&lizdu","Verstappen","Max", null, LocalDate.of(1993,5,12),null);
        assertThrows(ClientException.class, () -> service.ajouterClient(dto));
    }


    @DisplayName("""
            Si la méthode Ajouter est Ok, alors on appelle un save et c'est un ClientResponseDto qui est renvoyé""")
    @Test
    void testAjouterOK(){
        ClientRequestDto requestDto = new ClientRequestDto("max.verstapper@gmail.com", "Cc89&lizdu","Verstappen","Max", new AdresseDto("8 rue de la vitesse","1008","Amsterdam"), LocalDate.of(1993,5,12), null);
        Client clientAvantEnreg = creerPremierClient();
        clientAvantEnreg.setEmail("max.verstappen@gmail.com ");

        Client clientApresEnreg = creerPremierClient();
        ClientResponseDto responseDto = creerPremierClientResponseDto();

        Mockito.when(mapperMock.toClient(requestDto)).thenReturn(clientAvantEnreg);
        Mockito.when(daoMock.save(clientAvantEnreg)).thenReturn(clientApresEnreg);
        Mockito.when(mapperMock.toClientResponseDto(clientApresEnreg)).thenReturn(responseDto);

        assertSame(responseDto, service.ajouterClient(requestDto));
        Mockito.verify(daoMock, Mockito.times(1)).save(clientAvantEnreg);
    }



//    =======================================================================================================
//
//
//                                                  METHODES PRIVEES
//
//
//    =======================================================================================================






    private static Client creerPremierClient(){

        Client client = new Client();
        client.setNom("Verstappen");
        client.setPrenom("Max");
        client.setPassword("Cc89&lizdu");
        client.setEmail("max.verstappen@gmail.com");
        client.setDateNaissance(LocalDate.of(1993,5,12));
        client.setDateInscription(LocalDate.now());
        client.setAdresse(new Adresse("8 rue de la vitesse","1008","Amsterdam"));
        client.setPermis(null);
        return client;

    }

    private static Client creerSecondClient(){

        Client client = new Client();
        client.setNom("Leclerc");
        client.setPrenom("Charles");
        client.setPassword("Cc89&lizdu");
        client.setEmail("charles.leclerc@gmail.com");
        client.setDateNaissance(LocalDate.of(1996,5,12));
        client.setDateInscription(LocalDate.now());
        client.setAdresse(new Adresse("12 rue vroom vroom","98000","Monaco"));
        client.setPermis(null);
        return client;

    }

    private static ClientResponseDto creerPremierClientResponseDto(){
        return new ClientResponseDto("max.verstappen@gmail.com","Verstappen","Max",new AdresseDto("8 rue de la vitesse","1008","Amsterdam"),LocalDate.of(1993,5,12), null);
    }

    private static ClientResponseDto creerSecondClientResponseDto(){
        return new ClientResponseDto("charles.leclerc@gmail.com","Leclerc","Charles",new AdresseDto("12 rue vroom vroom","98000","Monaco"),LocalDate.of(1996,5,12),null);
    }











}
