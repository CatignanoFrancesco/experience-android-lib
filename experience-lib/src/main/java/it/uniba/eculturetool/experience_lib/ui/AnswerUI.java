package it.uniba.eculturetool.experience_lib.ui;

public class AnswerUI {
    private static AnswerUI instance;

    public final AnswerAdapterUI answerAdapterUI = new AnswerAdapterUI();

    // AnswerAdapter
    public static class AnswerAdapterUI {
        public int layout;
        public int layoutId;
        public int answerTextViewId;
    }

    private AnswerUI() {}

    public static AnswerUI getInstance() {
        if(instance == null) instance = new AnswerUI();
        return instance;
    }
}
