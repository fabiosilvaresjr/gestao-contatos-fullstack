package com.example.exercicio_springboot.dto;

import com.example.exercicio_springboot.entity.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}