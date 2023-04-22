package it.uniba.eculturetool.experience_lib.ui;

public class FindDetailsUI {
    private static FindDetailsUI instance;

    public final FindDetailsFragmentUI findDetailsFragmentUI = new FindDetailsFragmentUI();

    public static class FindDetailsFragmentUI {
        public int layout;
        public int experienceFragmentContainerView;
        public int messageEditText;
        public int detailsImageView;
        public int addImageButton;
        public int saveButton;
    }

    private FindDetailsUI() {}

    public static FindDetailsUI getInstance() {
        if(instance == null) instance = new FindDetailsUI();

        return instance;
    }
}
