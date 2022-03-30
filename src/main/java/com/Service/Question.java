package com.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Question {

    private String category;
    private String id;
    private String type;
    private String question;
    private String correctAnswer;
    private String[] incorrectAnswers;
    private String[] tags;
    private List<String> mixedAnswers = new ArrayList<>();

    public Question() { }

    public Question(String category, String id, String type, String question, String correctAnswer, String[] incorrectAnswers, String[] tags) {
        this.category = category;
        this.id = id;
        this.type = type;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public List<String> getMixedAnswers() {
        return mixedAnswers;
    }

    public void setMixedAnswers(List<String> mixed_answers) {
        this.mixedAnswers = mixed_answers;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String[] getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(String[] incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public void mixAnswers (){
        mixedAnswers.add(this.correctAnswer);
        mixedAnswers.addAll(Arrays.asList(this.incorrectAnswers));
        Collections.shuffle(mixedAnswers);
    }
}
