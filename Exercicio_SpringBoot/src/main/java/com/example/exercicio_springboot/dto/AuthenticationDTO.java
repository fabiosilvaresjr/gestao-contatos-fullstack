package com.example.exercicio_springboot.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(@NotBlank String login, @NotBlank String password) {}