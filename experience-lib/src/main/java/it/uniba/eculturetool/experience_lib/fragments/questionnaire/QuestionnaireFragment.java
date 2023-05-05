package it.uniba.eculturetool.experience_lib.fragments.questionnaire;

import static it.uniba.eculturetool.experience_lib.ExperienceEditorFragment.KEY_EXPERIENCE_ID;
import static it.uniba.eculturetool.experience_lib.ExperienceEditorFragment.KEY_OPERA_ID;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.uniba.eculturetool.experience_lib.ExperienceDataHolder;
import it.uniba.eculturetool.experience_lib.R;
import it.uniba.eculturetool.experience_lib.ui.QuestionnaireUI;

public class QuestionnaireFragment extends Fragment {
    private final QuestionnaireUI ui = QuestionnaireUI.getInstance();

    private QuestionnaireViewModel viewModel;
    private final ExperienceDataHolder experienceDataHolder = ExperienceDataHolder.getInstance();

    private String operaId;
    private String questionnaireId;

    public QuestionnaireFragment() {}

    public static QuestionnaireFragment newInstance(String operaId, String questionnaireId) {
        QuestionnaireFragment fragment = new QuestionnaireFragment();
        Bundle args = new Bundle();
        args.putString(KEY_OPERA_ID, operaId);
        args.putString(KEY_EXPERIENCE_ID, questionnaireId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(QuestionnaireViewModel.class);

        if (getArguments() != null) {
            operaId = getArguments().getString(KEY_OPERA_ID);
            questionnaireId = getArguments().getString(KEY_EXPERIENCE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(ui.questionnaireFragmentUi.layout, container, false);
    }
}