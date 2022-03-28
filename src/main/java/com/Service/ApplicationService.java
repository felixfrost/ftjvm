package com.Service;

import com.Model.Question;
import com.Model.QuizCategory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    @Autowired
    RestTemplate resttemplate;
    @Autowired
    UserRepository userRepo;
    @Autowired
    HighScoreRepository hsRepo;

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

    public HighScore getPersonalBest (Long userId) {
        List<HighScore> hsScorez = hsRepo.findFirstByUser_IdEqualsOrderByScoreDesc(userId);
        return hsScorez.get(0);
    }

    public List<HighScore> getWeekTop(){
        List<HighScore> weekTop = hsRepo.findByDateIsAfterOrderByScoreDesc(LocalDate.now().minusDays(7));
        return weekTop;
    }

    public List<HighScore> getMonthTop(){
        List<HighScore> monthTop = hsRepo.findByDateIsAfterOrderByScoreDesc(LocalDate.now().minusMonths(1));
        return monthTop;
    }

    public List<HighScore> getTodayTop(){
        List<HighScore> todayTop = hsRepo.findByDateIsOrderByScoreDesc(LocalDate.now());
        return todayTop;
    }

    public List<HighScore> getTopScores(){
        List<HighScore> topScores = hsRepo.findByOrderByScoreDesc();
        return topScores;
    }

    public void saveScore(HighScore hs){
        hsRepo.save(hs);
    }
}
