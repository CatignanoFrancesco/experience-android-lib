package it.uniba.eculturetool.experience_lib.fragments.questionnaire;

import static it.uniba.eculturetool.experience_lib.ExperienceEditorFragment.KEY_EXPERIENCE_ID;
import static it.uniba.eculturetool.experience_lib.ExperienceEditorFragment.KEY_OPERA_ID;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import it.uniba.eculturetool.experience_lib.ExperienceDataHolder;
import it.uniba.eculturetool.experience_lib.R;
import it.uniba.eculturetool.experience_lib.models.Experience;
import it.uniba.eculturetool.experience_lib.models.Questionnaire;
import it.uniba.eculturetool.experience_lib.ui.QuestionnaireUI;

public class QuestionnaireFragment extends Fragment {
    private final QuestionnaireUI ui = QuestionnaireUI.getInstance();

    private QuestionnaireViewModel viewModel;
    private final ExperienceDataHolder experienceDataHolder = ExperienceDataHolder.getInstance();

    private String operaId;
    private String questionnaireId;

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private QuestionnaireAdapter adapter;
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

        Set<Experience> experiences = experienceDataHolder.getExperienceByOperaId(operaId);
        if(experiences != null) {
            for(Experience experience : experiences) {
                if(experience.getId().equals(questionnaireId)) {
                    viewModel.setQuestionnaire((Questionnaire) experience);
                    break;
                }
            }
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

    @Override
    public void onStart() {
        super.onStart();

        if(viewModel.getQuestionnaire().getName() == null) {
            chooseQuestionnaireDialog(questionnaire -> viewModel.setQuestionnaire(questionnaire));
        }

        setupRecyclerView();

        saveButton.setOnClickListener(v -> {
            experienceDataHolder.addExperienceToOpera(operaId, viewModel.getQuestionnaire());
            requireActivity().finish();
        });
    }

    private void setupRecyclerView() {
        adapter = new QuestionnaireAdapter(requireContext(), viewModel.getQuestionnaire().getPreparedQuestions());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    private void chooseQuestionnaireDialog(Consumer<Questionnaire> onQuestionnaireClick) {
        List<Questionnaire> questionnaireList;

        // Ottengo la lista dei questionari disponibili
        try(InputStream inputStream = getResources().openRawResource(R.raw.questionnaire_list)) {
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            String json = new String(buffer, StandardCharsets.UTF_8);

            Type questionnaireListType = new TypeToken<ArrayList<Questionnaire>>() {}.getType();
            questionnaireList = new Gson().fromJson(json, questionnaireListType);
        }
        catch (IOException e) {
            Log.e(QuestionnaireFragment.class.getSimpleName(), "chooseQuestionnaireDialog: ", e);
            return;
        }

        // Lista dei questionari
        String[] questionnaireNames = questionnaireList.stream().map(Questionnaire::getName).toArray(String[]::new);

        // Mostro il dialog
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.choose_questionnaire)
                .setItems(questionnaireNames, (dialogInterface, i) -> onQuestionnaireClick.accept(questionnaireList.get(i)))
                .setCancelable(false)
                .setNegativeButton(R.string.back, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    requireActivity().finish();
                }).show();
    }
}