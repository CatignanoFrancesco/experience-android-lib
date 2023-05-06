package it.uniba.eculturetool.experience_lib.fragments.recognizetheobject;

import static it.uniba.eculturetool.experience_lib.ExperienceEditorFragment.KEY_EXPERIENCE_ID;
import static it.uniba.eculturetool.experience_lib.ExperienceEditorFragment.KEY_OPERA_ID;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuHost;
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

import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Set;

import it.uniba.eculturetool.experience_lib.ExperienceDataHolder;
import it.uniba.eculturetool.experience_lib.ExperienceEditorFragment;
import it.uniba.eculturetool.experience_lib.ExperienceViewModel;
import it.uniba.eculturetool.experience_lib.R;
import it.uniba.eculturetool.experience_lib.models.Experience;
import it.uniba.eculturetool.experience_lib.models.Puzzle;
import it.uniba.eculturetool.experience_lib.models.RecognizeTheObject;
import it.uniba.eculturetool.experience_lib.ui.RecognizeTheObjectUI;
import it.uniba.eculturetool.experience_lib.utils.ToolbarManager;

public class RecognizeTheObjectFragment extends Fragment {
    private final RecognizeTheObjectUI ui = RecognizeTheObjectUI.getInstance();

    private String operaId;
    private String recognizeTheObjectId;
    private RecognizeTheObjectViewModel viewModel;
    private ExperienceViewModel experienceViewModel;
    private final ExperienceDataHolder dataHolder = ExperienceDataHolder.getInstance();

    private ImageView referenceImage;
    private Button addReferenceImageButton, saveButton;
    private EditText descriptionEditText, modelNameEditText;
    private Toolbar toolbar;
    private ActivityResultLauncher<Intent> pickPhoto;

    public RecognizeTheObjectFragment() {}

    public static RecognizeTheObjectFragment newInstance(String operaId, String recognizeTheObjectId) {
        RecognizeTheObjectFragment fragment = new RecognizeTheObjectFragment();
        Bundle args = new Bundle();
        args.putString(KEY_OPERA_ID, operaId);
        args.putString(KEY_EXPERIENCE_ID, recognizeTheObjectId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            operaId = getArguments().getString(KEY_OPERA_ID);
            recognizeTheObjectId = getArguments().getString(KEY_EXPERIENCE_ID);
        }

        viewModel = new ViewModelProvider(requireActivity()).get(RecognizeTheObjectViewModel.class);
        experienceViewModel = new ViewModelProvider(requireActivity()).get(ExperienceViewModel.class);

        pickPhoto = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri uri = result.getData().getData();
                setImage(uri);

                try {
                    InputStream inputStream = requireActivity().getContentResolver().openInputStream(uri);
                    viewModel.getRecognizeTheObject().setReferenceImage(BitmapFactory.decodeStream(inputStream));
                    viewModel.getRecognizeTheObject().setUriReferenceImage(uri.toString());
                }
                catch(FileNotFoundException ex) {}
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(ui.recognizeTheObjectFieldsUi.layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addReferenceImageButton = view.findViewById(ui.recognizeTheObjectFieldsUi.addReferenceImageButton);
        referenceImage = view.findViewById(ui.recognizeTheObjectFieldsUi.referenceImageView);
        descriptionEditText = view.findViewById(ui.recognizeTheObjectFieldsUi.descriptionEditText);
        modelNameEditText = view.findViewById(ui.recognizeTheObjectFieldsUi.modelNameEditText);
        saveButton = view.findViewById(ui.recognizeTheObjectFieldsUi.saveButton);
        toolbar = view.findViewById(ui.recognizeTheObjectFieldsUi.toolbar);

        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        MenuHost menuHost = (MenuHost) requireActivity();
        ToolbarManager.setIcons(toolbar, activity, menuHost, this, R.string.recognize_object_help);

        Set<Experience> experiences = dataHolder.getExperienceByOperaId(operaId);
        if(experiences != null) {
            for(Experience experience : experiences) {
                if(experience.getId().equals(recognizeTheObjectId)) {
                    viewModel.setRecognizeTheObject((RecognizeTheObject) experience);
                    break;
                }
            }
        }
        experienceViewModel.setExperience(viewModel.getRecognizeTheObject());
    }

    @Override
    public void onStart() {
        super.onStart();
        RecognizeTheObject rto = viewModel.getRecognizeTheObject();

        // Immagine
        if(rto.getUriReferenceImage() != null) {
            setImage(Uri.parse(rto.getUriReferenceImage()));
        }

        // Descrizione
        if(rto.getDescription() != null) {
            descriptionEditText.setText(rto.getDescription());
        }

        // Nome modello
        if(rto.getModelName() == null) {
            modelNameEditText.setText(rto.getModelName());
        }

        addReferenceImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickPhoto.launch(intent);
        });

        saveButton.setOnClickListener(v -> {
            if(validate()) {
                viewModel.getRecognizeTheObject().setDescription(descriptionEditText.getText().toString());
                viewModel.getRecognizeTheObject().setModelName(modelNameEditText.getText().toString());

                dataHolder.addExperienceToOpera(operaId, viewModel.getRecognizeTheObject());
                requireActivity().finish();
            }
        });
    }

    private boolean validate() {
        if(descriptionEditText.getText().toString().isEmpty()) {
            descriptionEditText.setError(getString(R.string.description_missing));
            descriptionEditText.requestFocus();
            return false;
        }

        if(viewModel.getRecognizeTheObject().getUriReferenceImage() == null) {
            Toast.makeText(requireContext(), R.string.reference_image_missing, Toast.LENGTH_SHORT).show();
            return false;
        }

        ExperienceEditorFragment experienceEditorFragment = (ExperienceEditorFragment) getChildFragmentManager().findFragmentById(ui.recognizeTheObjectFieldsUi.experienceFragmentContainerView);
        return experienceEditorFragment.validate();
    }

    private void setImage(Uri uri) {
        Runnable onImageReady = () -> {
            referenceImage.setAlpha(1f);
            Picasso.get().load(uri).resize(referenceImage.getWidth(), referenceImage.getHeight()).centerCrop().into(referenceImage);
        };

        if(referenceImage.getWidth() == 0) {
            referenceImage.addOnLayoutChangeListener((view, i, i1, i2, i3, i4, i5, i6, i7) -> {
                onImageReady.run();
            });
        } else {
            onImageReady.run();
        }
    }
}