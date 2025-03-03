package com.accenture.service;

import com.accenture.exception.AdresseException;
import com.accenture.exception.ClientException;
import com.accenture.repository.ClientDao;
import com.accenture.repository.entity.Client;
import com.accenture.service.dto.AdresseDto;
import com.accenture.service.dto.ClientRequestDto;
import com.accenture.service.dto.ClientResponseDto;
import com.accenture.service.mapper.ClientMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


/**
 * Cette classe est là pour traiter les clients
 */


@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {


    private static final String ID_NON_PRESENT = "id non présent";
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
     * Méthode qui permet de trouver un client à partir de son id(email)
     * @param email
     * @return
     * @throws ClientException
     */
    @Override
    public ClientResponseDto trouver(String email) throws ClientException {
        Optional<Client> optionalClient = clientDao.findById(email);
        if(optionalClient.isEmpty())
            throw new EntityNotFoundException(ID_NON_PRESENT);
        Client client = optionalClient.get();
        return clientMapper.toClientResponseDto(client);
    }

    /**
     * Méthode qui permet de récupérer les informations d'un compte si l'email et le password sont identiques à ce qu'il y a en base
     * @param email
     * @param password
     * @return
     * @throws ClientException
     */
    @Override
    public ClientResponseDto recuperer(String email, String password) throws ClientException {

        Optional<Client> optionalClient = clientDao.findById(email);
        if(optionalClient.isEmpty())
            throw new ClientException("Identifiants incorrects");

        Client client = optionalClient.get();
        if(!client.getPassword().equals(password))
            throw new ClientException("Identifiants incorrects");

        return clientMapper.toClientResponseDto(client);

        //TODO : voir avec chatGPT pour externalisation

    }


    /**
     * Méthode qui permet l'ajout d'un client
     * @param clientRequestDto
     * @return
     * @throws ClientException
     */
    @Override
    public ClientResponseDto ajouterClient(ClientRequestDto clientRequestDto) throws ClientException {

        verifierClient(clientRequestDto);

        Client client = clientMapper.toClient(clientRequestDto);
        Client clientRetour = clientDao.save(client);

        return clientMapper.toClientResponseDto(clientRetour);

    }


    /**
     * Méthode qui permet la modification d'un ou de tous les attributs d'un client en base
     * @param email
     * @param clientRequestDto
     * @return
     * @throws ClientException
     * @throws EntityNotFoundException
     */
    @Override
    public ClientResponseDto modifierClient(String email, String password, ClientRequestDto clientRequestDto) throws ClientException, EntityNotFoundException {
//        verifierClient(clientRequestDto);

        Optional<Client> optionalClient = clientDao.findById(email);
        if (optionalClient.isEmpty())
            throw new EntityNotFoundException(ID_NON_PRESENT);
        Client clientExistant = optionalClient.get();
        Client clientModif = clientMapper.toClient(clientRequestDto);
        remplacerExistantParNouveau(clientModif, clientExistant);
        Client clientEnreg = clientDao.save(clientExistant);
        return clientMapper.toClientResponseDto(clientEnreg);
    }


    /**
     * Supprime un client de la base de données
     * @param email
     * @param password
     * @throws ClientException
     * @throws EntityNotFoundException
     */
    @Override
    public void supprimerClient(String email, String password) throws ClientException, EntityNotFoundException {

        recuperationClient(email, password);
        clientDao.deleteById(email);

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

    /**
     * Méthode qui permet de récupérer les informations d'un client en base avec son adresse-email et son mot de passe (password)
     * @param email
     * @param password
     */
    private void recuperationClient(String email, String password) {

        Optional<Client> optionalClient = clientDao.findById(email);
        if(optionalClient.isEmpty())
            throw new ClientException("Identifiants incorrects");

        Client client = optionalClient.get();
        if(!client.getPassword().equals(password))
            throw new ClientException("Identifiants incorrects");

        //faire un return de l'objet
    }


    /**
     * Méthode qui permet de remplacer des attributs par des nouveaux attributs client
     * @param client
     * @param clientExistant
     */
    private static void remplacerExistantParNouveau(Client client, Client clientExistant) {
        if (client.getEmail() != null)
            clientExistant.setEmail(client.getEmail());
        if (client.getPassword() != null)
            clientExistant.setPassword(client.getPassword());
        if (client.getNom() != null)
            clientExistant.setNom(client.getNom());
        if (client.getPrenom() != null)
            clientExistant.setPrenom(client.getPrenom());
        if (client.getDateNaissance() != null)
            clientExistant.setDateNaissance(client.getDateNaissance());
        if (client.getAdresse() != null)
            clientExistant.setAdresse(client.getAdresse());

    }






}
