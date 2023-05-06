package it.uniba.eculturetool.experience_lib.fragments.quiz;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

import it.uniba.eculturetool.experience_lib.ExperienceViewModel;
import it.uniba.eculturetool.experience_lib.R;
import it.uniba.eculturetool.experience_lib.TimedExperienceEditorFragment;
import it.uniba.eculturetool.experience_lib.models.Question;
import it.uniba.eculturetool.experience_lib.ui.QuizUI;
import it.uniba.eculturetool.experience_lib.utils.ToolbarManager;

public class QuizEditorFragment extends Fragment {
    private final QuizUI ui = QuizUI.getInstance();

    private TextView noQuizTextView;
    private Button addQuestionButton;
    private RecyclerView quizRecyclerView;
    private QuizFragment quizFragment;
    private Button saveButton;
    private Toolbar toolbar;

    private QuizViewModel quizViewModel;
    private ExperienceViewModel experienceViewModel;

    public QuizEditorFragment(QuizFragment quizFragment) {
        this.quizFragment = quizFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(ui.quizEditorFragmentUI.layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        quizViewModel = new ViewModelProvider(requireActivity()).get(QuizViewModel.class);
        experienceViewModel = new ViewModelProvider(requireActivity()).get(ExperienceViewModel.class);

        experienceViewModel.setExperience(quizViewModel.getQuiz().getValue());

        noQuizTextView = view.findViewById(ui.quizEditorFragmentUI.noQuestionsTextViewId);
        addQuestionButton = view.findViewById(ui.quizEditorFragmentUI.addQuestionButtonId);
        quizRecyclerView = view.findViewById(ui.quizEditorFragmentUI.questionsRecyclerViewId);
        saveButton = view.findViewById(ui.quizEditorFragmentUI.quizSaveButtonId);
        toolbar = view.findViewById(ui.quizEditorFragmentUI.toolbar);

        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        MenuHost menuHost = (MenuHost) requireActivity();
        ToolbarManager.setIcons(toolbar, activity, menuHost, this, R.string.quiz_help);

        addQuestionButton.setOnClickListener(v -> quizFragment.addQuestion());
        saveButton.setOnClickListener(v -> {
            if(validate())
                quizFragment.saveQuiz(quizViewModel.getQuiz().getValue());
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        setRecycler();
    }

    public void editQuestion(Question question) {
        quizViewModel.setActiveQuestion(question);
        quizFragment.addQuestion();
    }

    private void setRecycler() {
        Set<Question> questions = quizViewModel.getQuiz().getValue().getQuestions();

        if(questions != null && !questions.isEmpty()) {
            noQuizTextView.setVisibility(View.GONE);
        } else {
            noQuizTextView.setVisibility(View.VISIBLE);
        }

        QuestionAdapter adapter = new QuestionAdapter(
                requireContext(),
                this,
                new ArrayList<>(questions),
                question -> {
                    quizViewModel.getQuiz().getValue().getQuestions().remove(question);

                }
        );
        quizRecyclerView.setAdapter(adapter);
        quizRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private boolean validate() {
        if(quizViewModel.getQuiz().getValue().getQuestions().isEmpty()) {
            saveButton.setError(getString(R.string.questions_missing));
            return false;
        }

        TimedExperienceEditorFragment fragment = (TimedExperienceEditorFragment) getChildFragmentManager().findFragmentById(ui.quizEditorFragmentUI.experienceFragmentContainerViewId);
        return fragment.validate();
    }
}