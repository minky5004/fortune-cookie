package com.example.fortunecookie.repository;

import com.example.fortunecookie.entity.Fortune;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FortuneRepository extends JpaRepository<Fortune, Long> {

    @Query(value = "SELECT * FROM fortune ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<Fortune> findRandom();
}
