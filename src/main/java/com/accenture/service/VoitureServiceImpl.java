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
    public VoitureResponseDto ajouterVoiture(VoitureRequestDto voitureRequestDto) throws VoitureException {

       verifierVoiture(voitureRequestDto);

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

    @Override
    public VoitureResponseDto trouver(int id) throws VoitureException {
        Optional<Voiture> optionalVoiture = voitureDao.findById(id);
        if(optionalVoiture.isEmpty())
            throw new EntityNotFoundException(ID_NON_PRESENT);
        Voiture voiture = optionalVoiture.get();
        return voitureMapper.toVoitureResponseDto(voiture);
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

    private void verifierVoiture(VoitureRequestDto voitureRequestDto) throws VoitureException {
        if(voitureRequestDto == null)
            throw new VoitureException("Merci de remplir le formulaire");
        if (voitureRequestDto.marque() == null)
            throw new VoitureException("La marque est obligatoire");
        if (voitureRequestDto.modele() == null)
            throw new VoitureException("Le modele est obligatoire");
        if (voitureRequestDto.nbreDePlaces() < 1 || voitureRequestDto.nbreDePlaces() > 16)
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
        if(voitureRequestDto.nbrBagages() < 0 || voitureRequestDto.nbrBagages() >5 )
            throw new VoitureException("Merci de saisir un nombre de bagages compris entre 1 et 5");
    }

    private void attributionPermis(Voiture voiture){

        if (voiture.getNbreDePlaces() >= 10 )
            voiture.setPermis(List.of(Permis.D1));
        else voiture.setPermis(List.of(Permis.B));

        //TODO Finir cette méthode et repartir sur les tests modifs de voiture
    }


}
