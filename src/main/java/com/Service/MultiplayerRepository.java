package com.Service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MultiplayerRepository extends JpaRepository<Multiplayer, Long> {
    List<Question> findFirstByGameIdEqualsOrderByQuestionsAsc(String gameId);

    Long findFirstByGameIdEqualsOrderByIdAsc(String gameId);

    boolean existsByGameIdEquals(String gameId);

    @Transactional
    @Modifying
    @Query("update Multiplayer m set m.user2.user = ?1 where m.gameId = ?2")
    void joinMultiplayerGame(User user, String gameId);


}