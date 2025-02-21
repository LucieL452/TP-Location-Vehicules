package com.accenture.service.mapper;


import com.accenture.repository.entity.Adresse;
import com.accenture.service.dto.AdresseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdresseMapper {

    // REQUETE ALLER ( > CONTROLLER VERS ENTITY )
    Adresse toAdresse (AdresseDto adresseDto);

    // REQUETE RETOUR ( > ENTITY VERS ADRESSE(controller) )
    AdresseDto toAdresseDto (Adresse adresse);


}
