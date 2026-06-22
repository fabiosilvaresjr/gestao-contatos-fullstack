package com.example.exercicio_springboot.controller;

import com.example.exercicio_springboot.dto.AuthenticationDTO;
import com.example.exercicio_springboot.dto.LoginResponseDTO;
import com.example.exercicio_springboot.dto.RegisterDTO;
import com.example.exercicio_springboot.entity.Usuario;
import com.example.exercicio_springboot.repository.UsuarioRepository;
import com.example.exercicio_springboot.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
        if (this.repository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        Usuario newUsuario = new Usuario();
        newUsuario.setLogin(data.login());
        newUsuario.setPassword(encryptedPassword);
        newUsuario.setRole(data.role());

        this.repository.save(newUsuario);

        return ResponseEntity.ok().build();
    }
}