package it.uniba.eculturetool.experience_lib.ui;

public class SingleQuestionUI {
    private static SingleQuestionUI instance;

    public final SingleQuestionEditorUI singleQuestionEditorUi = new SingleQuestionEditorUI();

    // Question editor
    public static class SingleQuestionEditorUI {
        public int layout;
        public int toolbar;
        public int experienceFragmentContainerViewId;
        public int questionEditText;
        public int addAnswerButtonId;
        public int answersRecyclerViewId;
        public int saveButtonId;
    }

    private SingleQuestionUI() {}

    public static SingleQuestionUI getInstance() {
        if(instance == null) instance = new SingleQuestionUI();
        return instance;
    }
}
