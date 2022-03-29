package com.Service;

import com.Model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MultiplayerRepository extends JpaRepository<Multiplayer, Long> {
    List<Question> findFirstByGameIdEqualsOrderByQuestionsAsc(String gameId);

    HighScore findFirstByGameIdEqualsOrderByUser1_ScoreAsc(String gameId);

    HighScore findFirstByGameIdEqualsOrderByUser2_ScoreAsc(String gameId);

    String findFirstByGameIdEqualsOrderByUser1_User_UsernameAsc(String gameId);

    String findFirstByGameIdEqualsOrderByUser2_User_UsernameAsc(String gameId);

    @Transactional
    @Modifying
    @Query("update Multiplayer m set m.user2.user = ?1 where m.gameId = ?3")
    void joinMultiplayerGame(User user, String gameId);




}