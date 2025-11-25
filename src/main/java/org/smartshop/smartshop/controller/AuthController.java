package org.smartshop.smartshop.controller;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.smartshop.smartshop.DTO.auth.LoginRequestDTO;
import org.smartshop.smartshop.DTO.auth.LoginResponseDTO;
import org.smartshop.smartshop.service.Impl.UserServiceImpl;
import org.smartshop.smartshop.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")


public class AuthController {

     private  final UserService userService;

     public AuthController(UserServiceImpl userService) {
         this.userService = userService;
     }

        @GetMapping("/test")
        public String login() {
            return "login";
        }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
          @Valid @RequestBody LoginRequestDTO requestDTO,
          HttpSession session
    ){
        LoginResponseDTO login = userService.authenticate(requestDTO);
        session.setAttribute("user", login);
        return ResponseEntity.ok(login);
     }


     @PostMapping("/logout")
    public  ResponseEntity<Map<String,String>> logout(  HttpSession session){
         session.invalidate();
         return ResponseEntity.ok(Map.of("message","Logout Done"));
     }

     @GetMapping("/sesion")

    public ResponseEntity<String> getSession(HttpSession session){
         if (session.getAttribute("user")==null){
             return ResponseEntity.ok("session Khawiiiiiiiiiiiiiiiiiiii");
         }
         return ResponseEntity.ok("session 3aaaaaaaaaaaaaaaaaaaaaaamerrrrrrrr");
     }


}
