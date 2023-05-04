package it.uniba.eculturetool.experience_lib.ui;


public class RecognizeTheObjectUI {
    private static RecognizeTheObjectUI instance;

    public final RecognizeTheObjectFieldsUI recognizeTheObjectFieldsUi = new RecognizeTheObjectFieldsUI();

    public static class RecognizeTheObjectFieldsUI {
        public int layout;
        public int experienceFragmentContainerView;
        public int referenceImageView;
        public int addReferenceImageButton;
        public int descriptionEditText;
        public int modelNameEditText;
    }

    private RecognizeTheObjectUI() {}

    public static RecognizeTheObjectUI getInstance() {
        if(instance == null) instance = new RecognizeTheObjectUI();
        return instance;
    }
}
