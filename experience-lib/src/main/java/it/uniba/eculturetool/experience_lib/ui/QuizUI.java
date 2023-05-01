package it.uniba.eculturetool.experience_lib.ui;

public class QuizUI {
    private static QuizUI instance;

    public final QuizFragmentUI quizFragmentUI = new QuizFragmentUI();
    public final QuestionEditorFragmentUI questionEditorFragmentUI = new QuestionEditorFragmentUI();
    public final QuizEditorFragmentUI quizEditorFragmentUI = new QuizEditorFragmentUI();
    public final QuestionAdapterUI questionAdapterUI = new QuestionAdapterUI();

    // QuizFragment
    public static class QuizFragmentUI {
        public int layout;
        public int quizEditorFragmentContainerViewId;
    }

    // QuizEditorFragment
    public static class QuizEditorFragmentUI {
        public int layout;
        public int experienceFragmentContainerViewId;
        public int addQuestionButtonId;
        public int questionsRecyclerViewId;
        public int noQuestionsTextViewId;
        public int quizSaveButtonId;
    }

    // QuestionAdapter
    public static class QuestionAdapterUI {
        public int layout;
        public int layoutId;
        public int itemImageViewId;
        public int itemQuestionTextViewId;
    }

    // QuestionEditorFragment
    public static class QuestionEditorFragmentUI {
        public int layout;
        public int pointsInputTextId;
        public int pointsTextViewId;
        public int questionInputTextId;
        public int questionImageViewId;
        public int addQuestionImageButtonId;
        public int addAnswerButtonId;
        public int answersRecyclerViewId;
        public int saveButtonId;
    }


    private QuizUI() {}

    public static QuizUI getInstance() {
        if(instance == null) instance = new QuizUI();
        return instance;
    }
}
