package com.example.exercicio_springboot;

import com.example.exercicio_springboot.entity.UserRole;
import com.example.exercicio_springboot.entity.Usuario;
import com.example.exercicio_springboot.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ExercicioSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExercicioSpringBootApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (repository.findByLogin("admin") == null) {
                Usuario user = new Usuario();
                user.setLogin("admin");
                user.setPassword(passwordEncoder.encode("123456"));

                user.setRole(UserRole.ADMIN);

                repository.save(user);
                System.out.println("✅ Usuário de teste 'admin' criado com sucesso no banco!");
            }
        };
    }
}