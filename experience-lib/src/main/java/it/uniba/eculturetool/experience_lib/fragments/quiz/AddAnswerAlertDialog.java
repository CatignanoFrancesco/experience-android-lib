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
    private QuestionEditorFragment questionEditorFragment;
    private Answer answer;

    public AddAnswerAlertDialog(Context context, QuestionEditorFragment questionEditorFragment) {
        this(context, questionEditorFragment, null);
    }

    public AddAnswerAlertDialog(Context context, QuestionEditorFragment questionEditorFragment, Answer answer) {
        this.answer = answer;
        this.context = context;
        this.questionEditorFragment = questionEditorFragment;

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = layoutInflater.inflate(R.layout.add_answer_layout, null);

        chipGroup = layout.findViewById(R.id.answer_language_chip_group);
        answerEditText = layout.findViewById(R.id.answer_edit_text);
        checkBox = layout.findViewById(R.id.correct_checkbox);
        translateButton = layout.findViewById(R.id.answer_translate_button);

        loadData();
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(layout).setPositiveButton(R.string.save, this::onClickListener).setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss()).show();
    }

    private void loadData() {
        if(answer != null) {
            answerEditText.setText(answer.getAnswerText());
            checkBox.setChecked(answer.isCorrect());
        }
    }

    private void onClickListener(DialogInterface dialogInterface, int i) {
        if(!validate()) return;

        questionEditorFragment.onAnswerCreated(answer != null ? answer : new Answer(answerEditText.getText().toString(), checkBox.isChecked()));
    }

    private boolean validate() {
        if(answerEditText.getText().toString().isEmpty()) {
            answerEditText.setError(context.getString(R.string.empty_answer));
            answerEditText.requestFocus();
            return false;
        }
        return true;
    }
}
