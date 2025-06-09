package com.aliba.flexiapp;

public class Word {
    private String english;
    private String turkish;
    private String level;

    public Word(String english, String turkish,String level) {
        this.english = english;
        this.turkish = turkish;
        this.level = level;
    }

    public String getEnglish() {
        return english;
    }

    public String getTurkish() {
        return turkish;
    }

    public String getLevel() {
        return level;
    }
}
