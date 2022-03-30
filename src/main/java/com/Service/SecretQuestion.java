package com.Service;

import lombok.*;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class SecretQuestion {

    private String question;
    private String correct_answer;
    private String[] incorrect_answers;
    private List<String> mixedSecretAnswers = new ArrayList<>();

    public SecretQuestion(String question, String correct_answer, String[] incorrect_answers) {
        this.question = question;
        this.correct_answer = correct_answer;
        this.incorrect_answers = incorrect_answers;
    }

    public List<SecretQuestion> findAll() {
        List<SecretQuestion> all = new ArrayList<>();
        all.add(new SecretQuestion("Vilka av dessa har varit gruppers projekt namn?", "SirQuizAlot, Bitter, IMFDB, SneakySnakey", new String[] {"SirQuizLot, Ditter, IMSDB, Sneakysnake", "CatVikingQuiz, Birdy Fake IMDB, SnakeClone", "JustAnotherQuiz, TwitterClone, BlatentRipOff, Snake... Just snake"}));

        all.forEach(SecretQuestion::mixSecretAnswers);
        return all;
    }

    public List<String> getMixedSecretAnswers() { return mixedSecretAnswers;}


    public void mixSecretAnswers() {
        mixedSecretAnswers.add(this.correct_answer);
        mixedSecretAnswers.addAll(Arrays.asList(this.incorrect_answers));
        Collections.shuffle(mixedSecretAnswers);
    }
}
