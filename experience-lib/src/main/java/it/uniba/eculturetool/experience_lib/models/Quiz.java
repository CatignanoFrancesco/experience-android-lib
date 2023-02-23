package it.uniba.eculturetool.experience_lib.models;

import java.util.HashSet;
import java.util.Set;

public class Quiz extends TimedExperience {
    private final Set<Question> questions = new HashSet<>();

    public Quiz(Difficulty difficulty, int points, int minutes, int seconds) {
        super(difficulty, points, minutes, seconds);
    }

    public Quiz(String id, Difficulty difficulty, int points, int minutes, int seconds) {
        super(id, difficulty, points, minutes, seconds);
    }

    public Quiz() {
        super();
    }

    public int addPoints(int points) {
        return super.points + points;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public void removeQuestionById(String questionId) {
        for(Question question : questions) {
            if(question.getId().equals(questionId)) {
                points -= question.getPoints();
                questions.remove(question);
            }
        }
    }

    public Set<Question> getQuestions() {
        return questions;
    }
}
