package com.example.fortunecookie.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fortune")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Fortune {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FortuneCategory category;

    public Fortune(String message, FortuneCategory category) {
        this.message = message;
        this.category = category;
    }
}
