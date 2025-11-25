package org.smartshop.smartshop.controller;


import jakarta.validation.Valid;
import org.smartshop.smartshop.DTO.client.ClientCreateDTO;
import org.smartshop.smartshop.DTO.client.ClientReadDTO;
import org.smartshop.smartshop.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientSerice;
    public ClientController(ClientService clientSerice){
        this.clientSerice=clientSerice;
    }

    @GetMapping
    public ResponseEntity<List<ClientReadDTO>> getAllClients(){
        List<ClientReadDTO> clients= clientSerice.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @PostMapping
    public ResponseEntity<ClientReadDTO> createCLient(@Valid @RequestBody ClientCreateDTO clientCreateDTO){
        ClientReadDTO client=clientSerice.createClient(clientCreateDTO);
        return ResponseEntity.ok(client);
    }
}
