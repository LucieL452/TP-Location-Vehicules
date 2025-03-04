package com.accenture.service;


import com.accenture.exception.MotoException;
import com.accenture.exception.VoitureException;
import com.accenture.model.Permis;
import com.accenture.model.PermisMoto;
import com.accenture.repository.MotoDao;
import com.accenture.repository.entity.Moto;
import com.accenture.repository.entity.Voiture;
import com.accenture.service.dto.MotoRequestDto;
import com.accenture.service.dto.MotoResponseDto;
import com.accenture.service.dto.VoitureRequestDto;
import com.accenture.service.mapper.MotoMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Cette classe est là pour traiter les motos
 */

@Service
@AllArgsConstructor
public class MotoServiceImpl implements MotoService {



    private static final String ID_NON_PRESENT = "id non présent";
    private final MotoDao motoDao;
    private final MotoMapper motoMapper;


//    ====================================================================================================
//
//                                            METHODES PUBLIQUES
//
//    ====================================================================================================


    /**
     * Méthode qui permet l'ajout d'une moto
     * @param motoRequestDto
     * @return
     * @throws MotoException
     */
    @Override
    public MotoResponseDto ajouter(MotoRequestDto motoRequestDto) throws MotoException {

        verifier(motoRequestDto);

        Moto moto = motoMapper.toMoto(motoRequestDto);
        attributionPermis(moto);

        Moto motoRetour = motoDao.save(moto);

        return motoMapper.toMotoResponseDto(motoRetour);

    }


    /**
     * Méthode qui retourne une liste de toutes les motos
      * @return
     */
    @Override
    public List<MotoResponseDto> trouverToutes() {
        return motoDao.findAll().stream()
                .map(motoMapper::toMotoResponseDto)
                .toList();
    }


    /**
     * Méthode qui retourne un objet MotoResponseDto via id
     * @param id
     * @return
     * @throws MotoException
     */
    @Override
    public MotoResponseDto trouver(int id) throws MotoException {
        Optional<Moto> optionalMoto = motoDao.findById(id);
        if(optionalMoto.isEmpty())
            throw new EntityNotFoundException(ID_NON_PRESENT);
        Moto moto = optionalMoto.get();
        return motoMapper.toMotoResponseDto(moto);
    }


    /**
     * Méthode qui supprime un objet motoDao via id
     * @param id
     * @throws MotoException
     * @throws EntityNotFoundException
     */
    @Override
    public void supprimer(int id) throws MotoException, EntityNotFoundException {

        if(motoDao.existsById(id))
            motoDao.deleteById(id);
        else
            throw new EntityNotFoundException(ID_NON_PRESENT);

    }

    /**
     * Méthode qui permet la modification d'un ou de tous les attributs d'une moto en base
     * @param id
     * @param motoRequestDto
     * @return
     * @throws MotoException
     * @throws EntityNotFoundException
     */
    @Override
    public MotoResponseDto modifier(int id, MotoRequestDto motoRequestDto) throws MotoException, EntityNotFoundException {
        verifier(motoRequestDto);

        Optional<Moto> optionalMoto = motoDao.findById(id);
        if (optionalMoto.isEmpty())
            throw new EntityNotFoundException(ID_NON_PRESENT);

        Moto motoExistante = optionalMoto.get();
        Moto motoModif = motoMapper.toMoto(motoRequestDto);
        remplacerExistantParNouveau(motoModif, motoExistante);
        Moto motoEnreg = motoDao.save( motoExistante);
        return motoMapper.toMotoResponseDto(motoEnreg);
    }




//    ====================================================================================================
//
//                                            METHODES PRIVEES
//
//    ====================================================================================================



    private void verifier(MotoRequestDto motoRequestDto) throws VoitureException {
        if(motoRequestDto == null)
            throw new VoitureException("Merci de remplir le formulaire");
        if (motoRequestDto.marque() == null)
            throw new VoitureException("La marque est obligatoire");
        if (motoRequestDto.modele() == null)
            throw new VoitureException("Le modele est obligatoire");
        if (motoRequestDto.couleur() == null)
            throw new VoitureException("La couleur est obligatoire");
        if (motoRequestDto.nbreCylindres() == null || motoRequestDto.nbreCylindres() <= 1 || motoRequestDto.nbreCylindres() > 7)
            throw new VoitureException("Merci d'entrer un nombre entre 1 et 6.");
        if (motoRequestDto.cylindree() == null || motoRequestDto.cylindree() <= 50)
            throw new VoitureException("Merci d'entrer un nombre correct");
        if (motoRequestDto.poids() == null)
            throw new VoitureException("Merci de saisir un poids");
        if (motoRequestDto.typeMoto() == null)
            throw new VoitureException("Merci de saisir un type de moto");
        if(motoRequestDto.hauteurSelle() == null)
            throw new VoitureException("Merci de saisir une hauteur de selle ");
        if(motoRequestDto.transmission() == null)
            throw new VoitureException("Merci de saisir si la voiture est manuelle ou automatique");

    }


    private static void remplacerExistantParNouveau(Moto moto, Moto motoExistante) {
        if (moto.getMarque() != null)
            motoExistante.setMarque(moto.getMarque());
        if (moto.getModele() != null)
            motoExistante.setModele(moto.getModele());
        if (moto.getCouleur() != null)
            motoExistante.setCouleur(moto.getCouleur());
        if (moto.getNbreCylindres() != null)
            motoExistante.setNbreCylindres(moto.getNbreCylindres());
        if (moto.getPoids() != null)
            motoExistante.setPoids(moto.getPoids());
        if (moto.getCylindree() != null)
            motoExistante.setCylindree(moto.getCylindree());
        if (moto.getPuissanceKW() != null)
            motoExistante.setPuissanceKW(moto.getPuissanceKW());
        if (moto.getTransmission() != null)
            motoExistante.setTransmission(moto.getTransmission());
        if (moto.getHauteurSelle() != null)
            motoExistante.setHauteurSelle(moto.getHauteurSelle());
        if (moto.getKilometrage() != null)
            motoExistante.setKilometrage(moto.getKilometrage());
        if (moto.getPermisMoto() != null)
            motoExistante.setPermisMoto(moto.getPermisMoto());
        if (moto.getTypeMoto() != null)
            motoExistante.setTypeMoto(moto.getTypeMoto());
        if (moto.getTarifBase() != null)
            motoExistante.setTarifBase(moto.getTarifBase());
        if (moto.getKilometrage() != null)
            motoExistante.setKilometrage(moto.getKilometrage());
        if (moto.getHorsParc() != null)
            motoExistante.setHorsParc(moto.getHorsParc());
        if (moto.getActif() != null)
            motoExistante.setActif(moto.getActif());
    }




    private void attributionPermis(Moto moto){

        if (moto.getNbreCylindres() <= 125 && moto.getPuissanceKW() < 11 )
            moto.setPermisMoto(PermisMoto.A1);
        else if (moto.getPuissanceKW() < 35 )
            moto.setPermisMoto(PermisMoto.A2);
        else moto.setPermisMoto(PermisMoto.A);

    }

    private void horsParc(Moto moto) throws MotoException{

        if (!moto.getActif()){
            supprimer(moto.getId());
        } else throw new MotoException("La moto est en location");

    }



}
