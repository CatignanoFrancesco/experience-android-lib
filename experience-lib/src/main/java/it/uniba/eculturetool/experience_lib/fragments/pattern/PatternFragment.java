package it.uniba.eculturetool.experience_lib.fragments.pattern;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

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

public class PatternFragment extends Fragment {

    private String operaId;
    private String experienceId;

    private PatternViewModel viewModel;
    private ExperienceViewModel experienceViewModel;
    private final ExperienceDataHolder dataHolder = ExperienceDataHolder.getInstance();

    private final PatternUI ui = PatternUI.getInstance();
    private GridView gridView;
    private PatternAdapter adapter;
    private Button saveButton;

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
        for(Experience experience : experiences) {
            if(experience.getId().equals(experienceId)) {
                viewModel.setPattern((Pattern) experience);
                break;
            }
        }
        experienceViewModel.setExperience(viewModel.getPattern().getValue());

        gridView = view.findViewById(ui.patternFragmentUI.gridView);
        saveButton = view.findViewById(ui.patternFragmentUI.saveButton);

        setMatrix();

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

    private boolean validate() {
        if(adapter.getNumberOfSetDots() < 3) {
            Toast.makeText(requireContext(), getString(R.string.dot_number_error), Toast.LENGTH_SHORT).show();
            return false;
        }

        ExperienceEditorFragment fragment = (ExperienceEditorFragment) getChildFragmentManager().findFragmentById(ui.patternFragmentUI.experienceFragmentContainerView);
        return fragment.validate();
    }
}