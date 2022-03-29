package com.Service;

import com.Model.Question;
import com.Model.QuizCategory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ApplicationService {

    @Autowired
    RestTemplate resttemplate;
    @Autowired
    UserRepository userRepo;
    @Autowired
    HighScoreRepository hsRepo;
    @Autowired
    MultiplayerRepository mpRepo;

    private final List<QuizCategory> quizCategories = List.of(QuizCategory.values());
    private final List<Integer> quizLimits = List.of(10,25,50);

    public List<Question> getQuestions(int limit, String categories) {
        String response;
        if (categories.equals(""))
            response = resttemplate.getForObject("https://the-trivia-api.com/questions?limit=" + limit, String.class);
        else
            response = resttemplate.getForObject("https://the-trivia-api.com/questions?categories=" + categories + "&limit=" + limit, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        List<Question> questions = null;
        try {
            questions = Arrays.asList(objectMapper.readValue(response, Question[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        questions.forEach(Question::mixAnswers);

        return questions;
    }

    public List<QuizCategory> getQuizCategories() {
        return quizCategories;
    }

    public List<Integer> getQuizLimits() {
        return quizLimits;
    }

    public List<HighScore> getMonthTop(){
        List<HighScore> monthTop = hsRepo.findTop8ByDateIsAfterOrderByScoreDesc(LocalDate.now().minusMonths(1));
        return monthTop;
    }

    public List<HighScore> getWeekTop(){
        List<HighScore> weekTop = hsRepo.findTop8ByDateIsAfterOrderByScoreDesc(LocalDate.now().minusDays(7));
        if(weekTop.size()<8){
            for(int i=weekTop.size(); i<8; i++){
                weekTop.add(new HighScore(null, 0, null, new User(null, "--", null, null, null, null, null)));
            }
        }
        return weekTop;
    }

    public List<HighScore> getTodayTop(){
        List<HighScore> todayTop = hsRepo.findTop6ByDateIsOrderByScoreDesc(LocalDate.now());
        if(todayTop.size()<6){
            for(int i=todayTop.size(); i<6; i++){
                todayTop.add(new HighScore(null, 0, null, new User(null, "--", null, null, null, null, null)));
            }
        }
        return todayTop;
    }

    public List<HighScore> getTopScores(){
        List<HighScore> topScores = hsRepo.findTop10ByOrderByScoreDesc();
        if(topScores.size()<10){
            for(int i=topScores.size(); i<10; i++){
                topScores.add(new HighScore(null, 0, null, new User(null, "--", null, null, null, null, null)));
            }
        }
        return topScores;
    }

    public HighScore findUserTopScore(String username){
        HighScore userTop = hsRepo.findFirstByUser_UsernameEqualsOrderByScoreDesc(username);
        if(userTop == null){
            userTop = new HighScore(null, 0, null, null);
        }
        return userTop;
    }

    public void saveScore(HighScore hs){
        hsRepo.save(hs);
    }

    public String getFancyCategoryString(String category) {
        for (QuizCategory q : QuizCategory.values())
            if (q.getUrlString().equals(category))
                return q.getFancyString();
        return null;
    }

    public void createMultiplayerGame(String gameId, User user1, List<Question> questions){
        mpRepo.save(new Multiplayer(null, gameId, questions, new HighScore(null, 0, null, user1), null));
    }

    public List<Question> getMultiplayerQuestions(String gameId) {
        List<Question> questions = mpRepo.findFirstByGameIdEqualsOrderByQuestionsAsc(gameId);
        return questions;
    }

    public void joinMultiplayerGame(String gameId, User currentUser) {
        mpRepo.joinMultiplayerGame(currentUser, gameId);
    }
}
