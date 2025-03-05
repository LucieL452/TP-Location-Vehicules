package com.accenture.service;


import com.accenture.exception.VoitureException;
import com.accenture.model.Permis;
import com.accenture.repository.VoitureDao;
import com.accenture.repository.entity.Voiture;
import com.accenture.service.dto.VoitureRequestDto;
import com.accenture.service.dto.VoitureResponseDto;
import com.accenture.service.mapper.VoitureMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Cette classe est là pour traiter les voitures
 */


@Service
@AllArgsConstructor
public class VoitureServiceImpl implements VoitureService {

    private static final String ID_NON_PRESENT = "id non présent";
    private final VoitureDao voitureDao;
    private final VoitureMapper voitureMapper;




//    ====================================================================================================
//
//                                            METHODES PUBLIQUES
//
//    ====================================================================================================



    /**
     * Méthode qui permet l'ajout d'une voiture
     * @param voitureRequestDto
     * @return
     * @throws VoitureException
     */
    @Override
    public VoitureResponseDto ajouter(VoitureRequestDto voitureRequestDto) throws VoitureException {

       verifier(voitureRequestDto);

        Voiture voiture = voitureMapper.toVoiture(voitureRequestDto);
        attributionPermis(voiture);

        Voiture voitureRetour = voitureDao.save(voiture);

        return voitureMapper.toVoitureResponseDto(voitureRetour);

    }

    /**
     * Méthode qui retourne une liste de toutes les voitures
     * @return
     */
    @Override
    public List<VoitureResponseDto> trouverToutes() {
        return voitureDao.findAll().stream()
                .map(voitureMapper::toVoitureResponseDto)
                .toList();
    }

    /**
     * Méthode qui retourne un objet VoitureResponseDto via id
     * @param id
     * @return
     * @throws VoitureException
     */
    @Override
    public VoitureResponseDto trouver(int id) throws VoitureException {
        Optional<Voiture> optionalVoiture = voitureDao.findById(id);
        if(optionalVoiture.isEmpty())
            throw new EntityNotFoundException(ID_NON_PRESENT);
        Voiture voiture = optionalVoiture.get();
        return voitureMapper.toVoitureResponseDto(voiture);
    }

    /**
     * Méthode qui supprime un objet voitureDao via id
     * @param id
     * @throws VoitureException
     * @throws EntityNotFoundException
     */
    @Override
    public void supprimer(int id) throws VoitureException, EntityNotFoundException {
        if(!voitureDao.existsById(id))
            throw new EntityNotFoundException(ID_NON_PRESENT);

        voitureDao.deleteById(id);


//        if(voitureDao.existsById(id))
//            voitureDao.deleteById(id);
//        else
//            throw new EntityNotFoundException(ID_NON_PRESENT);

    }

    /**
     * Méthode qui permet la modification d'un ou de tous les attributs d'un véhicule en base
     * @param id
     * @param voitureRequestDto
     * @return
     * @throws VoitureException
     * @throws EntityNotFoundException
     */
    @Override
    public VoitureResponseDto modifier(int id, VoitureRequestDto voitureRequestDto) throws VoitureException, EntityNotFoundException {

        Optional<Voiture> optionalVoiture = voitureDao.findById(id);
        if (optionalVoiture.isEmpty())
            throw new EntityNotFoundException(ID_NON_PRESENT);

        Voiture voitureExistante = optionalVoiture.get();
        Voiture voitureModif = voitureMapper.toVoiture(voitureRequestDto);
        remplacerExistantParNouveau(voitureModif, voitureExistante);
        Voiture voitureEnreg = voitureDao.save(voitureExistante);
        return voitureMapper.toVoitureResponseDto(voitureEnreg);
    }





//    ====================================================================================================
//
//                                            METHODES PRIVEES
//
//    ====================================================================================================



    /**
     * Méthode qui vérifie les attributs de la voiture
     * @param voitureRequestDto
     * @throws VoitureException
     */

    private void verifier(VoitureRequestDto voitureRequestDto) throws VoitureException {
        if(voitureRequestDto == null)
            throw new VoitureException("Merci de remplir le formulaire");
        if (voitureRequestDto.marque() == null)
            throw new VoitureException("La marque est obligatoire");
        if (voitureRequestDto.modele() == null)
            throw new VoitureException("Le modele est obligatoire");
        if (voitureRequestDto.couleur() == null)
            throw new VoitureException("La couleur est obligatoire");
        if (voitureRequestDto.nbreDePlaces() == null || voitureRequestDto.nbreDePlaces() < 1 || voitureRequestDto.nbreDePlaces() > 16)
            throw new VoitureException("Merci d'entrer un nombre de place entre 1 et 16.");
        if (voitureRequestDto.carburant() == null)
            throw new VoitureException("Merci de saisir un carburant");
        if (voitureRequestDto.typeVoiture() == null)
            throw new VoitureException("Merci de saisir un type de voiture");
        if(voitureRequestDto.nbrePortes() == null)
            throw new VoitureException("Merci de saisir un nombre de portes : 3 ou 5 portes. ");
        if(voitureRequestDto.transmission() == null)
            throw new VoitureException("Merci de saisir si la voiture est manuelle ou automatique");
        if(voitureRequestDto.climatisation() == null)
            throw new VoitureException("Merci de saisir si la climatisation est active ou non");
        if(voitureRequestDto.nbrBagages() == null || voitureRequestDto.nbrBagages() < 0 || voitureRequestDto.nbrBagages() >10 )
            throw new VoitureException("Merci de saisir un nombre de bagages compris entre 1 et 5");
    }


    /**
     *
     * Méthode qui attribue automatiquement un permis en fonction du nombre de place d'un véhicule
     * @param voiture
     */
    private void attributionPermis(Voiture voiture){

        if (voiture.getNbreDePlaces() >= 10 )
            voiture.setPermis(Permis.D1);
        else voiture.setPermis(Permis.B);

    }


    private static void remplacerExistantParNouveau(Voiture voiture, Voiture voitureExistante) {
        if (voiture.getMarque() != null)
            voitureExistante.setMarque(voiture.getMarque());
        if (voiture.getModele() != null)
            voitureExistante.setModele(voiture.getModele());
        if (voiture.getCouleur() != null)
            voitureExistante.setCouleur(voiture.getCouleur());
        if (voiture.getNbrePortes() != null)
            voitureExistante.setNbrePortes(voiture.getNbrePortes());
        if (voiture.getNbreDePlaces() != null)
            voitureExistante.setNbreDePlaces(voiture.getNbreDePlaces());
        if (voiture.getCarburant() != null)
            voitureExistante.setCarburant(voiture.getCarburant());
        if (voiture.getTypeVoiture() != null)
            voitureExistante.setTypeVoiture(voiture.getTypeVoiture());
        if (voiture.getTransmission() != null)
            voitureExistante.setTransmission(voiture.getTransmission());
        if (voiture.getClimatisation() != null)
            voitureExistante.setClimatisation(voiture.getClimatisation());
        if (voiture.getNbrBagages() != null)
            voitureExistante.setNbrBagages(voiture.getNbrBagages());
        if (voiture.getPermis() != null)
            voitureExistante.setPermis(voiture.getPermis());

    }



//TODO LOCATION ?


}
