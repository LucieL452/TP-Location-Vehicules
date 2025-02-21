package com.accenture.repository;

import com.accenture.repository.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientDao extends JpaRepository<Client, String> {

}
