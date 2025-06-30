package com.example.models.enums;

public enum SecurityQuestion {

    PET_QUESTION("What is your favorite pet?"),
    GAME_QUESTION("What is your favorite game?"),
    CAR_QUESTION("What is your favorite car?"),
    COLOR_QUESTION("What is your favorite color?"),
    ;

    final private String question;

    SecurityQuestion(String question) {
        this.question = question;
    }

    public static String[] listAllQuestions() {
        String[] questions = new String[SecurityQuestion.values().length];
        for (int i = 0; i < SecurityQuestion.values().length; i++) {
            questions[i] = SecurityQuestion.values()[i].question;
        }
        return questions;
    }

    public String getQuestion() {
        return question;
    }

    @Override
    public String toString() {
        return question;
    }
}
