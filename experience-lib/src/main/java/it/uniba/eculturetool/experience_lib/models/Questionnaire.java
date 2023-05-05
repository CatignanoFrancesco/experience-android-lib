package it.uniba.eculturetool.experience_lib.models;

import java.util.ArrayList;
import java.util.List;

public class Questionnaire extends Experience {
    private String name;
    private List<PreparedQuestion> preparedQuestions = new ArrayList<>();

    public Questionnaire(String id, Difficulty difficulty, int points) {
        super(id, difficulty, points);
    }

    public Questionnaire(Difficulty difficulty, int points) {
        super(difficulty, points);
    }

    public Questionnaire() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PreparedQuestion> getPreparedQuestions() {
        return preparedQuestions;
    }

    public void setPreparedQuestions(List<PreparedQuestion> preparedQuestions) {
        this.preparedQuestions = preparedQuestions;
    }
}
