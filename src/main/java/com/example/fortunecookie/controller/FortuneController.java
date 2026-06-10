package com.example.fortunecookie.controller;

import com.example.fortunecookie.dto.FortuneResponse;
import com.example.fortunecookie.service.FortuneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class FortuneController {

    private final FortuneService fortuneService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/api/fortune")
    @ResponseBody
    public ResponseEntity<FortuneResponse> getFortune() {
        return ResponseEntity.ok(FortuneResponse.from(fortuneService.getRandomFortune()));
    }
}
