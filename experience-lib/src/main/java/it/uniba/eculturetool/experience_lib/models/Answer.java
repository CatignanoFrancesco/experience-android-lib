package it.uniba.eculturetool.experience_lib.models;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Answer {
    private String id;
    private String answerText;
    private boolean isCorrect;

    public Answer(String id, String answerText, boolean isCorrect) {
        this.id = id;
        this.answerText = answerText;
        this.isCorrect = isCorrect;
    }

    public Answer(String answerText, boolean isCorrect) {
        this.id = UUID.randomUUID().toString();
        this.answerText = answerText;
        this.isCorrect = isCorrect;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Answer)) return false;
        Answer answer = (Answer) o;
        return id.equals(answer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

