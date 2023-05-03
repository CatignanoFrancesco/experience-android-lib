package it.uniba.eculturetool.experience_lib.ui;

public class HitTheEnemyUI {
    private static HitTheEnemyUI instance;

    public final HitTheEnemyListUI hitTheEnemyListUi = new HitTheEnemyListUI();
    public final HitTheEnemyAdapterUI hitTheEnemyAdapterUi = new HitTheEnemyAdapterUI();
    public final HitTheEnemyEditorUI hitTheEnemyEditorUi = new HitTheEnemyEditorUI();

    public static class HitTheEnemyListUI {
        public int layout;
        public int recyclerViewId;
        public int addHitTheEnemyButton;
    }

    public static class HitTheEnemyAdapterUI {
        public int layout;
        public int layoutId;
        public int characterImageView;
        public int characterNameTextView;
        public int difficultyTextView;
    }

    public static class HitTheEnemyEditorUI {
        public int layout;
        public int characterImageView;
        public int addCharacterImageButton;
        public int characterSpeedSlider;
        public int characterResistanceSlider;
        public int characterNameEditText;
        public int backgroundImageView;
        public int addBackgroundImageButton;
        public int enemyImageView;
        public int addEnemyImageButton;
        public int enemyHitImageView;
        public int addEnemyHitImageButton;
        public int hitSpeedSlider;
    }

    private HitTheEnemyUI() {}

    public static HitTheEnemyUI getInstance() {
        if(instance == null) instance = new HitTheEnemyUI();

        return instance;
    }
}
