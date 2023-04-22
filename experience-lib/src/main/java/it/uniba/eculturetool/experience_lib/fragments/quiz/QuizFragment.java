package it.uniba.eculturetool.experience_lib.fragments.quiz;

import static it.uniba.eculturetool.experience_lib.ExperienceEditorFragment.KEY_EXPERIENCE_ID;
import static it.uniba.eculturetool.experience_lib.ExperienceEditorFragment.KEY_OPERA_ID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Set;

import it.uniba.eculturetool.experience_lib.ExperienceDataHolder;
import it.uniba.eculturetool.experience_lib.models.Experience;
import it.uniba.eculturetool.experience_lib.models.Quiz;
import it.uniba.eculturetool.experience_lib.ui.QuizUI;

public class QuizFragment extends Fragment {
    private final QuizUI ui = QuizUI.getInstance();

    private QuizViewModel quizViewModel;
    private final ExperienceDataHolder experienceDataHolder = ExperienceDataHolder.getInstance();
    private String operaId;
    private String quizId;

    public QuizFragment() {}

    public static QuizFragment newInstance(String operaId, String quizId) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_OPERA_ID, operaId);
        bundle.putString(KEY_EXPERIENCE_ID, quizId);

        QuizFragment quizFragment = new QuizFragment();
        quizFragment.setArguments(bundle);
        return quizFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        quizViewModel = new ViewModelProvider(requireActivity()).get(QuizViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(ui.quizFragmentUI.layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        operaId = getArguments().getString(KEY_OPERA_ID);
        quizId = getArguments().getString(KEY_EXPERIENCE_ID);

        Set<Experience> experiences = experienceDataHolder.getExperienceByOperaId(operaId);
        if(experiences != null) {
            for(Experience experience : experiences) {
                if(experience.getId().equals(quizId)) {
                    quizViewModel.setQuiz((Quiz) experience);
                    break;
                }
            }
        }

        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(ui.quizFragmentUI.quizEditorFragmentContainerViewId, new QuizEditorFragment(this))
                .commit();
    }

    public void addQuestion() {
        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .replace(ui.quizFragmentUI.quizEditorFragmentContainerViewId, new QuestionEditorFragment())
                .commit();
    }

    public void saveQuiz(Quiz quiz) {
        experienceDataHolder.addExperienceToOpera(operaId, quiz);
        requireActivity().finish();
    }

    public Fragment getActualFragment() {
        return getChildFragmentManager().findFragmentById(ui.quizFragmentUI.quizEditorFragmentContainerViewId);
    }

    public void back() {
        getChildFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}