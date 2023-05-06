package it.uniba.eculturetool.experience_lib.fragments.singlequestion;

import static it.uniba.eculturetool.experience_lib.ExperienceEditorFragment.KEY_EXPERIENCE_ID;
import static it.uniba.eculturetool.experience_lib.ExperienceEditorFragment.KEY_OPERA_ID;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuHost;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import it.uniba.eculturetool.experience_lib.ExperienceDataHolder;
import it.uniba.eculturetool.experience_lib.ExperienceViewModel;
import it.uniba.eculturetool.experience_lib.R;
import it.uniba.eculturetool.experience_lib.TimedExperienceEditorFragment;
import it.uniba.eculturetool.experience_lib.fragments.quiz.AddAnswerAlertDialog;
import it.uniba.eculturetool.experience_lib.fragments.quiz.AnswerAdapter;
import it.uniba.eculturetool.experience_lib.fragments.quiz.AnswerManager;
import it.uniba.eculturetool.experience_lib.listeners.OnClickDeleteListener;
import it.uniba.eculturetool.experience_lib.listeners.OnDataLoadListener;
import it.uniba.eculturetool.experience_lib.models.Answer;
import it.uniba.eculturetool.experience_lib.models.Experience;
import it.uniba.eculturetool.experience_lib.models.Puzzle;
import it.uniba.eculturetool.experience_lib.models.SingleQuestion;
import it.uniba.eculturetool.experience_lib.ui.SingleQuestionUI;
import it.uniba.eculturetool.experience_lib.utils.ToolbarManager;

public class SingleQuestionFragment extends Fragment implements AnswerManager {
    private final SingleQuestionUI ui = SingleQuestionUI.getInstance();

    private String operaId;
    private String singleQuestionId;
    private final ExperienceDataHolder experienceDataHolder = ExperienceDataHolder.getInstance();
    private SingleQuestionViewModel viewModel;
    private ExperienceViewModel experienceViewModel;

    private EditText questionEditText;
    private RecyclerView answersRecyclerView;
    private AnswerAdapter adapter;
    private Button saveButton, addAnswerButton;
    private Toolbar toolbar;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(ui.singleQuestionEditorUi.layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SingleQuestionViewModel.class);
        experienceViewModel = new ViewModelProvider(requireActivity()).get(ExperienceViewModel.class);

        Set<Experience> experiences = experienceDataHolder.getExperienceByOperaId(operaId);
        if(experiences != null) {
            for(Experience experience : experiences) {
                if(experience.getId().equals(singleQuestionId)) {
                    viewModel.setSingleQuestion((SingleQuestion) experience);
                    break;
                }
            }
        }
        experienceViewModel.setExperience(viewModel.getSingleQuestion());

        questionEditText = view.findViewById(ui.singleQuestionEditorUi.questionEditText);
        answersRecyclerView = view.findViewById(ui.singleQuestionEditorUi.answersRecyclerViewId);
        addAnswerButton = view.findViewById(ui.singleQuestionEditorUi.addAnswerButtonId);
        saveButton = view.findViewById(ui.singleQuestionEditorUi.saveButtonId);
        toolbar = view.findViewById(ui.singleQuestionEditorUi.toolbar);

        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        MenuHost menuHost = (MenuHost) requireActivity();
        ToolbarManager.setIcons(toolbar, activity, menuHost, this, R.string.single_question_help);
    }

    @Override
    public void onStart() {
        super.onStart();

        ((OnDataLoadListener) requireActivity()).onDataLoad();

        setupQuestionText();
        setupRecyclerView();

        saveButton.setOnClickListener(v -> {
            if(validate()) {
                experienceDataHolder.addExperienceToOpera(operaId, viewModel.getSingleQuestion());
                requireActivity().finish();
            }
        });
    }

    private void setupQuestionText() {
        String question = viewModel.getSingleQuestion().getQuestion();

        if(question != null && !question.isEmpty()) {
            questionEditText.setText(question);
        }

        questionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.getSingleQuestion().setQuestion(editable.toString());
            }
        });
    }

    private void setupRecyclerView() {
        if(adapter != null) return;

        adapter = new AnswerAdapter(
                requireContext(),
                new ArrayList<>(viewModel.getSingleQuestion().getAnswers()),
                this,
                answer -> {
                    viewModel.getSingleQuestion().getAnswers().remove(answer);
                    ((OnClickDeleteListener<Object>) requireActivity()).onClickDelete(answer);
                }
        );
        answersRecyclerView.setAdapter(adapter);
        answersRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        addAnswerButton.setOnClickListener(v -> new AddAnswerAlertDialog(requireContext(), this).show());
    }

    @Override
    public void onAnswerCreated(Answer answer) {
        viewModel.addAnswer(answer);
        adapter.addAnswer(answer);
        adapter.notifyDataSetChanged();
    }

    private boolean validate() {
        SingleQuestion question = viewModel.getSingleQuestion();
        Set<Answer> answers = viewModel.getSingleQuestion().getAnswers();

        if(question.getQuestion() == null || question.getQuestion().isEmpty()) {
            questionEditText.setError(getString(R.string.questions_empty));
            questionEditText.requestFocus();
            return false;
        }

        if(answers == null || answers.isEmpty()) {
            Toast.makeText(requireContext(), R.string.answers_missing, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(question.countCorrectAnswers() == 0) {
            Toast.makeText(requireContext(), R.string.correct_answer_missing, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(question.countCorrectAnswers() == answers.size()) {
            Toast.makeText(requireContext(), R.string.incorrect_answer_missing, Toast.LENGTH_SHORT).show();
            return false;
        }

        TimedExperienceEditorFragment fragment = (TimedExperienceEditorFragment) getChildFragmentManager().findFragmentById(ui.singleQuestionEditorUi.experienceFragmentContainerViewId);
        return fragment.validate();
    }
}