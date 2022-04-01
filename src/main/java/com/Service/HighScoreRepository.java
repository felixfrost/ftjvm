package com.Service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HighScoreRepository extends JpaRepository<HighScore, Long> {

    List<HighScore> findTop8ByDateIsAfterOrderByScoreDesc(LocalDate date);

    List<HighScore> findTop6ByDateIsOrderByScoreDesc(LocalDate date);

    List<HighScore> findTop10ByOrderByScoreDesc();

    HighScore findFirstByUser_UsernameEqualsOrderByScoreDesc(String username);

    List<HighScore> findByUser_UsernameEquals(String username);

    HighScore findByIdEquals(Long id);

    List<HighScore> findByIdEqualsOrderByDateDesc(Long id);



}