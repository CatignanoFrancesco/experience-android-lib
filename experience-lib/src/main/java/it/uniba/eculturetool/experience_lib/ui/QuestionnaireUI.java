package it.uniba.eculturetool.experience_lib.ui;

public class QuestionnaireUI {
    private static QuestionnaireUI instance;

    public final QuestionnaireFragmentUI questionnaireFragmentUi = new QuestionnaireFragmentUI();
    public final QuestionAdapterUI questionAdapterUi = new QuestionAdapterUI();
    public final AnswerAdapterUI answerAdapterUi = new AnswerAdapterUI();

    // Lista delle domande
    public static class QuestionnaireFragmentUI {
        public int layout;
        public int toolbar;
        public int questionsRecyclerView;
        public int saveButton;
    }

    // Adapter della domanda
    public static class QuestionAdapterUI {
        public int layout;
        public int questionTextView;
        public int answersRecyclerView;
    }

    // Adapter della risposta
    public static class AnswerAdapterUI {
        public int layout;
        public int answerTextView;
    }

    private QuestionnaireUI() {}

    public static QuestionnaireUI getInstance() {
        if(instance == null) instance = new QuestionnaireUI();

        return instance;
    }
}
