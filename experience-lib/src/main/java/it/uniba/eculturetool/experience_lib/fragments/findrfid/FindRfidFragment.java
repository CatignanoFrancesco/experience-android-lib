package it.uniba.eculturetool.experience_lib.fragments.findrfid;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Set;
import java.util.UUID;

import it.uniba.eculturetool.experience_lib.ExperienceDataHolder;
import it.uniba.eculturetool.experience_lib.ExperienceEditorFragment;
import it.uniba.eculturetool.experience_lib.ExperienceViewModel;
import it.uniba.eculturetool.experience_lib.R;
import it.uniba.eculturetool.experience_lib.models.Experience;
import it.uniba.eculturetool.experience_lib.models.FindRFID;
import it.uniba.eculturetool.experience_lib.ui.FindRfidUI;

public class FindRfidFragment extends Fragment {
    private final FindRfidUI ui = FindRfidUI.getInstance();

    private String operaId;
    private String experienceId;
    private FindRfidViewModel viewModel;
    private ExperienceViewModel experienceViewModel;
    private final ExperienceDataHolder dataHolder = ExperienceDataHolder.getInstance();

    private EditText messageEditText;
    private EditText rfidEditText;
    private Button saveButton, loadRfidButton;

    public FindRfidFragment() {
    }

    public static FindRfidFragment newInstance(String operaId, String experienceId) {
        FindRfidFragment fragment = new FindRfidFragment();

        Bundle args = new Bundle();
        args.putString(ExperienceEditorFragment.KEY_OPERA_ID, operaId);
        args.putString(ExperienceEditorFragment.KEY_EXPERIENCE_ID, experienceId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Dati dal bundle
        operaId = getArguments().getString(ExperienceEditorFragment.KEY_OPERA_ID);
        experienceId = getArguments().getString(ExperienceEditorFragment.KEY_EXPERIENCE_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(ui.findRfidFragmentUi.layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ui
        messageEditText = view.findViewById(ui.findRfidFragmentUi.messageEditText);
        rfidEditText = view.findViewById(ui.findRfidFragmentUi.rfidCodeEditText);
        saveButton = view.findViewById(ui.findRfidFragmentUi.saveButton);
        loadRfidButton = view.findViewById(ui.findRfidFragmentUi.loadRfidButton);

        // Caricamento dei dati
        viewModel = new ViewModelProvider(requireActivity()).get(FindRfidViewModel.class);
        experienceViewModel = new ViewModelProvider(requireActivity()).get(ExperienceViewModel.class);

        Set<Experience> experiences = dataHolder.getExperienceByOperaId(operaId);
        if (experiences != null) {
            for (Experience experience : experiences) {
                if (experience.getId().equals(experienceId)) {
                    viewModel.setFindRfid((FindRFID) experience);
                    break;
                }
            }
        }
        experienceViewModel.setExperience(viewModel.getFindRfid().getValue());

        setMessageEditText();
        setRfidEditText();

        saveButton.setOnClickListener(v -> {
            if (validate()) {
                dataHolder.addExperienceToOpera(operaId, viewModel.getFindRfid().getValue());
                requireActivity().finish();
            }
        });
    }

    private void setMessageEditText() {
        String message = viewModel.getFindRfid().getValue().getMessage();

        if (message != null && !message.isEmpty())
            messageEditText.setText(message);

        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.getFindRfid().getValue().setMessage(editable.toString());
            }
        });
    }

    private void setRfidEditText() {
        UUID rfidId = viewModel.getFindRfid().getValue().getRfidId();

        if (rfidId != null && !rfidId.toString().isEmpty()) {
            rfidEditText.setText(rfidId.toString());
        }
    }

    private boolean validate() {
        FindRFID fr = viewModel.getFindRfid().getValue();

        if (messageEditText.getText().toString().isEmpty()) {
            messageEditText.setError(getString(R.string.message_missing));
            messageEditText.requestFocus();
            return false;
        }

        if (rfidEditText.getText().toString().isEmpty()) {
            rfidEditText.setError(getString(R.string.rfid_code_missing));
            rfidEditText.requestFocus();
            return false;
        }

        try {
            fr.setRfidId(UUID.fromString(rfidEditText.getText().toString()));
        } catch (IllegalArgumentException e) {
            rfidEditText.setError(getString(R.string.rfid_code_wrong_format));
            rfidEditText.requestFocus();
            return false;
        }

        ExperienceEditorFragment fragment = (ExperienceEditorFragment) getChildFragmentManager().findFragmentById(ui.findRfidFragmentUi.experienceFragmentContainerView);
        return fragment.validate();
    }
}