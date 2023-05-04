package it.uniba.eculturetool.experience_lib.fragments.recognizetheobject;

import static it.uniba.eculturetool.experience_lib.ExperienceEditorFragment.KEY_EXPERIENCE_ID;
import static it.uniba.eculturetool.experience_lib.ExperienceEditorFragment.KEY_OPERA_ID;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.uniba.eculturetool.experience_lib.R;
import it.uniba.eculturetool.experience_lib.ui.RecognizeTheObjectUI;

public class RecognizeTheObjectFragment extends Fragment {
    private final RecognizeTheObjectUI ui = RecognizeTheObjectUI.getInstance();

    private String operaId;
    private String recognizeTheObjectId;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(ui.recognizeTheObjectFieldsUi.layout, container, false);
    }
}