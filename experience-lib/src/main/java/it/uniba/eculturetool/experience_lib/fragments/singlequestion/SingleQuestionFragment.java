package it.uniba.eculturetool.experience_lib.fragments.singlequestion;

import static it.uniba.eculturetool.experience_lib.ExperienceEditorFragment.KEY_EXPERIENCE_ID;
import static it.uniba.eculturetool.experience_lib.ExperienceEditorFragment.KEY_OPERA_ID;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import it.uniba.eculturetool.experience_lib.ExperienceDataHolder;
import it.uniba.eculturetool.experience_lib.ExperienceViewModel;
import it.uniba.eculturetool.experience_lib.fragments.quiz.QuizEditorFragment;
import it.uniba.eculturetool.experience_lib.listeners.OnDataLoadListener;
import it.uniba.eculturetool.experience_lib.ui.SingleQuestionUI;

public class SingleQuestionFragment extends Fragment {
    private final SingleQuestionUI ui = SingleQuestionUI.getInstance();

    private String operaId;
    private String singleQuestionId;
    private final ExperienceDataHolder experienceDataHolder = ExperienceDataHolder.getInstance();
    private SingleQuestionViewModel viewModel;
    private ExperienceViewModel experienceViewModel;

    private EditText questionEditText;
    private RecyclerView answersRecyclerView;
    private Button saveButton, addAnswerButton;

    public SingleQuestionFragment() {}

    public static SingleQuestionFragment newInstance(String operaId, String singleQuestionId) {
        SingleQuestionFragment fragment = new SingleQuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_OPERA_ID, operaId);
        bundle.putString(KEY_EXPERIENCE_ID, singleQuestionId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            operaId = getArguments().getString(KEY_OPERA_ID);
            singleQuestionId = getArguments().getString(KEY_EXPERIENCE_ID);
        }

        viewModel = new ViewModelProvider(requireActivity()).get(SingleQuestionViewModel.class);
        experienceViewModel = new ViewModelProvider(requireActivity()).get(ExperienceViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(ui.singleQuestionEditorUi.layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        questionEditText = view.findViewById(ui.singleQuestionEditorUi.questionEditText);
        answersRecyclerView = view.findViewById(ui.singleQuestionEditorUi.answersRecyclerViewId);
        addAnswerButton = view.findViewById(ui.singleQuestionEditorUi.addAnswerButtonId);
        saveButton = view.findViewById(ui.singleQuestionEditorUi.saveButtonId);
    }

    @Override
    public void onStart() {
        super.onStart();

        ((OnDataLoadListener) requireActivity()).onDataLoad();
    }
}