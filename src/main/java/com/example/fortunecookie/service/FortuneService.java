package com.example.fortunecookie.service;

import com.example.fortunecookie.entity.Fortune;
import com.example.fortunecookie.repository.FortuneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FortuneService {

    private final FortuneRepository fortuneRepository;

    public Fortune getRandomFortune() {
        return fortuneRepository.findRandom()
                .orElseThrow(() -> new IllegalStateException("운세 데이터가 없습니다."));
    }
}
