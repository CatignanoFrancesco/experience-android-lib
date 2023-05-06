package it.uniba.eculturetool.experience_lib.fragments.puzzle;

import static it.uniba.eculturetool.experience_lib.ExperienceEditorFragment.KEY_EXPERIENCE_ID;
import static it.uniba.eculturetool.experience_lib.ExperienceEditorFragment.KEY_OPERA_ID;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuHost;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Set;

import it.uniba.eculturetool.experience_lib.ExperienceDataHolder;
import it.uniba.eculturetool.experience_lib.ExperienceEditorFragment;
import it.uniba.eculturetool.experience_lib.ExperienceViewModel;
import it.uniba.eculturetool.experience_lib.R;
import it.uniba.eculturetool.experience_lib.listeners.OnDataLoadListener;
import it.uniba.eculturetool.experience_lib.models.Experience;
import it.uniba.eculturetool.experience_lib.models.Puzzle;
import it.uniba.eculturetool.experience_lib.ui.PuzzleUI;
import it.uniba.eculturetool.experience_lib.utils.ToolbarManager;

public class PuzzleFragment extends Fragment {

    private final PuzzleUI ui = PuzzleUI.getInstance();

    private String operaId;
    private String puzzleId;

    private Button addImageButton;
    private ImageView puzzleImageView;
    private EditText gridDimensionEditText;
    private TextView gridDimensionTextView;
    private SwitchCompat rotateSwitch;
    private Button saveButton;
    private Toolbar toolbar;

    private PuzzleViewModel puzzleViewModel;
    private ExperienceViewModel experienceViewModel;
    private ActivityResultLauncher<Intent> pickPhoto;
    private final ExperienceDataHolder experienceDataHolder = ExperienceDataHolder.getInstance();

    public PuzzleFragment() {}

    public static PuzzleFragment newInstance(String operaId, String puzzleId) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_OPERA_ID, operaId);
        bundle.putString(KEY_EXPERIENCE_ID, puzzleId);

        PuzzleFragment puzzleFragment = new PuzzleFragment();
        puzzleFragment.setArguments(bundle);

        return puzzleFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pickPhoto = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri uri = result.getData().getData();
                puzzleImageView.setAlpha(1f);
                Picasso.get().load(uri).resize(puzzleImageView.getWidth(), puzzleImageView.getHeight()).centerCrop().into(puzzleImageView);

                try {
                    InputStream inputStream = requireActivity().getContentResolver().openInputStream(uri);
                    puzzleViewModel.getPuzzle().getValue().setImage(BitmapFactory.decodeStream(inputStream));
                }
                catch(FileNotFoundException ex) {}
            }
        });

        puzzleViewModel = new ViewModelProvider(requireActivity()).get(PuzzleViewModel.class);
        experienceViewModel = new ViewModelProvider(requireActivity()).get(ExperienceViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(ui.puzzleFragmentUI.layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        operaId = getArguments().getString(KEY_OPERA_ID);
        puzzleId = getArguments().getString(KEY_EXPERIENCE_ID);

        addImageButton = view.findViewById(ui.puzzleFragmentUI.addImageButtonId);
        puzzleImageView = view.findViewById(ui.puzzleFragmentUI.puzzleImageViewId);
        gridDimensionEditText = view.findViewById(ui.puzzleFragmentUI.gridInputTextId);
        gridDimensionTextView = view.findViewById(ui.puzzleFragmentUI.gridDimensionTextViewId);
        rotateSwitch = view.findViewById(ui.puzzleFragmentUI.rotateSwitchId);
        saveButton = view.findViewById(ui.puzzleFragmentUI.saveButtonId);
        toolbar = view.findViewById(ui.puzzleFragmentUI.toolbar);

        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        MenuHost menuHost = (MenuHost) requireActivity();
        ToolbarManager.setIcons(toolbar, activity, menuHost, this, R.string.puzzle_help);

        Set<Experience> experiences = experienceDataHolder.getExperienceByOperaId(operaId);
        if(experiences != null) {
            for(Experience experience : experiences) {
                if(experience.getId().equals(puzzleId)) {
                    puzzleViewModel.setPuzzle((Puzzle) experience);
                    break;
                }
            }
        }
        experienceViewModel.setExperience(puzzleViewModel.getPuzzle().getValue());

        ((OnDataLoadListener) requireActivity()).onDataLoad();  // Caricamento dei dati necessari
        setImage();
        setGridDimension();
        setRotation();

        saveButton.setOnClickListener(v -> {
            if(validate()) {
                experienceDataHolder.addExperienceToOpera(operaId, puzzleViewModel.getPuzzle().getValue());
                requireActivity().finish();
            }
        });
    }

    private void setImage() {
        puzzleViewModel.getPuzzle().observe(getViewLifecycleOwner(), puzzle -> {
            puzzleImageView.setAlpha(1f);
            puzzleImageView.setImageBitmap(puzzle.getImage());
        });

        addImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickPhoto.launch(intent);
        });
    }

    private void setGridDimension() {
        Puzzle puzzle = puzzleViewModel.getPuzzle().getValue();
        if(puzzle.getGridDimension() != 0) {
            gridDimensionEditText.setText(String.valueOf(puzzle.getGridDimension()));
            gridDimensionTextView.setText(String.valueOf(puzzle.getGridDimension()));
        }

        gridDimensionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String dimension = charSequence.toString();
                if(dimension.equals("")) {
                    gridDimensionTextView.setText("0");
                } else {
                    gridDimensionTextView.setText(dimension);
                    puzzle.setGridDimension(Integer.parseInt(dimension));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void setRotation() {
        if(puzzleViewModel.getPuzzle().getValue().isRotationEnabled()) {
            rotateSwitch.setChecked(true);
        }
        rotateSwitch.setOnCheckedChangeListener((compoundButton, checked) -> puzzleViewModel.getPuzzle().getValue().setRotationEnabled(checked));
    }

    private boolean validate() {
        if(puzzleViewModel.getPuzzle().getValue().getImage() == null) {
            addImageButton.setError(getString(R.string.puzzle_image_missing));
            return false;
        }

        if(puzzleViewModel.getPuzzle().getValue().getGridDimension() <= 1) {
            gridDimensionEditText.setError(getString(R.string.insert_correct_grid_dimension));
            gridDimensionEditText.requestFocus();
            return false;
        }

        ExperienceEditorFragment fragment = (ExperienceEditorFragment) getChildFragmentManager().findFragmentById(ui.puzzleFragmentUI.experienceFragmentContainerViewId);

        return fragment.validate();
    }
}