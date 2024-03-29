package it.uniba.eculturetool.experience_lib.models;

import java.util.HashSet;
import java.util.Set;

/**
 * Experience per la compatibilità contiene solo una domanda
 */
public class SingleQuestion extends TimedExperience {
    private String question;
    private Set<Answer> answers = new HashSet<>();

    public SingleQuestion(String id, Difficulty difficulty, int points, int minutes, int seconds, String question, Set<Answer> answers) {
        super(id, difficulty, points, minutes, seconds);
        this.question = question;
        this.answers = answers;
    }

    public SingleQuestion(Difficulty difficulty, int points, int minutes, int seconds, String question, Set<Answer> answers) {
        super(difficulty, points, minutes, seconds);
        this.question = question;
        this.answers = answers;
    }

    public SingleQuestion(String question, Set<Answer> answers) {
        this.question = question;
        this.answers = answers;
    }

    public SingleQuestion() {
        super();
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public int countCorrectAnswers() {
        int c = 0;
        for(Answer answer : answers) {
            if(answer.isCorrect()) c++;
        }

        return c;
    }
}
