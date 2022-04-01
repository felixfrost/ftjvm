package com.Service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MultiplayerRepository extends JpaRepository<Multiplayer, Long> {
    String findFirstByGameIdEqualsOrderByQuestionsAsc(String gameId);

    @Query(value = "SELECT QUESTIONS FROM MULTIPLAYER WHERE GAME_ID=? LIMIT 1", nativeQuery = true)
    String getQuestionsJSONByGameId(String gameId);

    Long findFirstByGameIdEqualsOrderByIdAsc(String gameId);

    boolean existsByGameIdEquals(String gameId);

    Multiplayer findByGameIdEquals(String gameId);

    Multiplayer findByIdEquals(Long id);


}