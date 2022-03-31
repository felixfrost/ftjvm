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
        all.add(new SecretQuestion("Vilken hund har varit med oss under hela kursen?", "Fluto", new String[] {"Muto", "Kulo", "Chabo"}));
        all.add(new SecretQuestion("Vad kallar Andreas detta tecken: \"", "Dubbelfnutt", new String[] {"Citat tecken", "Dubbelfjong", "Dubbelcitat"}));
        all.add(new SecretQuestion("Vems barn har vi inte sett i Classroom?", "Vanja", new String[] {"Twana", "Rabia", "Daniel"}));
        all.add(new SecretQuestion("Vart flyttade Christopher under kursen?", "Skellefteå", new String[] {"Örnsköldsvik", "Piteå", "Lycksele"}));
        all.add(new SecretQuestion("Vilken modul hade vi dagen Amanda skulle spela på ESNS?", "Scrum", new String[] {"Presentations teknik", "Git", "Mini-project 1"}));
        all.add(new SecretQuestion("Vilken av dessa böcker har inte dykt upp i Andreas bookstore?", "A Game of Thrones av George R.R Martin", new String[]{"Pippi Långstrump av Astrid Lindgren", "Iliaden av Homeros", "Hitchhiker's guide to the galaxy av Douglas Adams"}));
        all.add(new SecretQuestion("Vem skyller Andreas på?", "Hans kollega Andreas... som definitivt existerar...", new String[]{"Molgan", "Spöket Laban", "Chicco"}));
        all.add(new SecretQuestion("Vad heter Jessicas podcast som hon har med sin man?", "Coding after work", new String[]{"Coding after dark", "After Work Coding", "Happy hour coding"}));
        all.add(new SecretQuestion("Vart bor Andreas?", "Älmhult", new String[] {"Lommarp", "Tingsryd", "Emmaboda"}));

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
