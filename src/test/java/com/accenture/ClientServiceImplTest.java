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


//    ==================================================================================================================
//                                            TESTS POUR FIND
//    ==================================================================================================================


    @DisplayName("""
            Test de la méthode trouverTous qui doit renvoyer une liste de ClientResponseDto correspondant aux clients existants en base""")
    @Test
    void testTrouverTous(){
        Client client1 = creerPremierClient();
        Client client2 = creerSecondClient();

        ClientResponseDto clientResponseDtoClient1 = creerPremierClientResponseDto();
        ClientResponseDto clientResponseDtoClient2 = creerSecondClientResponseDto();

        List<Client> clients = List.of(client1, client2);


        List<ClientResponseDto> dtos = List.of(clientResponseDtoClient1,clientResponseDtoClient2);

        Mockito.when(daoMock.findAll()).thenReturn(clients);
        Mockito.when(mapperMock.toClientResponseDto(client1)).thenReturn(clientResponseDtoClient1);
        Mockito.when(mapperMock.toClientResponseDto(client2)).thenReturn(clientResponseDtoClient2);
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


//    ==================================================================================================================
//                                            TESTS POUR LA METHODE AJOUTER
//    ==================================================================================================================


    @DisplayName("""
            Si ajouter(null) exception levée""")
    @Test
    void testAjouter(){
        assertThrows(ClientException.class, () -> service.ajouter(null));
    }

    @DisplayName("""
            Si ajouter(ClientRequestDto avec email null) exception levée""")
    @Test
    void testAjouterSansEmail(){
        ClientRequestDto dto = new ClientRequestDto(null, "Cc89&lizdu","Verstappen","Max",new AdresseDto("8 rue de la vitesse","1008","Amsterdam"), LocalDate.of(1997,9,30), null);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }

    @DisplayName("""
            Si ajouter(ClientRequestDto avec nom null) exception levée""")
    @Test
    void testAjouterSansNom(){
        ClientRequestDto dto = new ClientRequestDto("max.verstappen@gmail.com", "Cc89&lizdu",null,"Max", new AdresseDto("8 rue de la vitesse","1008","Amsterdam"), LocalDate.of(1997,9,30), null);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }


    @DisplayName("""
            Si ajouter(ClientRequestDto avec prenom null) exception levée""")
    @Test
    void testAjouterSansPrenom(){
        ClientRequestDto dto = new ClientRequestDto("max.verstappen@gmail.com", "Cc89&lizdu","Verstappen",null, new AdresseDto("8 rue de la vitesse","1008","Amsterdam"), LocalDate.of(1997,9,30), null);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }


    @DisplayName("""
            Si ajouter(ClientRequestDto avec date de naissance null) exception levée""")
    @Test
    void testAjouterDateNaissance(){
        ClientRequestDto dto = new ClientRequestDto("max.verstappen@gmail.com", "Cc89&lizdu","Verstappen","Max", new AdresseDto("8 rue de la vitesse","1008","Amsterdam"), null, null);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }


    @DisplayName("""
            Si ajouter(ClientRequestDto avec nom null) exception levée""")
    @Test
    void testAjouterSansMotDePasse(){
        ClientRequestDto dto = new ClientRequestDto("max.verstappen@gmail.com", null,"Verstappen","Max", new AdresseDto("8 rue de la vitesse","1008","Amsterdam"), LocalDate.of(1997,9,30), null);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }


    @DisplayName("""
            Si ajouter(ClientRequestDto avec adresse null) exception levée""")
    @Test
    void testAjouterSansAdresse(){
        ClientRequestDto dto = new ClientRequestDto("max.verstappen@gmail.com", "Cc89&lizdu","Verstappen","Max", null, LocalDate.of(1997,9,30),null);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }


    @DisplayName("""
            Si la méthode Ajouter est Ok, alors on appelle un save et c'est un ClientResponseDto qui est renvoyé""")
    @Test
    void testAjouterOK(){
        ClientRequestDto requestDto = new ClientRequestDto("max.verstappen@gmail.com", "Cc89&lizdu","Verstappen","Max", new AdresseDto("8 rue de la vitesse","1008","Amsterdam"), LocalDate.of(1997,9,30), null);
        Client clientAvantEnreg = creerPremierClient();
        clientAvantEnreg.setEmail("max.verstappen@gmail.com ");

        Client clientApresEnreg = creerPremierClient();
        ClientResponseDto responseDto = creerPremierClientResponseDto();

        Mockito.when(mapperMock.toClient(requestDto)).thenReturn(clientAvantEnreg);
        Mockito.when(daoMock.save(clientAvantEnreg)).thenReturn(clientApresEnreg);
        Mockito.when(mapperMock.toClientResponseDto(clientApresEnreg)).thenReturn(responseDto);

        assertSame(responseDto, service.ajouter(requestDto));
        Mockito.verify(daoMock, Mockito.times(1)).save(clientAvantEnreg);
    }


//    ==================================================================================================================
//                                            TESTS POUR LA METHODE MODIFIER
//    ==================================================================================================================



    @DisplayName("""
            Si la méthode modif est OK, on save les changements et un clientResponseDto est renvoyé""")
    @Test
    void TestModificationOK(){

        ClientRequestDto requestDto  = new ClientRequestDto(null, null,null,"Olivier", null, null, null);
        Client clientNouveau = new Client(null, null, null, "Olivier", null, null, null, null);
        Client clientVrai = creerPremierClient();

        Client clientRemplace = creerPremierClient();
        clientRemplace.setPrenom("Olivier");

        ClientResponseDto responseDto = new ClientResponseDto("max.verstappen@gmail.com", "Cc89&lizdu","Olivier", new AdresseDto("8 rue de la vitesse","1008","Amsterdam"), LocalDate.of(1997,9,30), null);


        Mockito.when(daoMock.findById("max.verstappen@gmail.com")).thenReturn(Optional.of(clientVrai));
        Mockito.when(mapperMock.toClient(requestDto)).thenReturn(clientNouveau);
        Mockito.when(daoMock.save(clientVrai)).thenReturn(clientRemplace);
        Mockito.when(mapperMock.toClientResponseDto(clientRemplace)).thenReturn(responseDto);

        assertEquals(responseDto, service.modifier("max.verstappen@gmail.com","Cc89&lizdu", requestDto));

        Mockito.verify(daoMock, Mockito.times(1)).save(clientVrai);

    }

//    @DisplayName("""
//            Si modifier ClientRequestDto avec un nom null, alors exception levée""")
//    @Test
//    void testModificationNom(){
//        ClientRequestDto dto = new ClientRequestDto("max.verstappen@gmail.com", "Cc89&lizdu",null,"Max", new AdresseDto("8 rue de la vitesse","1008","Amsterdam"), LocalDate.of(1997,9,30), null);
//        Client client = creerPremierClient();
//        Mockito.when(daoMock.findById("max.verstappen@gmail.com")).thenReturn(Optional.of(client));
//        Mockito.when(mapperMock.toClient(dto)).thenReturn();
//
//        assertThrows(ClientException.class, () -> service.modifier("max.verstappen@gmail.com", "Cc89&lizdu", dto));
//    }
//
//
//    @DisplayName("""
//            Si modifier ClientRequestDto avec un prenom null, alors exception levée""")
//    @Test
//    void testModificationPrenom(){
//        ClientRequestDto dto = new ClientRequestDto("max.verstappen@gmail.com", "Cc89&lizdu","Verstappen",null, new AdresseDto("8 rue de la vitesse","1008","Amsterdam"), LocalDate.of(1997,9,30), null);
//        assertThrows(ClientException.class, () -> service.modifier("max.verstappen@gmail.com", "Cc89&lizdu", dto));
//    }
//
//    @DisplayName("""
//            Si modifier ClientRequestDto avec une date de naissance null, alors exception levée""")
//    @Test
//    void testModificationDateNaissance(){
//        ClientRequestDto dto = new ClientRequestDto("max.verstappen@gmail.com", "Cc89&lizdu","Verstappen","Max", new AdresseDto("8 rue de la vitesse","1008","Amsterdam"), null, null);
//        assertThrows(ClientException.class, () -> service.modifier("max.verstappen@gmail.com", "Cc89&lizdu", dto));
//    }
//
//    @DisplayName("""
//            Si modifier ClientRequestDto avec un mot de passe null, alors exception levée""")
//    @Test
//    void testModificationMotDePasse(){
//        ClientRequestDto dto = new ClientRequestDto("max.verstappen@gmail.com", null,"Verstappen","Max", new AdresseDto("8 rue de la vitesse","1008","Amsterdam"), LocalDate.of(1997,9,30), null);
//        assertThrows(ClientException.class, () -> service.modifier("max.verstappen@gmail.com", null, dto));
//    }
//    @DisplayName("""
//            Si modifier ClientRequestDto avec une date de naissance null, alors exception levée""")
//    @Test
//    void testModificationAdresse(){
//        ClientRequestDto dto = new ClientRequestDto("max.verstappen@gmail.com", "Cc89&lizdu","Verstappen","Max",null, LocalDate.of(1997,9,30), null);
//        assertThrows(ClientException.class, () -> service.modifier("max.verstappen@gmail.com", "Cc89&lizdu", dto));
//    }




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
        client.setDateNaissance(LocalDate.of(1997,9,30));
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
        client.setDateNaissance(LocalDate.of(1997,9,16));
        client.setDateInscription(LocalDate.now());
        client.setAdresse(new Adresse("12 rue vroom vroom","98000","Monaco"));
        client.setPermis(List.of(Permis.AM));
        return client;

    }

    private static ClientResponseDto creerPremierClientResponseDto(){
        return new ClientResponseDto("max.verstappen@gmail.com","Verstappen","Max",new AdresseDto("8 rue de la vitesse","1008","Amsterdam"),LocalDate.of(1997,9,30), null);
    }

    private static ClientResponseDto creerSecondClientResponseDto(){
        return new ClientResponseDto("charles.leclerc@gmail.com","Leclerc","Charles",new AdresseDto("12 rue vroom vroom","98000","Monaco"),LocalDate.of(1997,9,16),List.of(Permis.AM));
    }











}
