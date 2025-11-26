package org.smartshop.smartshop.service;


import jakarta.validation.Valid;
import org.smartshop.smartshop.DTO.client.ClientCreateDTO;
import org.smartshop.smartshop.DTO.client.ClientReadDTO;
import org.smartshop.smartshop.DTO.client.ClientUpdateDTO;

import java.util.List;


public interface ClientService {
    List<ClientReadDTO> getAllClients();

    ClientReadDTO createClient(@Valid ClientCreateDTO clientCreateDTO);

    ClientReadDTO updateClient(@Valid ClientUpdateDTO clientUpdateDTO, Long id);
}
