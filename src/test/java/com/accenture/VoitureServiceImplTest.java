package com.accenture;

import com.accenture.exception.VoitureException;
import com.accenture.model.*;
import com.accenture.repository.VoitureDao;
import com.accenture.repository.entity.Voiture;
import com.accenture.service.VoitureServiceImpl;
import com.accenture.service.dto.*;
import com.accenture.service.mapper.VoitureMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Captor
    ArgumentCaptor <Voiture> captor;

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
        Voiture voiture1 = creerPremiereVoiture();
        Voiture voiture2 = creerSecondeVoiture();

        VoitureResponseDto voiture1ResponseDto = creerPremiereVoitureResponseDto();
        VoitureResponseDto voiture2ResponseDto = creerSecondeVoitureResponseDto();

        List<Voiture> voitures = List.of(voiture1, voiture2);


        List<VoitureResponseDto> dtos = List.of(voiture1ResponseDto,voiture2ResponseDto);

        Mockito.when(daoMock.findAll()).thenReturn(voitures);
        Mockito.when(mapperMock.toVoitureResponseDto(voiture1)).thenReturn(voiture1ResponseDto);
        Mockito.when(mapperMock.toVoitureResponseDto(voiture2)).thenReturn(voiture2ResponseDto);
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
        Voiture voiture =  creerPremiereVoiture();
        Optional<Voiture> optionalVoiture = Optional.of(voiture);
        Mockito.when(daoMock.findById(1)).thenReturn(optionalVoiture);
        VoitureResponseDto dto = creerPremiereVoitureResponseDto();
        Mockito.when(mapperMock.toVoitureResponseDto(voiture)).thenReturn(dto);


        assertSame(dto, service.trouver(1));

    }


//    ==================================================================================================================
//                                            TESTS POUR LA METHODE AJOUTER
//    ==================================================================================================================


    @DisplayName("""
            Test de la méthode d'attribution permis B""")
    @Test
    void testAttributionPermisB(){

        VoitureRequestDto requestDto = new VoitureRequestDto("Maserati", "Grecale","rose",5, Carburant.Hybride, TypeVoiture.Berline, NbrePortes.Cinq, Transmission.AUTOMATIQUE,true,5);
        Voiture v =  creerPremiereVoiture();

        Mockito.when(mapperMock.toVoiture(requestDto)).thenReturn(v);
        service.ajouter(requestDto);
        Mockito.verify(daoMock).save(captor.capture());
        assertEquals(Permis.B, captor.getValue().getPermis());

    }

    @DisplayName("""
            Test de la méthode d'attribution permis D1""")
    @Test
    void testAttributionPermisD1(){

        VoitureRequestDto requestDto = new VoitureRequestDto("Maserati", "Grecale","rose",5, Carburant.Hybride, TypeVoiture.Berline, NbrePortes.Cinq, Transmission.AUTOMATIQUE,true,5);
        Voiture v =  creerSecondeVoiture();

        Mockito.when(mapperMock.toVoiture(requestDto)).thenReturn(v);
        service.ajouter(requestDto);
        Mockito.verify(daoMock).save(captor.capture());
        assertEquals(Permis.D1, captor.getValue().getPermis());

    }


    @DisplayName("""
            Si ajouter(null) exception levée""")
    @Test
    void testAjouter(){
        assertThrows(VoitureException.class, () -> service.ajouter(null));
    }

    @DisplayName("""
            Si ajouter(VoitureRequestDto avec marque null) exception levée""")
    @Test
    void testAjouterMarque(){
        VoitureRequestDto dto = new VoitureRequestDto(null, "Grecale","rose",5, Carburant.Hybride, TypeVoiture.Berline, NbrePortes.Cinq, Transmission.AUTOMATIQUE,true,3);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }

    @DisplayName("""
            Si ajouter(VoitureRequestDto avec modele null) exception levée""")
    @Test
    void testAjouterSansModele(){
        VoitureRequestDto dto = new VoitureRequestDto("Maserati", null,"rose",5, Carburant.Hybride, TypeVoiture.Berline, NbrePortes.Cinq, Transmission.AUTOMATIQUE,true,3);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }

    @DisplayName("""
            Si ajouter(VoitureRequestDto avec couleur null) exception levée""")
    @Test
    void testAjouterSansCouleur(){
        VoitureRequestDto dto = new VoitureRequestDto("Maserati", "Grecale",null,5, Carburant.Hybride, TypeVoiture.Berline, NbrePortes.Cinq, Transmission.AUTOMATIQUE,true,3);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }


    @DisplayName("""
            Si ajouter(VoitureRequestDto nbreDePlaces est different) exception levée""")
    @Test
    void testAjouterSansNbrePlaces(){
        VoitureRequestDto dto = new VoitureRequestDto("Maserati", "Grecale","rose",null, Carburant.Hybride, TypeVoiture.Berline, NbrePortes.Cinq, Transmission.AUTOMATIQUE,true,3);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }


    @DisplayName("""
            Si ajouter(VoitureRequestDto avec carburant null) exception levée""")
    @Test
    void testAjouterSansCarburant(){
        VoitureRequestDto dto = new VoitureRequestDto("Maserati", "Grecale","rose",5, null, TypeVoiture.Berline, NbrePortes.Cinq, Transmission.AUTOMATIQUE,true,3);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }


    @DisplayName("""
            Si ajouter(VoitureRequestDto avec typeVoiture null) exception levée""")
    @Test
    void testAjouterSansTypeVoiture(){
        VoitureRequestDto dto = new VoitureRequestDto("Maserati", "Grecale","rose",5, Carburant.Hybride, null, NbrePortes.Cinq, Transmission.AUTOMATIQUE,true,3);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }

    @DisplayName("""
            Si ajouter(VoitureRequestDto nbreDePlaces est different) exception levée""")
    @Test
    void testAjouterSansNbrePortes(){
        VoitureRequestDto dto = new VoitureRequestDto("Maserati", "Grecale","rose",5, Carburant.Hybride, TypeVoiture.Berline, null, Transmission.AUTOMATIQUE,true,3);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }


    @DisplayName("""
            Si ajouter(VoitureRequestDto avec carburant null) exception levée""")
    @Test
    void testAjouterSansTransmission(){
        VoitureRequestDto dto = new VoitureRequestDto("Maserati", "Grecale","rose",5, Carburant.Hybride, TypeVoiture.Berline, NbrePortes.Cinq, null,true,3);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }


    @DisplayName("""
            Si ajouter(VoitureRequestDto avec nom null) exception levée""")
    @Test
    void testAjouterSansClim(){
        VoitureRequestDto dto = new VoitureRequestDto("Maserati", "Grecale","rose",5, Carburant.Hybride, TypeVoiture.Berline, NbrePortes.Cinq, Transmission.AUTOMATIQUE,null,3);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }


    @DisplayName("""
            Si ajouter(VoitureRequestDto avec NbreBagages null) exception levée""")
    @Test
    void testAjouterSansNbrBagages(){
        VoitureRequestDto dto = new VoitureRequestDto("Maserati", "Grecale","rose",5, Carburant.Hybride, TypeVoiture.Berline, NbrePortes.Cinq, Transmission.AUTOMATIQUE,true,null);
        assertThrows(VoitureException.class, () -> service.ajouter(dto));
    }


    @DisplayName("""
            Si la méthode Ajouter est Ok, alors on appelle un save et c'est un VoitureResponseDto qui est renvoyé""")
    @Test
    void testAjouterOK(){

        //Creation d'une voiture requestDto (attribut dans la méthode "ajouter voiture")
        VoitureRequestDto requestDto = new VoitureRequestDto("Maserati", "Grecale","rose",5, Carburant.Hybride, TypeVoiture.Berline, NbrePortes.Cinq, Transmission.AUTOMATIQUE,true,3);

        //Creation d'un objet voiture, avec les mêmes attributs, utile dans la méthode 'toVoiture'
        Voiture voitureAvantEnreg = creerPremiereVoiture();

        //Idem, utile pour la méthode 'toVoitureResponseDto'
        Voiture voitureApresEnreg = creerPremiereVoiture();

        //Creation d'une voiture response dto, en sortie de la méthode 'toVoitureResponseDto'
        VoitureResponseDto responseDto = creerPremiereVoitureResponseDto();

        //Depuis le mapper, > toVoiture(requestDto) on retourne voitureAvantEnreg
        Mockito.when(mapperMock.toVoiture(requestDto)).thenReturn(voitureAvantEnreg);

        //Enregistrement de voitureAvantEnreg dans le dao, retourne voitureApresEnreg
        Mockito.when(daoMock.save(voitureAvantEnreg)).thenReturn(voitureApresEnreg);

        //Depuis le mapper > toVoitureResponseDto (dans l'autre sens, voitureApresEnreg), on retourne responseDto
        Mockito.when(mapperMock.toVoitureResponseDto(voitureApresEnreg)).thenReturn(responseDto);


        //méthode qui affirme si deux objets renvoient au même objet
        assertSame(responseDto, service.ajouter(requestDto));
        //on set le permis
        voitureAvantEnreg.setPermis(Permis.B);

        //vérifie que la méthode save a été appelée une fois sur l'objet voitureAvantEnreg
        Mockito.verify(daoMock, Mockito.times(1)).save(voitureAvantEnreg);
    }


//    ==================================================================================================================
//                                            TESTS POUR LA METHODE SUPPRIMER
//    ==================================================================================================================


    @DisplayName("""
            Test de la méthode supprimer""")
    @Test
    void testSupprimerOK(){

        Voiture requestDto = creerPremiereVoiture();
        requestDto.setId(1);

    //    VoitureResponseDto responseDto = creerPremiereVoitureResponseDto();

        //existsById retourne un booléen
        Mockito.when(daoMock.existsById(1)).thenReturn(true);

        service.supprimer(1);

        Mockito.verify(daoMock, Mockito.times(1)).deleteById(1);


    }



//    ==================================================================================================================
//                                            TESTS POUR LA METHODE MODIFIER
//    ==================================================================================================================


    @DisplayName("""
            Si la méthode modif est OK, on save les changements et un voitureResponseDto est renvoyé""")
    @Test
    void TestModificationOK(){

        VoitureRequestDto requestDto  = new VoitureRequestDto("Maserati", null,null,null, null, null, null,null, null, null);
        Voiture nouvelleVoiture = new Voiture();
        Voiture vraieVoiture = creerPremiereVoiture();

        Voiture voitureRemplace = creerPremiereVoiture();
        voitureRemplace.setMarque("Peugeot");

        VoitureResponseDto responseDto = new VoitureResponseDto(1, "Maserati", "Grecale","rose",null, Carburant.Hybride, TypeVoiture.Berline, Transmission.AUTOMATIQUE, NbrePortes.Cinq,true,3, Permis.B);


        Mockito.when(daoMock.findById(1)).thenReturn(Optional.of(vraieVoiture));
        Mockito.when(mapperMock.toVoiture(requestDto)).thenReturn(nouvelleVoiture);
        Mockito.when(daoMock.save(vraieVoiture)).thenReturn(voitureRemplace);
        Mockito.when(mapperMock.toVoitureResponseDto(voitureRemplace)).thenReturn(responseDto);

        assertEquals(responseDto, service.modifier(1, requestDto));

        Mockito.verify(daoMock, Mockito.times(1)).save(vraieVoiture);

    }

    //TODO : voir les deux méthodes vérifier avec Emmanuel



//    ==================================================================================================================
//                                            TESTS POUR LA METHODE MODIFIER
//    ==================================================================================================================



    @DisplayName("""
            Si modifier VoitureRequestDto avec une marque null, alors exception levée""")
    @Test
    void testModificationMarque(){
        VoitureRequestDto dto = new VoitureRequestDto(null, "Grecale","rose",5, Carburant.Hybride, TypeVoiture.Berline, NbrePortes.Cinq, Transmission.AUTOMATIQUE,true,3);
        assertThrows(VoitureException.class, () -> service.modifier(1, dto));
    }


    @DisplayName("""
            Si modifier VoitureRequestDto avec un modele null, alors exception levée""")
    @Test
    void testModificationModele(){
        VoitureRequestDto dto = new VoitureRequestDto("Maserati", null,"rose",5, Carburant.Hybride, TypeVoiture.Berline, NbrePortes.Cinq, Transmission.AUTOMATIQUE,true,3);
        assertThrows(VoitureException.class, () -> service.modifier(1, dto));
    }

    @DisplayName("""
            Si modifier VoitureRequestDto avec une couleur null, alors exception levée""")
    @Test
    void testModificationCouleur(){
        VoitureRequestDto dto = new VoitureRequestDto("Maserati", "Grecale",null,5, Carburant.Hybride, TypeVoiture.Berline, NbrePortes.Cinq, Transmission.AUTOMATIQUE,true,3);
        assertThrows(VoitureException.class, () -> service.modifier(1, dto));
    }

    @DisplayName("""
            Si modifier VoitureRequestDto avec un nbre de places null, alors exception levée""")
    @Test
    void testModificationNbrePlaces(){
        VoitureRequestDto dto = new VoitureRequestDto("Maserati", "Grecale","rose",null, Carburant.Hybride, TypeVoiture.Berline, NbrePortes.Cinq, Transmission.AUTOMATIQUE,true,3);
        assertThrows(VoitureException.class, () -> service.modifier(1, dto));
    }
    @DisplayName("""
            Si modifier VoitureRequestDto avec un carburant null, alors exception levée""")
    @Test
    void testModificationCarburant(){
        VoitureRequestDto dto = new VoitureRequestDto("Maserati", "Grecale","rose",5,null, TypeVoiture.Berline, NbrePortes.Cinq, Transmission.AUTOMATIQUE,true,3);
        assertThrows(VoitureException.class, () -> service.modifier(1, dto));
    }

    @DisplayName("""
            Si modifier VoitureRequestDto avec un type de voiture null, alors exception levée""")
    @Test
    void testModificationTypeVoiture(){
        VoitureRequestDto dto = new VoitureRequestDto("Maserati", "Grecale","rose",5, Carburant.Hybride, null, NbrePortes.Cinq, Transmission.AUTOMATIQUE,true,3);
        assertThrows(VoitureException.class, () -> service.modifier(1, dto));
    }

    @DisplayName("""
            Si modifier VoitureRequestDto avec un nbre de portes null, alors exception levée""")
    @Test
    void testModificationNbrePortes(){
        VoitureRequestDto dto = new VoitureRequestDto("Maserati", "Grecale","rose",5, Carburant.Hybride, TypeVoiture.Berline, null, Transmission.AUTOMATIQUE,true,3);
        assertThrows(VoitureException.class, () -> service.modifier(1, dto));
    }

    @DisplayName("""
            Si modifier VoitureRequestDto avec une transmission null, alors exception levée""")
    @Test
    void testModificationTransmission(){
        VoitureRequestDto dto = new VoitureRequestDto("Maserati", "Grecale","rose",5, Carburant.Hybride, TypeVoiture.Berline, NbrePortes.Cinq, null,true,3);
        assertThrows(VoitureException.class, () -> service.modifier(1, dto));
    }

    @DisplayName("""
            Si modifier VoitureRequestDto avec une climatisation null, alors exception levée""")
    @Test
    void testModificationClimatisation(){
        VoitureRequestDto dto = new VoitureRequestDto("Maserati", "Grecale","rose",5, Carburant.Hybride, TypeVoiture.Berline, NbrePortes.Cinq, Transmission.AUTOMATIQUE,null,3);
        assertThrows(VoitureException.class, () -> service.modifier(1, dto));
    }

    @DisplayName("""
            Si modifier VoitureRequestDto avec un nbre de bagages null, alors exception levée""")
    @Test
    void testModificationNbreBagages(){
        VoitureRequestDto dto = new VoitureRequestDto("Maserati", "Grecale","rose",5, Carburant.Hybride, TypeVoiture.Berline, NbrePortes.Cinq, Transmission.AUTOMATIQUE,true,null);
        assertThrows(VoitureException.class, () -> service.modifier(1, dto));
    }





//TODO tester les méthodes modifs
    //TODO Add les logs + swagger




//    =======================================================================================================
//
//
//                                                  METHODES PRIVEES
//
//
//    =======================================================================================================






    private static Voiture creerPremiereVoiture(){

        Voiture voiture = new Voiture();
        voiture.setMarque("Maserati");
        voiture.setModele("Grecale");
        voiture.setCouleur("rose");
        voiture.setNbreDePlaces(5);
        voiture.setCarburant(Carburant.Hybride);
        voiture.setTypeVoiture(TypeVoiture.Berline);
        voiture.setNbrePortes(NbrePortes.Cinq);
        voiture.setTransmission(Transmission.AUTOMATIQUE);
        voiture.setClimatisation(true);
        voiture.setNbrBagages(3);
        return voiture;

    }

        private static Voiture creerSecondeVoiture(){

        Voiture voiture = new Voiture();
        voiture.setMarque("Mercedes");
        voiture.setModele("Sprinter XXL");
        voiture.setCouleur("rose");
        voiture.setNbreDePlaces(15);
        voiture.setCarburant(Carburant.Essence);
        voiture.setTypeVoiture(TypeVoiture.Luxe);
        voiture.setNbrePortes(NbrePortes.Cinq);
        voiture.setTransmission(Transmission.AUTOMATIQUE);
        voiture.setClimatisation(true);
        voiture.setNbrBagages(10);
        return voiture;

    }

    private static VoitureResponseDto creerPremiereVoitureResponseDto(){
        return new VoitureResponseDto(1, "Maserati","Grecale","rose",5, Carburant.Hybride, TypeVoiture.Berline,Transmission.AUTOMATIQUE,NbrePortes.Cinq,true,3,Permis.B);
    }

    private static VoitureResponseDto creerSecondeVoitureResponseDto() {
        return new VoitureResponseDto(2, "Mercedes", "Sprinter XXL", "rose", 15, Carburant.Essence, TypeVoiture.Luxe, Transmission.AUTOMATIQUE, NbrePortes.Cinq, true, 10, Permis.D1);
    }





}
