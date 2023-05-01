package it.uniba.eculturetool.experience_lib.ui;

public class SingleQuestionUI {
    private static SingleQuestionUI instance;

    public final SingleQuestionEditorUI singleQuestionEditorUi = new SingleQuestionEditorUI();
    public final AnswerAdapterUI answerAdapterUi = new AnswerAdapterUI();

    // Question editor
    public static class SingleQuestionEditorUI {
        public int layout;
        public int experienceFragmentContainerViewId;
        public int questionEditText;
        public int addAnswerButtonId;
        public int answersRecyclerViewId;
        public int saveButtonId;
    }

    // AnswerAdapter
    public static class AnswerAdapterUI {
        public int layout;
        public int layoutId;
        public int answerTextViewId;
    }

    private SingleQuestionUI() {}

    public static SingleQuestionUI getInstance() {
        if(instance == null) instance = new SingleQuestionUI();
        return instance;
    }
}
