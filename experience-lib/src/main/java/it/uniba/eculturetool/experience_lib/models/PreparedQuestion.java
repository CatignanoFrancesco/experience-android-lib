package it.uniba.eculturetool.experience_lib.models;

import java.util.ArrayList;
import java.util.List;

public class PreparedQuestion {
    private String text;
    private List<String> options = new ArrayList<>();

    public PreparedQuestion(String text, List<String> options) {
        this.text = text;
        this.options = options;
    }

    public PreparedQuestion() {}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
