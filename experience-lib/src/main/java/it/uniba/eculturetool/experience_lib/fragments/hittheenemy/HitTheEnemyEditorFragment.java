package it.uniba.eculturetool.experience_lib.fragments.hittheenemy;

import static it.uniba.eculturetool.experience_lib.ExperienceEditorFragment.KEY_EXPERIENCE_ID;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;

import it.uniba.eculturetool.experience_lib.ExperienceEditorFragment;
import it.uniba.eculturetool.experience_lib.ExperienceViewModel;
import it.uniba.eculturetool.experience_lib.R;
import it.uniba.eculturetool.experience_lib.models.hittheenemy.HitTheEnemyItem;
import it.uniba.eculturetool.experience_lib.ui.HitTheEnemyUI;

public class HitTheEnemyEditorFragment extends Fragment {
    private static final String KEY_HTE_ITEM_POSITION = "KEY_HTE_ITEM_POSITION";
    private final HitTheEnemyUI ui = HitTheEnemyUI.getInstance();
    private String hitTheEnemyId;
    private HitTheEnemyViewModel viewModel;
    private ExperienceViewModel experienceViewModel;

    private ImageView selectedImageView, characterImage, backgroundImage, enemyImage, hitImage;
    private Button addCharacterImageButton, addBackgroundImageButton, addEnemyImageButton, addHitImageButton, saveButton;
    private EditText characterNameEditText;
    private Slider characterSpeedSlider, hitSpeedSlider, hitResistanceSlider;

    private ActivityResultLauncher<Intent> pickPhoto;

    public HitTheEnemyEditorFragment() {}

    public static HitTheEnemyEditorFragment newInstance(int hitTheEnemyItemPosition) {
        HitTheEnemyEditorFragment fragment = new HitTheEnemyEditorFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_HTE_ITEM_POSITION, hitTheEnemyItemPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(HitTheEnemyViewModel.class);
        experienceViewModel = new ViewModelProvider(requireActivity()).get(ExperienceViewModel.class);

        if (getArguments() != null) {
            hitTheEnemyId = getArguments().getString(KEY_EXPERIENCE_ID);
        }

        pickPhoto = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                selectedImageView.setAlpha(1f);
                Uri uri = result.getData().getData();
                Picasso.get().load(uri).resize(selectedImageView.getWidth(), selectedImageView.getHeight()).centerCrop().into(selectedImageView);

                if(selectedImageView == characterImage)
                    viewModel.getActiveHitTheEnemyItem().setUriCharacter(uri.toString());
                else if(selectedImageView == backgroundImage)
                    viewModel.getActiveHitTheEnemyItem().setUriBackground(uri.toString());
                else if(selectedImageView == enemyImage)
                    viewModel.getActiveHitTheEnemyItem().setUriEnemy(uri.toString());
                else if(selectedImageView == hitImage)
                    viewModel.getActiveHitTheEnemyItem().setUriEnemyHit(uri.toString());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(ui.hitTheEnemyEditorUi.layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        experienceViewModel.setExperience(viewModel.getHitTheEnemy());

        characterImage = view.findViewById(ui.hitTheEnemyEditorUi.characterImageView);
        backgroundImage = view.findViewById(ui.hitTheEnemyEditorUi.backgroundImageView);
        enemyImage = view.findViewById(ui.hitTheEnemyEditorUi.enemyImageView);
        hitImage = view.findViewById(ui.hitTheEnemyEditorUi.enemyHitImageView);
        addCharacterImageButton = view.findViewById(ui.hitTheEnemyEditorUi.addCharacterImageButton);
        addBackgroundImageButton = view.findViewById(ui.hitTheEnemyEditorUi.addBackgroundImageButton);
        addEnemyImageButton = view.findViewById(ui.hitTheEnemyEditorUi.addEnemyImageButton);
        addHitImageButton = view.findViewById(ui.hitTheEnemyEditorUi.addEnemyHitImageButton);
        characterNameEditText = view.findViewById(ui.hitTheEnemyEditorUi.characterNameEditText);
        characterSpeedSlider = view.findViewById(ui.hitTheEnemyEditorUi.characterSpeedSlider);
        hitSpeedSlider = view.findViewById(ui.hitTheEnemyEditorUi.hitSpeedSlider);
        hitResistanceSlider = view.findViewById(ui.hitTheEnemyEditorUi.characterResistanceSlider);
        saveButton = view.findViewById(ui.hitTheEnemyEditorUi.saveButton);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Immagini
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        addCharacterImageButton.setOnClickListener(v -> {
            selectedImageView = characterImage;
            pickPhoto.launch(photoPickerIntent);
        });
        addEnemyImageButton.setOnClickListener(v -> {
            selectedImageView = enemyImage;
            pickPhoto.launch(photoPickerIntent);
        });
        addBackgroundImageButton.setOnClickListener(v -> {
            selectedImageView = backgroundImage;
            pickPhoto.launch(photoPickerIntent);
        });
        addHitImageButton.setOnClickListener(v -> {
            selectedImageView = hitImage;
            pickPhoto.launch(photoPickerIntent);
        });

        // Salvataggio
        saveButton.setOnClickListener(v -> {
            if(!validate()) return;

            HitTheEnemyItem hteItem = viewModel.getActiveHitTheEnemyItem();
            hteItem.setCharacterName(characterNameEditText.getText().toString());
            hteItem.setCharacterSpeed((int) characterSpeedSlider.getValue());
            hteItem.setCharacterResistance((int) hitResistanceSlider.getValue());
            hteItem.setHitSpeed((int) hitSpeedSlider.getValue());

            Toast.makeText(requireContext(), R.string.hit_the_enemy_created, Toast.LENGTH_SHORT).show();
            ((HitTheEnemyFragment) getParentFragment()).onHitEnemyCreted();
        });
    }

    public boolean validate() {
        if(characterNameEditText.getText().toString().isEmpty()) {
            characterNameEditText.setError(getString(R.string.character_name_empty));
            characterNameEditText.requestFocus();
            return false;
        }

        HitTheEnemyItem hteItem = viewModel.getActiveHitTheEnemyItem();
        if(hteItem.getUriCharacter() == null) {
            Toast.makeText(requireContext(), R.string.character_image_missing, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(hteItem.getUriBackground() == null) {
            Toast.makeText(requireContext(), R.string.background_image_missing, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(hteItem.getUriEnemy() == null) {
            Toast.makeText(requireContext(), R.string.enemy_image_missing, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(hteItem.getUriEnemyHit() == null) {
            Toast.makeText(requireContext(), R.string.enemy_hit_image_missing, Toast.LENGTH_SHORT).show();
            return false;
        }

        ExperienceEditorFragment fragment = (ExperienceEditorFragment) getChildFragmentManager().findFragmentById(ui.hitTheEnemyEditorUi.experienceEditorFragmentContainerView);
        return fragment.validate();
    }
}