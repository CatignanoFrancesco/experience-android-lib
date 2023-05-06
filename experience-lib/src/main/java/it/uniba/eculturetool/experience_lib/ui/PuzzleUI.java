package it.uniba.eculturetool.experience_lib.ui;

public class PuzzleUI {
    private static PuzzleUI instance;

    public final PuzzleFragmentUI puzzleFragmentUI = new PuzzleFragmentUI();

    // PuzzleFragment
    public static class PuzzleFragmentUI {
        public int layout;
        public int toolbar;
        public int experienceFragmentContainerViewId;
        public int puzzleImageViewId;
        public int explanationTextViewId;
        public int addImageButtonId;
        public int gridInputTextId;
        public int gridDimensionTextViewId;
        public int rotateSwitchId;
        public int saveButtonId;
    }


    private PuzzleUI() {}

    public static PuzzleUI getInstance() {
        if(instance == null) instance = new PuzzleUI();

        return instance;
    }
}

