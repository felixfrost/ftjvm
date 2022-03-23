package com.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionList {

    private List<Question> questions;

    public QuestionList() {

    }
    
    public QuestionList(List<Question> questions) {
        this.questions = questions;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
