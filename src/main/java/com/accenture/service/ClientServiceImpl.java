package com.accenture.service;

import com.accenture.exception.AdresseException;
import com.accenture.exception.ClientException;
import com.accenture.repository.ClientDao;
import com.accenture.repository.entity.Client;
import com.accenture.service.dto.AdresseDto;
import com.accenture.service.dto.ClientRequestDto;
import com.accenture.service.dto.ClientResponseDto;
import com.accenture.service.mapper.ClientMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {


    private final ClientDao clientDao;
    private final ClientMapper clientMapper;


//    ====================================================================================================
//
//                                            METHODES PUBLIQUES
//
//    ====================================================================================================

    /**
     * Méthode qui retourne une liste de tous les clients
     * @return
     */

    @Override
    public List<ClientResponseDto> trouverTous() {
        return clientDao.findAll().stream()
                .map(clientMapper::toClientResponseDto)
                .toList();
    }


    /**
     * Méthode qui permet l'ajout d'un client
     * @param clientRequestDto
     * @return
     * @throws ClientException
     */

    @Override
    public ClientResponseDto ajouter(ClientRequestDto clientRequestDto) throws ClientException {

        verifierClient(clientRequestDto);

        Client client = clientMapper.toClient(clientRequestDto);
        Client clientRetour = clientDao.save(client);

        return clientMapper.toClientResponseDto(clientRetour);

    }



//    ====================================================================================================
//
//                                            METHODES PRIVEES
//
//    ====================================================================================================

    /**
     * Méthode qui vérifie les attributs du client
     * @param clientRequestDto
     * @throws ClientException
     */

    private void verifierClient(ClientRequestDto clientRequestDto) throws ClientException {
        if(clientRequestDto == null)
            throw new ClientException("Merci de remplir le formulaire");
        if (clientRequestDto.adresse() == null)
            throw new ClientException("L'adresse est obligatoire");
        if (clientRequestDto.dateNaissance() == null)
            throw new ClientException("La date de naissance est obligatoire");
        if (clientRequestDto.dateNaissance().isAfter(LocalDate.now().minusYears(18)))
            throw new ClientException("Vous devez avoir plus de 18 ans pour vous inscrire");
        if (clientRequestDto.email() == null || clientRequestDto.email().isBlank())
            throw new ClientException("Merci de saisir une adresse email");
        if (clientRequestDto.password() == null)
            throw new ClientException("Merci de saisir un mot de passe");
        if(clientRequestDto.nom() == null || clientRequestDto.nom().isBlank())
            throw new ClientException("Merci de saisir un nom");
        if(clientRequestDto.prenom() == null || clientRequestDto.prenom().isBlank())
            throw new ClientException("Merci de saisir un prenom");
        verifierAdresse(clientRequestDto.adresse());


    }

    /**
     * Méthode qui vérifie les attributs de la classe Adresse
     * @param adresseDto
     * @throws AdresseException
     */

    private void verifierAdresse(AdresseDto adresseDto) throws AdresseException {
        if(adresseDto.nomDeRue() == null || adresseDto.nomDeRue().isBlank())
            throw new AdresseException("Merci de saisir un nom de rue");
        if(adresseDto.codePostal() == null || adresseDto.codePostal().isBlank())
            throw new AdresseException("Merci de saisir un code postal");
        if(adresseDto.ville() == null || adresseDto.ville().isBlank())
            throw new AdresseException("Merci de saisir une ville");
    }




}
