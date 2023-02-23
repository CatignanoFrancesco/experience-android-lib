package it.uniba.eculturetool.experience_lib.models;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Answer {
    private String id;
    private Map<String, String> answerTexts;
    private boolean isCorrect;

    public Answer(String id, Map<String, String> answerTexts, boolean isCorrect) {
        this.id = id;
        this.answerTexts = answerTexts;
        this.isCorrect = isCorrect;
    }

    public Answer(Map<String, String> answerTexts, boolean isCorrect) {
        this.id = UUID.randomUUID().toString();
        this.answerTexts = answerTexts;
        this.isCorrect = isCorrect;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Map<String, String> getAnswerTexts() {
        return answerTexts;
    }

    public String getDefaultText() {
        if(answerTexts == null || answerTexts.isEmpty()) return "";

        String defaultLang = Locale.getDefault().getLanguage().toUpperCase();
        if(answerTexts.containsKey(defaultLang)) return answerTexts.get(defaultLang);
        else return answerTexts.entrySet().iterator().next().getValue();
    }

    public void setAnswerTexts(Map<String, String> answerTexts) {
        this.answerTexts = answerTexts;
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

