package com.Service;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MultiplayerHighScoreRepository extends JpaRepository<MultiplayerHighScore, Long> {

    List<MultiplayerHighScore> findByMpIdEquals(Long mpId);

    MultiplayerHighScore findByHsIdEquals(Long hsId);

}