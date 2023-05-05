package it.uniba.eculturetool.experience_lib.fragments.questionnaire;

import static it.uniba.eculturetool.experience_lib.ExperienceEditorFragment.KEY_EXPERIENCE_ID;
import static it.uniba.eculturetool.experience_lib.ExperienceEditorFragment.KEY_OPERA_ID;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toolbar;

import it.uniba.eculturetool.experience_lib.ExperienceDataHolder;
import it.uniba.eculturetool.experience_lib.R;
import it.uniba.eculturetool.experience_lib.ui.QuestionnaireUI;

public class QuestionnaireFragment extends Fragment {
    private final QuestionnaireUI ui = QuestionnaireUI.getInstance();

    private QuestionnaireViewModel viewModel;
    private final ExperienceDataHolder experienceDataHolder = ExperienceDataHolder.getInstance();

    private String operaId;
    private String questionnaireId;

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Button saveButton;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(ui.questionnaireFragmentUi.toolbar);
        recyclerView = view.findViewById(ui.questionnaireFragmentUi.questionsRecyclerView);
        saveButton = view.findViewById(ui.questionnaireFragmentUi.saveButton);
    }

    private void chooseQuestionnaireDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.choose_questionnaire)
                .setCancelable(false)
                .setNegativeButton(R.string.back, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    requireActivity().finish();
                }).show();
    }
}