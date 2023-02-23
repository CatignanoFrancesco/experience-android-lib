package it.uniba.eculturetool.experience_lib.fragments.quiz;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.chip.ChipGroup;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import it.uniba.eculturetool.experience_lib.R;
import it.uniba.eculturetool.experience_lib.models.Answer;
import it.uniba.eculturetool.experience_lib.utils.ConnectivityUtils;
import it.uniba.eculturetool.tag_lib.tag.model.LanguageTag;
import it.uniba.eculturetool.tag_lib.textmaker.facade.TextMaker;
import it.uniba.eculturetool.tag_lib.viewhelpers.EditingTagViewHelper;
import it.uniba.eculturetool.tag_lib.viewhelpers.LanguageTagViewData;

public class AddAnswerAlertDialog {
    private Context context;
    private View layout;
    private ChipGroup chipGroup;
    private EditText answerEditText;
    private CheckBox checkBox;
    private ImageButton translateButton;

    private List<LanguageTag> languageTags;
    private LanguageTagViewData  languageTagViewData;
    private EditingTagViewHelper editingTagViewHelper;
    private Map<String, String> answers;
    private QuestionEditorFragment questionEditorFragment;
    private Answer answer;

    public AddAnswerAlertDialog(Context context, QuestionEditorFragment questionEditorFragment, List<LanguageTag> languageTags) {
        this(context, questionEditorFragment, languageTags, null);
    }

    public AddAnswerAlertDialog(Context context, QuestionEditorFragment questionEditorFragment, List<LanguageTag> languageTags, Answer answer) {
        this.answer = answer;
        this.context = context;
        this.languageTags = languageTags;
        this.questionEditorFragment = questionEditorFragment;

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = layoutInflater.inflate(R.layout.add_answer_layout, null);

        chipGroup = layout.findViewById(R.id.answer_language_chip_group);
        answerEditText = layout.findViewById(R.id.answer_edit_text);
        checkBox = layout.findViewById(R.id.correct_checkbox);
        translateButton = layout.findViewById(R.id.answer_translate_button);

        setTags();
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(layout).setPositiveButton(R.string.save, this::onClickListener).setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss()).show();
    }

    private void setTags() {
        languageTagViewData = new LanguageTagViewData(Locale.getDefault().getLanguage().toUpperCase());
        editingTagViewHelper = new EditingTagViewHelper(context, languageTagViewData, answerEditText, translateButton, chipGroup);
        loadData();
        editingTagViewHelper.setDescriptionEditTextBehavior();
        answers = languageTagViewData.getDescriptions();
        editingTagViewHelper.setSimpleChipGroupBehaviorForLanguages(languageTags);
        for(LanguageTag languageTag : languageTags) languageTagViewData.addTag(languageTag);

        translation();
    }

    private void loadData() {
        if(answer != null) {
            languageTagViewData.setDescriptions(answer.getAnswerTexts());
            checkBox.setChecked(answer.isCorrect());
        }
    }

    private void translation() {
        // Generazione della traduzione
        TextMaker textMaker = TextMaker.getInstance(context.getString(R.string.deepl_auth_key));
        translateButton.setOnClickListener(view -> {
            if (!ConnectivityUtils.isNetworkAvailable(context)) {
                Toast.makeText(context, context.getString(R.string.msg_internet_non_disponibile), Toast.LENGTH_LONG).show();
                return;
            }

            // Se c'è la connessione ad internet, si può effettuare la traduzione
            for (LanguageTag languageTag : languageTagViewData.getTargetLanguages()) {
                textMaker.generateText(
                        answerEditText.getText().toString(),
                        languageTag,
                        bundle -> {
                            translateButton.setVisibility(View.GONE);
                            answers.put(languageTag.getLanguage(), bundle.getString(languageTag.getLanguage()));
                            languageTagViewData.setDescriptions(answers);
                            Toast.makeText(context, context.getString(it.uniba.eculturetool.tag_lib.R.string.successo_traduzione) + languageTag.getTitle(), Toast.LENGTH_LONG).show();
                        },
                        tag -> Toast.makeText(context, context.getString(it.uniba.eculturetool.tag_lib.R.string.errore_traduzione) + tag.getTitle(), Toast.LENGTH_LONG).show()
                );
            }
        });
    }

    private void onClickListener(DialogInterface dialogInterface, int i) {
        if(!validate()) return;

        questionEditorFragment.onAnswerCreated(answer != null ? answer : new Answer(answers, checkBox.isChecked()));
    }

    private boolean validate() {
        if(answers.size() < languageTagViewData.getAddedLanguages().getValue().size()) {
            answerEditText.setError(context.getString(R.string.empty_answer));
            answerEditText.requestFocus();
            return false;
        }
        return true;
    }
}
