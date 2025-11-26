package org.smartshop.smartshop.service.Impl;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.smartshop.smartshop.DTO.auth.LoginResponseDTO;
import org.smartshop.smartshop.DTO.client.ClientCreateDTO;
import org.smartshop.smartshop.DTO.client.ClientReadDTO;
import org.smartshop.smartshop.DTO.client.ClientUpdateDTO;
import org.smartshop.smartshop.DTO.client.ClientProfileDTO;
import org.smartshop.smartshop.entity.Client;
import org.smartshop.smartshop.entity.User;
import org.smartshop.smartshop.enums.UserRole;
import org.smartshop.smartshop.exception.DuplicateResourceException;
import org.smartshop.smartshop.exception.ResourceNotFoundException;
import org.smartshop.smartshop.exception.SessionEmtyException;
import org.smartshop.smartshop.exception.UnauthorizedException;
import org.smartshop.smartshop.mapper.ClientMapper;
import org.smartshop.smartshop.mapper.UserMapper;
import org.smartshop.smartshop.repository.ClientRepository;
import org.smartshop.smartshop.repository.OrderRepository;
import org.smartshop.smartshop.repository.UserRepository;
import org.smartshop.smartshop.service.ClientService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private  final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
    private final OrderRepository orderRepository;

    public List<ClientReadDTO> getAllClients(){
        return clientRepository.findAll().stream()
                .map(clientMapper::toReadDTO)
                .toList();
    }

    public   ClientReadDTO getClientById(Long id){
        return getAllClients().stream().filter(client->client.getId().equals(id)).
                findFirst().orElseThrow(()->new ResourceNotFoundException("client with this id  doesn t existe"));
    }
    public ClientReadDTO createClient(@Valid ClientCreateDTO clientCreateDTO){

           if (clientRepository.existsClientByEmail(clientCreateDTO.getEmail())){
                throw  new DuplicateResourceException("Ce Email deja existe");
           }

        User user = User.builder()
                .username(clientCreateDTO.getEmail())
                .password(passwordEncoder.encode(clientCreateDTO.getPassword()))
                .role(UserRole.CLIENT)
                .build();
        User savedUser = userRepository.save(user);

        Client client = Client.builder()
                .name(clientCreateDTO.getName())
                .email(clientCreateDTO.getEmail())
                .user(savedUser)
                .build();
        Client savedClient = clientRepository.save(client);
        
        return clientMapper.toReadDTO(savedClient);
    }
    public ClientReadDTO updateClient(@Valid ClientUpdateDTO clientUpdateDTO, Long id){
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client doesn't exist with this id"));
        
        clientMapper.updateEntity(clientUpdateDTO, client);
        Client savedClient = clientRepository.save(client);
        
        return clientMapper.toReadDTO(savedClient);
    }
    public  void deleteClient(Long id){
        Client client= clientRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("Client doesn't exist with this id"));

        if (!orderRepository.findAllByClient(client).isEmpty()){
            throw new UnauthorizedException("You can t delete thi client (he has comands)");
        }
        clientRepository.delete(client);
    }
    public ClientProfileDTO getProfile(HttpSession session){
        if (session.getAttribute("user") == null){
            throw new SessionEmtyException("You Should connect first with ur Account");
        }
        LoginResponseDTO user = (LoginResponseDTO) session.getAttribute("user");
        
        Client client = clientRepository.findByUser_Id(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Client profile not found"));
        
        return clientMapper.toProfileDTO(client);
    }
}
