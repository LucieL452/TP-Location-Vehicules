package com.accenture.repository;

import com.accenture.repository.entity.Adresse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdresseDao extends JpaRepository<Adresse, Integer> {
}
