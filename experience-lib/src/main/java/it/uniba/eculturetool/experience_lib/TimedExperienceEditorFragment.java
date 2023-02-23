package it.uniba.eculturetool.experience_lib;

import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import it.uniba.eculturetool.experience_lib.models.TimedExperience;

public class TimedExperienceEditorFragment extends ExperienceEditorFragment {
    private NumberPicker minutesPicker;
    private NumberPicker secondsPicker;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        minutesPicker = view.findViewById(ui.timedExperienceFragmentUI.minutesNumberPicker);
        secondsPicker = view.findViewById(ui.timedExperienceFragmentUI.secondsNumberPicker);
    }

    @Override
    public void onStart() {
        super.onStart();

        setPickers();
    }

    private void setPickers() {
        minutesPicker.setMinValue(TimedExperience.MIN_MINUTES);
        minutesPicker.setMaxValue(TimedExperience.MAX_MINUTES);
        secondsPicker.setMinValue(TimedExperience.MIN_SECONDS);
        secondsPicker.setMaxValue(TimedExperience.MAX_SECONDS);

        TimedExperience experience = (TimedExperience) viewModel.getExperience().getValue();
        if(experience.getMinutes() != TimedExperience.MIN_MINUTES) {
            minutesPicker.setValue(experience.getMinutes());
        }
        if(experience.getSeconds() != TimedExperience.MIN_SECONDS) {
            secondsPicker.setValue(experience.getSeconds());
        }

        minutesPicker.setOnValueChangedListener((numberPicker, oldVal, newVal) -> experience.setMinutes(newVal));
        secondsPicker.setOnValueChangedListener((picker, oldVal, newVal) -> experience.setSeconds(newVal));
    }

    @Override
    public boolean validate() {
        if(minutesPicker.getValue() == 0 && secondsPicker.getValue() < 10) {
            Toast.makeText(requireContext(), getString(R.string.time_invalid), Toast.LENGTH_LONG).show();
            secondsPicker.requestFocus();
            return false;
        }

        return super.validate();
    }
}