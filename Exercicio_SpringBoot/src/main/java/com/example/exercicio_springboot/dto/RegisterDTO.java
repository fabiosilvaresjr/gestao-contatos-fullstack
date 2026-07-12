package com.example.exercicio_springboot.dto;

import com.example.exercicio_springboot.entity.UserRole;
import jakarta.validation.constraints.NotBlank;

public record RegisterDTO(@NotBlank String login, @NotBlank String password, UserRole role) {}