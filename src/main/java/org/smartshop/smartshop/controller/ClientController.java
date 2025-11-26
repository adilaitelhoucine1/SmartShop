package org.smartshop.smartshop.controller;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.hibernate.query.Page;
import org.smartshop.smartshop.DTO.client.ClientCreateDTO;
import org.smartshop.smartshop.DTO.client.ClientProfileDTO;
import org.smartshop.smartshop.DTO.client.ClientReadDTO;
import org.smartshop.smartshop.DTO.client.ClientUpdateDTO;
import org.smartshop.smartshop.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService){
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<ClientReadDTO>> getAllClients(){
        List<ClientReadDTO> clients= clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ClientReadDTO> getClientById(@PathVariable("id") Long id){
        ClientReadDTO client=clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @PostMapping
    public ResponseEntity<ClientReadDTO> createCLient(@Valid @RequestBody ClientCreateDTO clientCreateDTO){
        ClientReadDTO client=clientService.createClient(clientCreateDTO);
        return ResponseEntity.ok(client);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientReadDTO> updateClient(@Valid @RequestBody ClientUpdateDTO clientUpdateDTO,
                                                          @PathVariable("id") Long id){
            ClientReadDTO clientReadDTO=clientService.updateClient(clientUpdateDTO,id);
            return ResponseEntity.ok(clientReadDTO);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Map<String,String>> deleteClient(@PathVariable("id") Long id){

        clientService.deleteClient(id);
        return ResponseEntity.ok(Map.of("Message","Client with id :  " +id +" is deleted seccufuly"));
    }

    @GetMapping("/profile")
    public ResponseEntity<ClientProfileDTO> getProfile(HttpSession session){
        ClientProfileDTO clientProfile= clientService.getProfile(session);
        return ResponseEntity.ok(clientProfile);
    }



}
