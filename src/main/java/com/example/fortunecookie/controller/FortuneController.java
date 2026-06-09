package com.example.fortunecookie.controller;

import com.example.fortunecookie.entity.Fortune;
import com.example.fortunecookie.service.FortuneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> getFortune() {
        Fortune fortune = fortuneService.getRandomFortune();
        return ResponseEntity.ok(Map.of(
                "id", fortune.getId(),
                "message", fortune.getMessage()
        ));
    }
}
