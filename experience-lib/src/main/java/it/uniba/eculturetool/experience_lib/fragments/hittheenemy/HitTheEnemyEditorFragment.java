package it.uniba.eculturetool.experience_lib.fragments.hittheenemy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.slider.Slider;

import it.uniba.eculturetool.experience_lib.ui.HitTheEnemyUI;

public class HitTheEnemyEditorFragment extends Fragment {
    private final HitTheEnemyUI ui = HitTheEnemyUI.getInstance();
    private static final String HIT_THE_ENEMY_ID = "HIT_THE_ENEMY_ID";
    private String hitTheEnemyId;

    private ImageView characterImage, backgroundImage, enemyImage, hitImage;
    private Button addCharacterImageButton, addBackgroundImageButton, addEnemyImageButton, addHitImageButton;
    private EditText characterNameEditText;
    private Slider characterSpeedSlider, hitSpeedSlider, hitResistanceSlider;

    public HitTheEnemyEditorFragment() {}

    public static HitTheEnemyEditorFragment newInstance(String hitTheEnemyId) {
        HitTheEnemyEditorFragment fragment = new HitTheEnemyEditorFragment();
        Bundle args = new Bundle();
        args.putString(HIT_THE_ENEMY_ID, hitTheEnemyId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            hitTheEnemyId = getArguments().getString(HIT_THE_ENEMY_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(ui.hitTheEnemyEditorUi.layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        characterImage = view.findViewById(ui.hitTheEnemyEditorUi.characterImageView);
        backgroundImage = view.findViewById(ui.hitTheEnemyEditorUi.backgroundImageView);
        enemyImage = view.findViewById(ui.hitTheEnemyEditorUi.enemyImageView);
        hitImage = view.findViewById(ui.hitTheEnemyEditorUi.enemyHitImageView);
        addCharacterImageButton = view.findViewById(ui.hitTheEnemyEditorUi.addCharacterImageButton);
        addBackgroundImageButton = view.findViewById(ui.hitTheEnemyEditorUi.addCharacterImageButton);
        addEnemyImageButton = view.findViewById(ui.hitTheEnemyEditorUi.addEnemyImageButton);
        addHitImageButton = view.findViewById(ui.hitTheEnemyEditorUi.addEnemyHitImageButton);
        characterNameEditText = view.findViewById(ui.hitTheEnemyEditorUi.characterNameEditText);
        characterSpeedSlider = view.findViewById(ui.hitTheEnemyEditorUi.characterSpeedSlider);
        hitSpeedSlider = view.findViewById(ui.hitTheEnemyEditorUi.hitSpeedSlider);
        hitResistanceSlider = view.findViewById(ui.hitTheEnemyEditorUi.characterResistanceSlider);
    }
}