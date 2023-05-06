package it.uniba.eculturetool.experience_lib.fragments.pattern;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuHost;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import it.uniba.eculturetool.experience_lib.ExperienceDataHolder;
import it.uniba.eculturetool.experience_lib.ExperienceEditorFragment;
import it.uniba.eculturetool.experience_lib.ExperienceViewModel;
import it.uniba.eculturetool.experience_lib.R;
import it.uniba.eculturetool.experience_lib.models.Experience;
import it.uniba.eculturetool.experience_lib.models.Pattern;
import it.uniba.eculturetool.experience_lib.ui.PatternUI;
import it.uniba.eculturetool.experience_lib.utils.ToolbarManager;

public class PatternFragment extends Fragment {

    private String operaId;
    private String experienceId;

    private PatternViewModel viewModel;
    private ExperienceViewModel experienceViewModel;
    private final ExperienceDataHolder dataHolder = ExperienceDataHolder.getInstance();

    private final PatternUI ui = PatternUI.getInstance();
    private GridView gridView;
    private PatternAdapter adapter;
    private Button saveButton, loadMatrixButton;
    private Toolbar toolbar;

    public PatternFragment() {}

    public static PatternFragment newInstance(String operaId, String experienceId) {
        PatternFragment fragment = new PatternFragment();
        Bundle args = new Bundle();
        args.putString(ExperienceEditorFragment.KEY_OPERA_ID, operaId);
        args.putString(ExperienceEditorFragment.KEY_EXPERIENCE_ID, experienceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        operaId = getArguments().getString(ExperienceEditorFragment.KEY_OPERA_ID);
        experienceId = getArguments().getString(ExperienceEditorFragment.KEY_EXPERIENCE_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(ui.patternFragmentUI.layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(PatternViewModel.class);
        experienceViewModel = new ViewModelProvider(requireActivity()).get(ExperienceViewModel.class);

        Set<Experience> experiences = dataHolder.getExperienceByOperaId(operaId);
        if(experiences != null) {
            for(Experience experience : experiences) {
                if(experience.getId().equals(experienceId)) {
                    viewModel.setPattern((Pattern) experience);
                    break;
                }
            }
        }
        experienceViewModel.setExperience(viewModel.getPattern().getValue());

        gridView = view.findViewById(ui.patternFragmentUI.gridView);
        loadMatrixButton = view.findViewById(ui.patternFragmentUI.loadPatternButton);
        saveButton = view.findViewById(ui.patternFragmentUI.saveButton);
        toolbar = view.findViewById(ui.patternFragmentUI.toolbar);

        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        MenuHost menuHost = (MenuHost) requireActivity();
        ToolbarManager.setIcons(toolbar, activity, menuHost, this, R.string.pattern_help);

        setMatrix();

        loadMatrixButton.setOnClickListener(v -> loadPattern());

        saveButton.setOnClickListener(v -> {
            if(validate()) {
                dataHolder.addExperienceToOpera(operaId, viewModel.getPattern().getValue());
                requireActivity().finish();
            }
        });
    }

    private void setMatrix() {
        if(viewModel.getPattern().getValue().isMatrixEmpty()) {
            adapter = new PatternAdapter(requireActivity(), Stream.of(viewModel.getPattern().getValue().getMatrixPattern()).flatMapToInt(IntStream::of).toArray());
        } else {
            adapter = new PatternAdapter(requireActivity());
        }

        gridView.setAdapter(adapter);
    }

    private void loadPattern() {
        try(InputStream inputStream = getResources().openRawResource(R.raw.pattern_list)) {
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            String json = new String(buffer, StandardCharsets.UTF_8);

            // Conversione del JSON
            Gson gson = new Gson();
            List<int[][]> matrixList = gson.fromJson(json, new TypeToken<List<int[][]>>(){}.getType());

            // Dialogo
            new AlertDialog.Builder(requireContext())
                    .setTitle(R.string.choose_pattern)
                    .setItems(R.array.pattern_difficulty, (dialogInterface, i) -> {
                        viewModel.setMatrix(matrixList.get(i));
                        setMatrix();
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        }
        catch (IOException e) {
            return;
        }
    }

    private boolean validate() {
        if(adapter.getNumberOfSetDots() < 3) {
            Toast.makeText(requireContext(), getString(R.string.dot_number_error), Toast.LENGTH_SHORT).show();
            return false;
        }

        ExperienceEditorFragment fragment = (ExperienceEditorFragment) getChildFragmentManager().findFragmentById(ui.patternFragmentUI.experienceFragmentContainerView);
        return fragment.validate();
    }
}