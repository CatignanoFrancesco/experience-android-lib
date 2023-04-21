package it.uniba.eculturetool.experience_lib.fragments.quiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.uniba.eculturetool.experience_lib.R;
import it.uniba.eculturetool.experience_lib.listeners.OnClickDeleteListener;
import it.uniba.eculturetool.experience_lib.listeners.OnDataLoadListener;
import it.uniba.eculturetool.experience_lib.models.Answer;
import it.uniba.eculturetool.experience_lib.models.Question;
import it.uniba.eculturetool.experience_lib.ui.QuizUI;
import it.uniba.eculturetool.experience_lib.utils.ConnectivityUtils;
import it.uniba.eculturetool.tag_lib.tag.model.LanguageTag;
import it.uniba.eculturetool.tag_lib.textmaker.facade.TextMaker;
import it.uniba.eculturetool.tag_lib.viewhelpers.EditingTagViewHelper;
import it.uniba.eculturetool.tag_lib.viewhelpers.LanguageTagViewData;

public class QuestionEditorFragment extends Fragment {
    private final QuizUI ui = QuizUI.getInstance();

    private Button addAnswerButton;
    private TextInputEditText pointsText;
    private ChipGroup chipGroup;
    private TextInputEditText questionsText;
    private ImageButton translateButton;
    private Button addImageButton;
    private ImageView image;
    private RecyclerView recyclerView;
    private AnswerAdapter adapter;
    private Button saveButton;

    private QuizViewModel quizViewModel;
    private List<LanguageTag> languageTags;
    private LanguageTagViewData languageTagViewData;
    private EditingTagViewHelper editingTagViewHelper;
    private ActivityResultLauncher<Intent> pickPhoto;
    private int previousPoints = 0; // In caso di modifica questo valore servirà per calcolare la differenza di punteggio con il nuovo punteggio

    public QuestionEditorFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pickPhoto = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri uri = result.getData().getData();
                Picasso.get().load(uri).resize(image.getWidth(), image.getHeight()).centerCrop().into(image);

                try {
                    InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
                    quizViewModel.getActiveQuestion().setImage(BitmapFactory.decodeStream(inputStream));
                }
                catch(FileNotFoundException ex) {}
            }
        });

        quizViewModel = new ViewModelProvider(requireActivity()).get(QuizViewModel.class);
        previousPoints = quizViewModel.getActiveQuestion().getPoints();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(ui.questionEditorFragmentUI.layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        languageTags = quizViewModel.getLanguageTags();

        addAnswerButton = view.findViewById(ui.questionEditorFragmentUI.addAnswerButtonId);
        recyclerView = view.findViewById(ui.questionEditorFragmentUI.answersRecyclerViewId);
        pointsText = view.findViewById(ui.questionEditorFragmentUI.pointsInputTextId);
        chipGroup = view.findViewById(ui.questionEditorFragmentUI.languagesChipGroupId);
        questionsText = view.findViewById(ui.questionEditorFragmentUI.questionInputTextId);
        translateButton = view.findViewById(ui.questionEditorFragmentUI.questionTranslateImageButtonId);
        addImageButton = view.findViewById(ui.questionEditorFragmentUI.addQuestionImageButtonId);
        image = view.findViewById(ui.questionEditorFragmentUI.questionImageViewId);
        saveButton = view.findViewById(ui.questionEditorFragmentUI.saveButtonId);

        // Comportamento dei chip
        languageTagViewData = new LanguageTagViewData(Locale.getDefault().getLanguage().toUpperCase());
        editingTagViewHelper = new EditingTagViewHelper(requireContext(), languageTagViewData, questionsText, translateButton, chipGroup);
        if(!quizViewModel.getActiveQuestion().getQuestionTexts().isEmpty()) languageTagViewData.setDescriptions(quizViewModel.getActiveQuestion().getQuestionTexts());
        editingTagViewHelper.setDescriptionEditTextBehavior();
        quizViewModel.getActiveQuestion().setQuestionTexts(languageTagViewData.getDescriptions());
        editingTagViewHelper.setSimpleChipGroupBehaviorForLanguages(languageTags);
        for(LanguageTag languageTag : languageTags) languageTagViewData.addTag(languageTag);
    }

    @Override
    public void onStart() {
        super.onStart();

        translation();

        ((OnDataLoadListener) requireActivity()).onDataLoad();
        setRecyclerView();
        setPointsText();
        setImage();
        saveButton.setOnClickListener(v -> {
            if (validate()) {
                quizViewModel.saveQuestion(previousPoints);
                requireActivity().onBackPressed();
            }
        });
    }

    /**
     * Comportamento da avere quando si aggiunge una risposta
     * @param answer La risposta
     */
    public void onAnswerCreated(Answer answer) {
        quizViewModel.addAnswer(answer);
        adapter.addAnswer(answer);
        adapter.notifyDataSetChanged();
    }

    /**
     * Setta il comportamento dell'input dei punti
     */
    private void setPointsText() {
        pointsText.setText(String.valueOf(quizViewModel.getActiveQuestion().getPoints()));

        pointsText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                quizViewModel.getActiveQuestion().setPoints(charSequence.toString().equals("") ? 0 : Integer.parseInt(charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    /**
     * Imposta il comportamento dell'immagine e del pulsante
     */
    private void setImage() {
        if(quizViewModel.getActiveQuestion().hasImage()) {
            if(quizViewModel.getActiveQuestion().getImage() != null) {
                this.image.setImageBitmap(quizViewModel.getActiveQuestion().getImage());
            }
        }

        addImageButton.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickPhoto.launch(photoPickerIntent);
        });
    }

    /**
     * Imposta il comportamento della recyclerview e di tutto ciò che è collegato alle risposte
     */
    private void setRecyclerView() {
        if(adapter != null) return;

        adapter = new AnswerAdapter(
                requireContext(),
                new ArrayList<>(quizViewModel.getAnswers()),
                this,
                answer -> {
                    quizViewModel.getActiveQuestion().getAnswers().remove(answer);
                    ((OnClickDeleteListener<Object>) requireActivity()).onClickDelete(answer);
                }
        );
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        addAnswerButton.setOnClickListener(v -> new AddAnswerAlertDialog(requireContext(), this).show());
    }

    /**
     * Effettua la traduzione
     */
    private void translation() {
        // Generazione della traduzione
        Context context = requireContext();
        TextMaker textMaker = TextMaker.getInstance(context.getString(R.string.deepl_auth_key));

        translateButton.setOnClickListener(view -> {
            if (!ConnectivityUtils.isNetworkAvailable(context)) {
                Toast.makeText(context, context.getString(R.string.msg_internet_non_disponibile), Toast.LENGTH_LONG).show();
                return;
            }

            // Se c'è la connessione ad internet, si può effettuare la traduzione
            for (LanguageTag languageTag : languageTagViewData.getTargetLanguages()) {
                textMaker.generateText(
                        questionsText.getText().toString(),
                        languageTag,
                        bundle -> {
                            translateButton.setVisibility(View.GONE);
                            quizViewModel.getActiveQuestion().getQuestionTexts().put(languageTag.getLanguage(), bundle.getString(languageTag.getLanguage()));
                            languageTagViewData.setDescriptions(quizViewModel.getActiveQuestion().getQuestionTexts());
                            Toast.makeText(context, context.getString(it.uniba.eculturetool.tag_lib.R.string.successo_traduzione) + languageTag.getTitle(), Toast.LENGTH_LONG).show();
                        },
                        tag -> Toast.makeText(context, context.getString(it.uniba.eculturetool.tag_lib.R.string.errore_traduzione) + tag.getTitle(), Toast.LENGTH_LONG).show()
                );
            }
        });
    }

    /**
     * Controlla che tutti i dati siano stati inseriti correttamente
     * @return true se i dati sono inseriti correttamente, false altrimenti
     */
    private boolean validate() {
        Question question = quizViewModel.getActiveQuestion();

        if(question.getPoints() == 0) {
            pointsText.setError(requireContext().getString(R.string.points_missing));
            pointsText.requestFocus();
            return false;
        }

        if(question.getQuestionTexts().size() < languageTagViewData.getAddedLanguages().getValue().size()) {
            questionsText.setError(requireContext().getString(R.string.questions_empty));
            questionsText.requestFocus();
            return false;
        }

        if(question.getAnswers().size() < 1) {
            addAnswerButton.setError(requireContext().getString(R.string.answers_missing));
            return false;
        }

        if(question.countCorrectAnswers() == 0) {
            addAnswerButton.setError(requireContext().getString(R.string.correct_answer_missing));
            return false;
        }

        if(question.countCorrectAnswers() == question.getAnswers().size()) {
            addAnswerButton.setError(requireContext().getString(R.string.incorrect_answer_missing));
            return false;
        }

        return true;
    }
}