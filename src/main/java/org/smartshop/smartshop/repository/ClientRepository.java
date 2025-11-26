package org.smartshop.smartshop.repository;

import org.smartshop.smartshop.DTO.client.ClientCreateDTO;
import org.smartshop.smartshop.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
     boolean existsClientByEmail(String email);
     boolean existsClientById(Long id);

}