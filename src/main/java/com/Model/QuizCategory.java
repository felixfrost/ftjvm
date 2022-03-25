package com.Model;

public enum QuizCategory {
    ALL("", "Random Categories"),
    ARTS_LITERATURE("arts&litterature", "Arts & Literature"),
    FILM_TV("film&tv", "Film & TV"),
    FOOD_DRINK("food&drink", "Food & Drink"),
    GENERAL_KNOWLEDGE("generalknowledge", "General Knowledge"),
    GEOGRAPHY("geography", "Geography"),
    HISTORY("history", "History"),
    MUSIC("music", "Music"),
    SCIENCE("science", "Science"),
    SOCIETY_CULTURE("society&culture", "Society & Culture"),
    SPORTS_LEISURE("sports&leisure", "Sports & Leisure");

    private String urlString;
    private String fancyString;

    QuizCategory(String categoryString, String fancyString) {
        this.urlString = categoryString;
        this.fancyString = fancyString;
    }

    public String getUrlString() {
        return urlString;
    }

    public String getFancyString() {
        return fancyString;
    }

}
