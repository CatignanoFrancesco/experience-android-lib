package it.uniba.eculturetool.experience_lib;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;

import it.uniba.eculturetool.experience_lib.models.Difficulty;
import it.uniba.eculturetool.experience_lib.models.Experience;
import it.uniba.eculturetool.experience_lib.models.Quiz;
import it.uniba.eculturetool.experience_lib.ui.ExperienceUI;

public class ExperienceEditorFragment extends Fragment {
    public static final String KEY_EXPERIENCE_ID = "EXP_ID";
    public static final String KEY_OPERA_ID = "OPERA_ID";

    protected final ExperienceUI ui = ExperienceUI.getInstance();

    protected ExperienceViewModel viewModel;
    protected Experience experience;

    private TextInputEditText pointsTextInput;
    private Slider difficultySlider;
    private TextView difficultyValue;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(ui.experienceEditorFragmentUI.layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ExperienceViewModel.class);

        pointsTextInput = (TextInputEditText) view.findViewById(ui.experienceEditorFragmentUI.pointsInputTextId);
        difficultySlider = (Slider) view.findViewById(ui.experienceEditorFragmentUI.difficultySliderId);
        difficultyValue = (TextView) view.findViewById(ui.experienceEditorFragmentUI.difficultyValueTextViewId);
    }

    @Override
    public void onStart() {
        super.onStart();

        setPoints();
        setDifficulty();
    }

    private void setPoints() {
        experience = viewModel.getExperience().getValue();
        if(experience == null) return;

        viewModel.getExperience().observe(requireActivity(), new Observer<Experience>() {
            @Override
            public void onChanged(Experience experience) {
                if(experience instanceof Quiz) pointsTextInput.setEnabled(false);
            }
        });

        pointsTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String points = charSequence.toString();
                if(!points.equals("")) {
                    experience.setPoints(Integer.parseInt(points));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        pointsTextInput.setText(String.valueOf(experience.getPoints()));
    }

    private void setDifficulty() {
        difficultySlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                Difficulty difficulty = Difficulty.valueOf((int) value);
                difficultyValue.setText(difficulty.toString(requireContext()));
                if(experience != null) experience.setDifficulty(difficulty);
            }
        });

        if(experience != null) {
            difficultySlider.setValue(experience.getDifficulty().ordinal());
            difficultyValue.setText(experience.getDifficulty().toString(requireContext()));
        }
    }

    public boolean validate() {
        if(viewModel.getExperience().getValue().getPoints() == 0) {
            pointsTextInput.setError(getString(R.string.points_missing));
            pointsTextInput.requestFocus();
            return false;
        }

        return true;
    }

}