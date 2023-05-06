package it.uniba.eculturetool.experience_lib.ui;

public class PatternUI {
    private static PatternUI instance;

    public final PatternFragmentUI patternFragmentUI = new PatternFragmentUI();
    public final PatternItemUI patternItemUI = new PatternItemUI();

    public static class PatternFragmentUI {
        public int layout;
        public int toolbar;
        public int experienceFragmentContainerView;
        public int loadPatternButton;
        public int gridView;
        public int saveButton;
    }

    public static class PatternItemUI {
        public int layout;
        public int numberLayout;
        public int numberTextView;
    }


    private PatternUI() {}

    public static PatternUI getInstance() {
        if(instance == null) instance = new PatternUI();

        return instance;
    }
}

