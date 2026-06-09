package com.example.fortunecookie.dto;

import com.example.fortunecookie.entity.Fortune;

public record FortuneResponse(Long id, String message) {

    public static FortuneResponse from(Fortune fortune) {
        return new FortuneResponse(fortune.getId(), fortune.getMessage());
    }
}
