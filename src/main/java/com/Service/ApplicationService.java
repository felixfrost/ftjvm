package com.Service;

import com.Model.QuizCategory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Autowired
    MultiplayerHighScoreRepository mpHsRepo;

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
    public String getQuestionsJSON(int limit, String categories) {
        String response;
        if (categories.equals(""))
            response = resttemplate.getForObject("https://the-trivia-api.com/questions?limit=" + limit, String.class);
        else
            response = resttemplate.getForObject("https://the-trivia-api.com/questions?categories=" + categories + "&limit=" + limit, String.class);
        return response;
    }

    public List<Question> getQuestionsFromJSON(String JSON) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Question> questions = null;
        try {
            questions = Arrays.asList(objectMapper.readValue(JSON, Question[].class));
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

    public Long saveScore(HighScore hs){
        return hsRepo.save(hs).getId();
    }

    public String getFancyCategoryString(String category) {
        for (QuizCategory q : QuizCategory.values())
            if (q.getUrlString().equals(category))
                return q.getFancyString();
        return null;
    }

    public Long createMultiplayerGame(String gameId, String questionsJSON){
        return mpRepo.save(new Multiplayer(null, gameId, questionsJSON)).getId();
    }

    public List<Question> getMultiplayerQuestions(String gameId) {
        List<Question> questions = getQuestionsFromJSON(mpRepo.getQuestionsJSONByGameId(gameId));
        return questions;
    }

    public void addMultiplayerScore(String gameId, Long hsId) {
        Multiplayer mpId = mpRepo.findByGameIdEquals(gameId);
        mpHsRepo.save(new MultiplayerHighScore(null, hsId, mpId.getId()));
    }

    public Boolean validGameId(String gameId){
        Boolean valid = mpRepo.existsByGameIdEquals(gameId);
        return valid;
    }

    public void saveUser(User user) {
        userRepo.save(user);
    }

    public User findUser(String username) {
        return userRepo.findByUsernameEquals(username);
    }

    public void saveAvatar(int num, String username) {
        User currentUser = userRepo.findByUsernameEquals(username);
        currentUser.setAvatarId(num);
        userRepo.save(currentUser);
    }

    public List<ShowMultiplayerScore> getMultiplayerScores(String username) {
        List<ShowMultiplayerScore> scores = new ArrayList<>();
        List<HighScore> myHighScores = hsRepo.findByUser_UsernameEquals(username);

        System.out.println(myHighScores);

        for (HighScore h : myHighScores) {
            if (mpHsRepo.findByHsIdEquals(h.getId()) != null) {
                scores.add(new ShowMultiplayerScore(h, null));
            }
        }
        System.out.println(scores);

        Multiplayer mp = new Multiplayer();
        List<MultiplayerHighScore> mpHs = new ArrayList<>();
        List<HighScore> matchingScores = new ArrayList<>();
        List<MultiplayerHighScore> myMultiplayerGames = myHighScores.stream().map(m -> mpHsRepo.findByHsIdEquals(m.getId())).collect(Collectors.toList());
        List<Multiplayer> myGamesIds = myMultiplayerGames.stream().map(m -> mpRepo.findByIdEquals(m.getMpId())).collect(Collectors.toList());
        List<HighScore> matchingGamesScores = new ArrayList<>();
        List<MultiplayerHighScore> matchingGames = new ArrayList<>();

        return scores;
    }
}
        /*
        int i = 0;
        for (Multiplayer m : myGamesIds) {
            mp = mpRepo.findByGameIdEquals(m.getGameId());
            System.out.println(mp);
            mpHs = mpHsRepo.findByMpIdEquals(mp.getId());
            System.out.println(mpHs);

            hsRepo.findByIdEquals(matchingGames.get(i).getHsId())


    public List<HighScore> getHostScores(String username) {
        List<HighScore> myHighScores = hsRepo.findByUsername(username);
        List<HighScore> hostMultiplayerScores = new ArrayList<>();

        for (HighScore h: myHighScores) {
            if (mpHsRepo.findByHsIdEquals(h.getId()) != null) {
                hostMultiplayerScores.add(h);
            }
        }

        return hostMultiplayerScores;
    }

    public List<HighScore> getMatchingScores(List<HighScore> currentUserHs) {
        List<HighScore> matchingScores = new ArrayList<>();
        for (HighScore h:currentUserHs) {
            if () {
                matchingScores.add(h);
            }
        }

        return matchingScores;
    }





    List<MultiplayerHighScore> mphs123 = mpHsRepo.findByMpIdEquals(mphs1.getMpId());
        for (MultiplayerHighScore mphs1: matchingGames) {
            for (:
                 ) {

            }
            matchingGames.add);
        }

        //myMultiplayerGames.stream().map(ms -> matchingGames.addAll();
        //fram hit  är allt som det ska vara, du har dina MultiplayerSpel i en lista och kan  skriva  ut gameId
        myGamesIds.forEach(m -> System.out.println(m.getGameId()));

        for (int i = 0; i < matchingGames.size(); i++) {
            System.out.println(matchingGames.get(i));
            if(hsRepo.findByIdEquals(matchingGames.get(i).getHsId()) != null) {
                matchingGamesScores.add(hsRepo.findByIdEquals(matchingGames.get(i).getHsId()));
                System.out.println(matchingGamesScores);
            }
            for (HighScore h: matchingGamesScores) {
                if (h != scores.get(i).getMyScore()) {
                    matchingScores.add(h);
                }
            }
            scores.get(i).setOtherScores(matchingScores);
        }

        matchingScores.forEach(f -> System.out.println(f.getScore()));
        scores.forEach(f -> f.getOtherScores().forEach(p -> System.out.println(p.getScore())));
        return scores;
    }
*/


