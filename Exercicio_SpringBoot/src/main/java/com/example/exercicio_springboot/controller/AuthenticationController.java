package com.example.exercicio_springboot.controller;

import com.example.exercicio_springboot.dto.AuthenticationDTO;
import com.example.exercicio_springboot.dto.LoginResponseDTO;
import com.example.exercicio_springboot.dto.RegisterDTO;
import com.example.exercicio_springboot.entity.UserRole;
import com.example.exercicio_springboot.entity.Usuario;
import com.example.exercicio_springboot.repository.UsuarioRepository;
import com.example.exercicio_springboot.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder; // Import da interface correta
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterDTO data) {
        if (this.repository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = passwordEncoder.encode(data.password());

        Usuario newUsuario = new Usuario();
        newUsuario.setLogin(data.login());
        newUsuario.setPassword(encryptedPassword);

        newUsuario.setRole(UserRole.USER);

        this.repository.save(newUsuario);

        return ResponseEntity.ok().build();
    }
}