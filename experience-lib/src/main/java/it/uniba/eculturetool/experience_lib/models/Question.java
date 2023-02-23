package it.uniba.eculturetool.experience_lib.models;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Question {
    private String id;
    private Map<String, String> questionTexts;
    private int points;
    private transient Bitmap image;
    private Set<Answer> answers;
    private boolean hasImage = false;

    public Question(String id, Map<String, String> questionTexts, int points, Set<Answer> answers, boolean hasImage, Bitmap image) {
        this.id = id;
        this.questionTexts = questionTexts;
        this.points = points;
        this.image = image;
        this.answers = answers;
        this.hasImage = true;
    }

    public Question(Map<String, String> questionTexts, int points, Set<Answer> answers, boolean hasImage, Bitmap image) {
        this(UUID.randomUUID().toString(), questionTexts, points, answers, hasImage, image);
    }

    public Question() {
        this(
                new HashMap<>(),
                0,
                new HashSet<>(),
                false,
                null
        );
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Map<String, String> getQuestionTexts() {
        return questionTexts;
    }

    public void setQuestionTexts(Map<String, String> questionTexts) {
        this.questionTexts = questionTexts;
    }

    public String getDefaultQuestionText() {
        if(questionTexts == null || questionTexts.isEmpty()) return "";

        if(questionTexts.containsKey(Locale.getDefault().getLanguage().toUpperCase())) return questionTexts.get(Locale.getDefault().getLanguage().toUpperCase());
        else return questionTexts.entrySet().iterator().next().getValue();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
        if(image != null) hasImage = true;
    }

    public int countCorrectAnswers() {
        int count = 0;

        for(Answer answer : answers) {
            if(answer.isCorrect()) count++;
        }

        return count;
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public boolean hasImage() {
        return hasImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question)) return false;
        Question question = (Question) o;
        return id.equals(question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
