package it.uniba.eculturetool.experience_lib.ui;

public class FindRfidUI {
    private static FindRfidUI instance;

    public final FindRfidFragmentUI findRfidFragmentUi = new FindRfidFragmentUI();

    // Fragment
    public static class FindRfidFragmentUI {
        public int layout;
        public int toolbar;
        public int experienceFragmentContainerView;
        public int loadRfidButton;
        public int messageEditText;
        public int rfidCodeEditText;
        public int saveButton;
    }


    private FindRfidUI() {}

    public static FindRfidUI getInstance() {
        if(instance == null) instance = new FindRfidUI();

        return instance;
    }
}
