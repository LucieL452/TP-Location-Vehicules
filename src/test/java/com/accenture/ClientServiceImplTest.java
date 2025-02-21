package com.accenture;


import com.accenture.repository.ClientDao;
import com.accenture.repository.entity.Client;
import com.accenture.service.ClientServiceImpl;
import com.accenture.service.dto.ClientResponseDto;
import com.accenture.service.mapper.ClientMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    private static Client creerPremierClient(){

        Client client = new Client();
        client.setNom("Verstappen");
        client.setPrenom("Max");
        client.set

        //TODO : Finir la méthode creerpremierclient
    }











}
