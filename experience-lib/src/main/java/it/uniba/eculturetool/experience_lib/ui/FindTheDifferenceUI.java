package it.uniba.eculturetool.experience_lib.ui;

public class FindTheDifferenceUI {
    private static FindTheDifferenceUI instance;

    public final FindTheDifferenceFragmentUI findTheDifferenceFragmentUI = new FindTheDifferenceFragmentUI();

    public static class FindTheDifferenceFragmentUI {
        public int layout;
        public int experienceFragmentContainerView;
        public int originalImageView;
        public int originalAddImageButton;
        public int differentImageView;
        public int differentAddImageButton;
        public int saveButton;
    }


    private FindTheDifferenceUI() {}

    public static FindTheDifferenceUI getInstance() {
        if(instance == null) instance = new FindTheDifferenceUI();

        return instance;
    }
}
