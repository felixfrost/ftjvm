package com.Model;

public class Category {

    private Integer id;
    private String name;
    private Integer numOfQuestions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumOfQuestions() {
        return numOfQuestions;
    }

    public void setNumOfQuestions(Integer numOfQuestions) {
        this.numOfQuestions = numOfQuestions;
    }

    @Override
    public String toString() {
        return "id: " + id + ", name: " + name + ", Verified questions: " + numOfQuestions;
    }
}
