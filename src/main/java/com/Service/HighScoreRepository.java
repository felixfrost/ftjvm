package com.Service;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HighScoreRepository extends JpaRepository<HighScore, Long> {

    List<HighScore> findFirstByUser_IdEqualsOrderByScoreDesc(long id);

    List<HighScore> findByDateIsAfterOrderByScoreDesc(LocalDate date);

    List<HighScore> findByDateIsOrderByScoreDesc(LocalDate date);

    List<HighScore> findByOrderByScoreDesc();




}