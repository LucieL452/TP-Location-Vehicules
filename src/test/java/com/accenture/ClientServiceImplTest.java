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
    void init() {
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
    void testTrouverTous() {
        Client client1 = creerPremierClient();
        Client client2 = creerSecondClient();

        ClientResponseDto clientResponseDtoClient1 = creerPremierClientResponseDto();
        ClientResponseDto clientResponseDtoClient2 = creerSecondClientResponseDto();

        List<Client> clients = List.of(client1, client2);


        List<ClientResponseDto> dtos = List.of(clientResponseDtoClient1, clientResponseDtoClient2);

        Mockito.when(daoMock.findAll()).thenReturn(clients);
        Mockito.when(mapperMock.toClientResponseDto(client1)).thenReturn(clientResponseDtoClient1);
        Mockito.when(mapperMock.toClientResponseDto(client2)).thenReturn(clientResponseDtoClient2);
        assertEquals(dtos, service.trouverTous());

    }

    @DisplayName("""
            Test de la méthode trouver qui doit renvoyer une exception lorsque le client n'existe pas en base""")
    @Test
    void testTrouverExistePas() {
        //simulation que objet n'existe pas en base
        Mockito.when(daoMock.findById("whatever@gmail.com")).thenReturn(Optional.empty());

        //verifier que la méthode trouver(whatever@gmail.com) renvoie bien une exception
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.trouver("whatever@gmail.com"));
        assertEquals("id non présent", ex.getMessage());

    }

    @DisplayName("""
            Test de la méthode trouver qui doit renvoyer un clientResponseDto lorsque le client existe en base""")
    @Test
    void testTrouverExiste() {
        //simulation qu'un client existe en base
        Client client = creerPremierClient();
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
    void testAjouter() {
        assertThrows(ClientException.class, () -> service.ajouter(null));
    }

    @DisplayName("""
            Si ajouter(ClientRequestDto avec email null) exception levée""")
    @Test
    void testAjouterSansEmail() {
        ClientRequestDto dto = new ClientRequestDto(null, "Cc89&lizdu", "Verstappen", "Max", new AdresseDto("8 rue de la vitesse", "1008", "Amsterdam"), LocalDate.of(1997, 9, 30), null);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }

    @DisplayName("""
            Si ajouter(ClientRequestDto avec email blank) exception levée""")
    @Test
    void testAjouterEmailBlank() {
        ClientRequestDto dto = new ClientRequestDto("", "Cc89&lizdu", "Verstappen", "Max", new AdresseDto("8 rue de la vitesse", "1008", "Amsterdam"), LocalDate.of(1997, 9, 30), null);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }

    @DisplayName("""
            Si ajouter(ClientRequestDto avec nom null) exception levée""")
    @Test
    void testAjouterSansNom() {
        ClientRequestDto dto = new ClientRequestDto("max.verstappen@gmail.com", "Cc89&lizdu", null, "Max", new AdresseDto("8 rue de la vitesse", "1008", "Amsterdam"), LocalDate.of(1997, 9, 30), null);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }

    @DisplayName("""
            Si ajouter(ClientRequestDto avec nom blank) exception levée""")
    @Test
    void testAjouterNomBlank() {
        ClientRequestDto dto = new ClientRequestDto("max.verstappen@gmail.com", "Cc89&lizdu", "", "Max", new AdresseDto("8 rue de la vitesse", "1008", "Amsterdam"), LocalDate.of(1997, 9, 30), null);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }


    @DisplayName("""
            Si ajouter(ClientRequestDto avec prenom null) exception levée""")
    @Test
    void testAjouterSansPrenom() {
        ClientRequestDto dto = new ClientRequestDto("max.verstappen@gmail.com", "Cc89&lizdu", "Verstappen", null, new AdresseDto("8 rue de la vitesse", "1008", "Amsterdam"), LocalDate.of(1997, 9, 30), null);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }

    @DisplayName("""
            Si ajouter(ClientRequestDto avec prenom blank) exception levée""")
    @Test
    void testAjouterPrenomBlank() {
        ClientRequestDto dto = new ClientRequestDto("max.verstappen@gmail.com", "Cc89&lizdu", "Verstappen", "", new AdresseDto("8 rue de la vitesse", "1008", "Amsterdam"), LocalDate.of(1997, 9, 30), null);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }



    @DisplayName("""
            Si ajouter(ClientRequestDto avec date de naissance null) exception levée""")
    @Test
    void testAjouterDateNaissance() {
        ClientRequestDto dto = new ClientRequestDto("max.verstappen@gmail.com", "Cc89&lizdu", "Verstappen", "Max", new AdresseDto("8 rue de la vitesse", "1008", "Amsterdam"), null, null);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }


    @DisplayName("""
            Si ajouter(ClientRequestDto avec nom null) exception levée""")
    @Test
    void testAjouterSansMotDePasse() {
        ClientRequestDto dto = new ClientRequestDto("max.verstappen@gmail.com", null, "Verstappen", "Max", new AdresseDto("8 rue de la vitesse", "1008", "Amsterdam"), LocalDate.of(1997, 9, 30), null);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }


    @DisplayName("""
            Si ajouter(ClientRequestDto avec adresse null) exception levée""")
    @Test
    void testAjouterSansAdresse() {
        ClientRequestDto dto = new ClientRequestDto("max.verstappen@gmail.com", "Cc89&lizdu", "Verstappen", "Max", null, LocalDate.of(1997, 9, 30), null);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }

    @DisplayName("""
            Si ajouter(ClientRequestDto avec nom de rue null) exception levée""")
    @Test
    void testAjouterSansNomDeRue() {
        ClientRequestDto dto = new ClientRequestDto("max.verstappen@gmail.com", "Cc89&lizdu", "Verstappen", "Max", new AdresseDto(null, "1008", "Amsterdam"), LocalDate.of(1997, 9, 30), null);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }


    @DisplayName("""
            Si ajouter(ClientRequestDto avec nom de rue blank) exception levée""")
    @Test
    void testAjouterNomRueBlank() {
        ClientRequestDto dto = new ClientRequestDto("max.verstappen@gmail.com", "Cc89&lizdu", "Verstappen", "Max", new AdresseDto("", "1008", "Amsterdam"), LocalDate.of(1997, 9, 30), null);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }

    @DisplayName("""
            Si ajouter(ClientRequestDto avec code postal null) exception levée""")
    @Test
    void testAjouterSansCodePostal() {
        ClientRequestDto dto = new ClientRequestDto("max.verstappen@gmail.com", "Cc89&lizdu", "Verstappen", "Max", new AdresseDto("8 rue de la vitesse", null, "Amsterdam"), LocalDate.of(1997, 9, 30), null);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }

    @DisplayName("""
            Si ajouter(ClientRequestDto avec code postal blank) exception levée""")
    @Test
    void testAjouterCodePostalBlank() {
        ClientRequestDto dto = new ClientRequestDto("max.verstappen@gmail.com", "Cc89&lizdu", "Verstappen", "Max", new AdresseDto("8 rue de la vitesse", "", "Amsterdam"), LocalDate.of(1997, 9, 30), null);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }


    @DisplayName("""
            Si ajouter(ClientRequestDto avec ville null) exception levée""")
    @Test
    void testAjouterSansVille() {
        ClientRequestDto dto = new ClientRequestDto("max.verstappen@gmail.com", "Cc89&lizdu", "Verstappen", "Max", new AdresseDto("8 rue de la vitesse", "1008", null), LocalDate.of(1997, 9, 30), null);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }


    @DisplayName("""
            Si ajouter(ClientRequestDto avec ville null) exception levée""")
    @Test
    void testAjouterVilleBlank() {
        ClientRequestDto dto = new ClientRequestDto("max.verstappen@gmail.com", "Cc89&lizdu", "Verstappen", "Max", new AdresseDto("8 rue de la vitesse", "1008", ""), LocalDate.of(1997, 9, 30), null);
        assertThrows(ClientException.class, () -> service.ajouter(dto));
    }


    @DisplayName("""
            Si la méthode Ajouter est Ok, alors on appelle un save et c'est un ClientResponseDto qui est renvoyé""")
    @Test
    void testAjouterOK() {
        ClientRequestDto requestDto = new ClientRequestDto("max.verstappen@gmail.com", "Cc89&lizdu", "Verstappen", "Max", new AdresseDto("8 rue de la vitesse", "1008", "Amsterdam"), LocalDate.of(1997, 9, 30), null);
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
            On modifie le prénom, on save les changements et un clientResponseDto avec le prénom modifié est renvoyé""")
    @Test
    void TestModificationPrenom() {

        ClientRequestDto requestDto = new ClientRequestDto(null, null, null, "Olivier", null, null, null);
        Client clientNouveau = new Client(null, null, null, "Olivier", null, null, null, null);
        Client clientVrai = creerPremierClient();

        Client clientRemplace = creerPremierClient();
        clientRemplace.setPrenom("Olivier");

        ClientResponseDto responseDto = new ClientResponseDto("max.verstappen@gmail.com", "Cc89&lizdu", "Olivier", new AdresseDto("8 rue de la vitesse", "1008", "Amsterdam"), LocalDate.of(1997, 9, 30), null);


        Mockito.when(daoMock.findById("max.verstappen@gmail.com")).thenReturn(Optional.of(clientVrai));
        Mockito.when(mapperMock.toClient(requestDto)).thenReturn(clientNouveau);
        Mockito.when(daoMock.save(clientVrai)).thenReturn(clientRemplace);
        Mockito.when(mapperMock.toClientResponseDto(clientRemplace)).thenReturn(responseDto);

        assertEquals(responseDto, service.modifier("max.verstappen@gmail.com", "Cc89&lizdu", requestDto));

        Mockito.verify(daoMock, Mockito.times(1)).save(clientVrai);

        assertEquals("Olivier", clientVrai.getPrenom());

    }


    @DisplayName("""
            On modifie tous les attributs sauf le prénom, on save les changements et un clientResponseDto avec les attributs modifiés est renvoyé""")
    @Test
    void TestModificationToutSaufPrenom() {

        ClientRequestDto requestDto = new ClientRequestDto("max.verstappen@gmail.com", "rtze_FEZH89", "Hamilton", null, new AdresseDto("FastSpeed","SG1", "Stevenage"), LocalDate.of(1985, 1,7), List.of(Permis.B));
        Client clientNouveau = new Client("max.verstappen@gmail.com", "rtze_FEZH89", "Hamilton", null, new Adresse("FastSpeed","SG1", "Stevenage"), LocalDate.of(1985, 1,7), LocalDate.now(), List.of(Permis.B));
        Client clientVrai = creerPremierClient();

        Client clientRemplace = creerPremierClient();
        clientRemplace.setEmail("max.verstappen@gmail.com");
        clientRemplace.setNom("Hamilton");
        clientRemplace.setPassword("rtze_FEZH89");
        clientRemplace.setAdresse(new Adresse("FastSpeed","SG1", "Stevenage"));
        clientRemplace.setPermis(List.of(Permis.B));
        clientRemplace.setDateNaissance(LocalDate.of(1985, 1,7));
        clientRemplace.setDateInscription(LocalDate.now());

        ClientResponseDto responseDto = new ClientResponseDto("max.verstappen@gmail.com", "Hamilton", null, new AdresseDto("FastSpeed","SG1", "Stevenage"), LocalDate.of(1985, 1,7), List.of(Permis.B));


        Mockito.when(daoMock.findById("max.verstappen@gmail.com")).thenReturn(Optional.of(clientVrai));
        Mockito.when(mapperMock.toClient(requestDto)).thenReturn(clientNouveau);
        Mockito.when(daoMock.save(clientVrai)).thenReturn(clientRemplace);
        Mockito.when(mapperMock.toClientResponseDto(clientRemplace)).thenReturn(responseDto);

        assertEquals(responseDto, service.modifier("max.verstappen@gmail.com", "Cc89&lizdu", requestDto));

        Mockito.verify(daoMock, Mockito.times(1)).save(clientVrai);
        assertEquals("Hamilton", clientVrai.getNom());
        assertEquals("rtze_FEZH89", clientVrai.getPassword());
        assertEquals("max.verstappen@gmail.com", clientVrai.getEmail());
        assertEquals("Max", clientVrai.getPrenom());
        assertEquals(new Adresse("FastSpeed","SG1", "Stevenage"), clientVrai.getAdresse());
        assertEquals(List.of(Permis.B), clientVrai.getPermis());
        assertEquals(LocalDate.of(1985, 1,7), clientVrai.getDateNaissance());
        assertEquals(LocalDate.now(), clientVrai.getDateInscription());

    }


//    ==================================================================================================================
//                                            TESTS POUR LA METHODE SUPPRIMER
//    ==================================================================================================================


    @DisplayName("""
            Test de la méthode supprimer""")
    @Test
    void testSupprimerOK() {

        Client client = new Client();
        client.setEmail("max.verstappen@gmail.com");
        client.setPassword("Cc89&lizdu");

        Mockito.when(daoMock.findById("max.verstappen@gmail.com")).thenReturn(Optional.of(client));

        ClientResponseDto responseDto = creerPremierClientResponseDto();

        Mockito.when(mapperMock.toClientResponseDto(client)).thenReturn(responseDto);
        ClientResponseDto resultat = service.supprimer("max.verstappen@gmail.com", "Cc89&lizdu");

        assertNotNull(resultat);

        Mockito.verify(daoMock, Mockito.times(1)).deleteById("max.verstappen@gmail.com");


    }


//    ==================================================================================================================
//                                            TESTS POUR LA METHODE RECUPERER
//    ==================================================================================================================


    @DisplayName("""
            Test de la méthode recuperer""")
    @Test
    void testRecupererOk() {

        Client nouveauClient = new Client();
        nouveauClient.setPassword("Cc89&lizdu");
        Mockito.when(daoMock.findById("max.verstappen@gmail.com")).thenReturn(Optional.of(nouveauClient));


        ClientResponseDto responseDto = creerPremierClientResponseDto();

        Mockito.when(mapperMock.toClientResponseDto(nouveauClient)).thenReturn(responseDto);

        assertSame(responseDto, service.recuperer("max.verstappen@gmail.com", "Cc89&lizdu"));


    }

    @DisplayName("""
            Test qui vérifie que le mot de passe entré est identique à celui en base. """)
    @Test
    void testRecupererPasswordIdentiques() {
        Client client = new Client();
        client.setPassword("Cc89&lizdu");
        Mockito.when(daoMock.findById("max.verstappen@gmail.com")).thenReturn(Optional.of(client));
        assertDoesNotThrow(() -> service.recuperer("max.verstappen@gmail.com", "Cc89&lizdu"));
    }


    @DisplayName("""        
            Test qui lève une exception si le mot de passe rentré et celui en base ne concordent pas """)
    @Test
    void testRecupererPasswordDiff() {
        Client client = new Client();
        client.setPassword("lidz_88fhDZE");
        Mockito.when(daoMock.findById("max.verstappen@gmail.com")).thenReturn(Optional.of(client));
        ClientException ex = assertThrows(ClientException.class, () -> service.recuperer("max.verstappen@gmail.com", "Cc89&lizdu"));
        assertEquals("Identifiants incorrects", ex.getMessage());
    }


    @DisplayName("""
            Test qui vérifie que la méthode lève bien une exception lorsque le login fournis n'existe pas dans la basse de donnée        """)
    @Test
    void testInfosClientsMail() {
        Mockito.when(daoMock.findById("max.verstappen@gmail.com")).thenReturn(Optional.empty());
        ClientException ex = assertThrows(ClientException.class, () -> service.recuperer("max.verstappen@gmail.com", "Cc89&lizdu"));
        assertEquals("Identifiants incorrects", ex.getMessage());
        Mockito.verify(daoMock).findById("max.verstappen@gmail.com");
    }



//    =======================================================================================================
//
//
//                                                  METHODES PRIVEES
//
//
//    =======================================================================================================


    private static Client creerPremierClient() {

        Client client = new Client();
        client.setNom("Verstappen");
        client.setPrenom("Max");
        client.setPassword("Cc89&lizdu");
        client.setEmail("max.verstappen@gmail.com");
        client.setDateNaissance(LocalDate.of(1997, 9, 30));
        client.setDateInscription(LocalDate.now());
        client.setAdresse(new Adresse("8 rue de la vitesse", "1008", "Amsterdam"));
        client.setPermis(null);
        return client;

    }

    private static Client creerSecondClient() {

        Client client = new Client();
        client.setNom("Leclerc");
        client.setPrenom("Charles");
        client.setPassword("Cc89&lizdu");
        client.setEmail("charles.leclerc@gmail.com");
        client.setDateNaissance(LocalDate.of(1997, 9, 16));
        client.setDateInscription(LocalDate.now());
        client.setAdresse(new Adresse("12 rue vroom vroom", "98000", "Monaco"));
        client.setPermis(List.of(Permis.AM));
        return client;

    }

    private static ClientResponseDto creerPremierClientResponseDto() {
        return new ClientResponseDto("max.verstappen@gmail.com", "Verstappen", "Max", new AdresseDto("8 rue de la vitesse", "1008", "Amsterdam"), LocalDate.of(1997, 9, 30), null);
    }

    private static ClientResponseDto creerSecondClientResponseDto() {
        return new ClientResponseDto("charles.leclerc@gmail.com", "Leclerc", "Charles", new AdresseDto("12 rue vroom vroom", "98000", "Monaco"), LocalDate.of(1997, 9, 16), List.of(Permis.AM));
    }


}
