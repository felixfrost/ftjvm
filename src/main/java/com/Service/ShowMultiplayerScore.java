package com.Service;

import java.util.List;

public class ShowMultiplayerScore {

    private HighScore myScore;
    private List<HighScore> otherScores;


    public ShowMultiplayerScore(){

    }

    public ShowMultiplayerScore(HighScore myScore, List<HighScore> otherScores) {
        this.myScore = myScore;
        this.otherScores = otherScores;
    }

    public HighScore getMyScore() {
        return myScore;
    }

    public void setMyScore(HighScore myScore) {
        this.myScore = myScore;
    }

    public List<HighScore> getOtherScores() {
        return otherScores;
    }

    public void setOtherScores(List<HighScore> otherScores) {
        this.otherScores = otherScores;
    }
}
