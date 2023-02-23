package it.uniba.eculturetool.experience_lib.ui;

public class ExperienceUI {
    private static ExperienceUI instance;

    public final ExperienceEditorFragmentUI experienceEditorFragmentUI = new ExperienceEditorFragmentUI();
    public final ExperienceAdapterUI experienceAdapterUI = new ExperienceAdapterUI();
    public final TimedExperienceFragmentUI timedExperienceFragmentUI = new TimedExperienceFragmentUI();

    // ExperienceEditorFragment
    public static class ExperienceEditorFragmentUI {
        public int layout;
        public int pointsInputTextId;
        public int pointsTextViewId;
        public int difficultySliderId;
        public int difficultyValueTextViewId;
    }

    // TimedExperienceEditor
    public static class TimedExperienceFragmentUI {
        public int layout;
        public int minutesNumberPicker;
        public int secondsNumberPicker;
    }

    // ExperienceAdapterUI
    public static class ExperienceAdapterUI {
        public int layout;
        public int layoutId;
        public int itemTypeImageId;
        public int itemTypeTextViewId;
        public int difficultyTextViewId;
        public int pointsTextViewId;
    }


    private ExperienceUI() {}

    public static ExperienceUI getInstance() {
        if(instance == null) instance = new ExperienceUI();

        return instance;
    }
}
