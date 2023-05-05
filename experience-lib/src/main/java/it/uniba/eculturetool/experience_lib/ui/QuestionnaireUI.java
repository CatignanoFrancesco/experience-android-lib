package it.uniba.eculturetool.experience_lib.ui;

public class QuestionnaireUI {
    private static QuestionnaireUI instance;

    public final QuestionnaireFragmentUI questionnaireFragmentUi = new QuestionnaireFragmentUI();
    public final QuestionAdapterUI questionAdapterUi = new QuestionAdapterUI();
    public final OptionAdapterUI optionAdapterUi = new OptionAdapterUI();

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

    // Adapter dell'opzione
    public static class OptionAdapterUI {
        public int layout;
        public int optionTextView;
    }

    private QuestionnaireUI() {}

    public static QuestionnaireUI getInstance() {
        if(instance == null) instance = new QuestionnaireUI();

        return instance;
    }
}
